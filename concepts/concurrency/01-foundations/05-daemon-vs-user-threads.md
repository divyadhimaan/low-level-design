# Daemon vs User Threads

## 1. The rule

Every `Thread` carries a daemon boolean. A new thread **inherits the daemon status of whatever thread created it**, at the moment of creation — not the status of whatever it'll run alongside later. `main` is non-daemon (user) by default, so any thread spawned from it is also non-daemon unless `setDaemon(true)` is called explicitly.

**JVM exit condition:** the JVM keeps running as long as **at least one non-daemon thread is alive**. Once every non-daemon thread finishes (including `main` returning), the JVM begins shutdown, and any remaining daemon threads are **abruptly killed mid-execution** — no guarantee their `finally` blocks run, no guarantee their state is left consistent.

```java
Thread worker = new Thread(() -> {
    while (true) {
        // background housekeeping, e.g. cache eviction
        try { Thread.sleep(1000); } catch (InterruptedException e) { Thread.currentThread().interrupt(); return; }
    }
});
worker.setDaemon(true);  // must be set BEFORE start()
worker.start();
// main() returns here -> JVM exits immediately; worker's infinite loop is just cut off
```

## 2. Gotcha: `setDaemon()` must be called before `start()`

Once a thread is running, its daemon status is fixed. Calling `setDaemon()` after `start()` throws `IllegalThreadStateException` — you cannot convert a live thread from user to daemon or vice versa.

```java
Thread t = new Thread(task);
t.start();
t.setDaemon(true); // IllegalThreadStateException — too late, already started
```

## 3. Choosing daemon vs user

- **Daemon** — background work where an incomplete/abrupt stop is acceptable: cache eviction, health-check polling, log flushing. The JVM's own GC and JIT compiler threads are daemon threads.
- **User (non-daemon)** — anything whose completion actually matters: writing to disk, finishing a DB transaction, handling an in-flight request. Must survive to completion even if everything else in the app has finished, so it can't be daemon.

## 4. Direct tie-in: why forgetting `ExecutorService.shutdown()` hangs the JVM

This is the mechanism behind the gotcha in [03a](03a-raw-thread-vs-executorservice.md). `Executors`' default `ThreadFactory` creates **non-daemon** worker threads. An idle pool worker blocked on the internal task queue is a live non-daemon thread — it keeps the JVM alive indefinitely until `shutdown()` is called.

If you supply a custom `ThreadFactory` that marks pool threads as daemon instead, forgetting `shutdown()` no longer hangs the JVM — but now you lose the guarantee that in-flight/queued tasks finish before the process exits. This is a genuine trade-off, not just a bug to avoid: daemon pool threads sacrifice graceful completion for exit-safety.

## 5. Guaranteed cleanup on exit

Don't rely on a daemon thread's own `finally` block for cleanup — the JVM doesn't unwind a daemon thread's stack during this kind of shutdown, it just halts it. If cleanup must happen regardless, register a JVM shutdown hook:
```java
Runtime.getRuntime().addShutdownHook(new Thread(() -> {
    // guaranteed to run (within time limits) when the JVM begins shutdown
}));
```
This runs independent of any thread's daemon status.

## 6. Self-check

Q: `main` spawns a non-daemon thread doing a 10-second disk write, and a daemon thread doing an infinite polling loop, then `main` returns immediately. What does the JVM do, and what happens to each thread?

A: The JVM does **not** exit yet — the non-daemon disk-write thread is still alive, so the JVM waits for it. It runs to completion (~10s), and only once it finishes (and no other non-daemon threads remain) does the JVM shut down — at which point the still-running daemon polling loop is abruptly killed, with no guarantee of clean termination.

## 7. Know cold (summary)

- New threads inherit their creator's daemon status at creation time; `main` is non-daemon, so spawned threads default to non-daemon too.
- JVM exits once zero non-daemon threads remain; daemon threads still running at that point are killed abruptly, no cleanup guaranteed.
- `setDaemon()` must be called before `start()` — after that, it throws `IllegalThreadStateException`.
- Daemon = safe-to-abandon background work. User/non-daemon = must-complete work.
- `ExecutorService`'s default worker threads are non-daemon — this is exactly why forgetting `shutdown()` keeps the JVM alive forever.
- For cleanup that must happen regardless of daemon status, use `Runtime.getRuntime().addShutdownHook(...)`, not a daemon thread's `finally` block.
