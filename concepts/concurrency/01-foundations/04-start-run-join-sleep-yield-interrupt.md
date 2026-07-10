# start vs run · join · sleep · yield · interrupt

## 1. `start()` vs `run()`

`start()` allocates a new OS-backed thread, sets up its call stack, and eventually invokes `run()` **on that new thread**. `run()` called directly is just an ordinary method call — executes synchronously on whatever thread called it, no new thread, no concurrency at all. Classic "spot the bug" trap.

`start()` can only be called **once** per `Thread` instance — a second call throws `IllegalThreadStateException`.

## 2. `join()`

Blocks the *calling* thread until the target thread terminates. Implemented internally via `wait()` on the `Thread` object, so the caller actually enters `WAITING` (no-arg `join()`) or `TIMED_WAITING` (`join(ms)`).

```java
Thread worker = new Thread(() -> doWork());
worker.start();
worker.join();          // blocks caller until worker terminates
worker.join(2000);      // bounded wait — up to 2s
```

**Gotcha:** `join(ms)` with a timeout does **not** stop or cancel the target thread if it hasn't finished. It only means the caller stops waiting and moves on — the target thread keeps running in the background regardless. A timed `join()` is not cancellation.

Typical use: spawn N worker threads, `join()` each one, then aggregate results — guarantees all workers have finished before you read their output.

## 3. `sleep(ms)`

Static method. Pauses the **current** thread for *at least* the given duration — no upper bound guarantee (OS scheduler granularity can make it longer, never shorter).

**Does not release any locks** the thread holds — contrast with `wait()`, which does. This is the classic `sleep()` vs `wait()` distinction.

Throws checked `InterruptedException` if interrupted while sleeping, and **clears the interrupt flag** as part of throwing it.

## 4. `yield()`

Pure scheduler *hint*: "I'm willing to let another thread of equal/higher priority run." The JVM/OS is free to ignore it entirely — no guarantee of any effect. The thread stays `RUNNABLE` the whole time; there is no state transition from calling `yield()`. Rarely used in production beyond spin-wait tricks; behavior is platform-dependent and unreliable.

## 5. `interrupt()` / `isInterrupted()` / `Thread.interrupted()` — cooperative cancellation

This is the mechanism, and it only works if the target thread cooperates:

- `interrupt()` called on a thread currently blocked in an **interruptible** operation (`sleep`, `wait`, `join`, `lockInterruptibly`, some blocking I/O) → that call immediately throws `InterruptedException`, and the interrupt flag is cleared as part of throwing it.
- `interrupt()` called on a thread running ordinary, non-blocking code → does nothing automatically. It just sets a boolean flag. Execution doesn't stop until the running code explicitly checks the flag and decides to exit.
- `isInterrupted()` (instance method) — reads the flag, does **not** clear it.
- `Thread.interrupted()` (**static** method) — reads **and clears** the flag, but always for the **currently executing thread**, regardless of which `Thread` reference you called it through. `someOtherThread.interrupted()` still checks whatever thread is running that line of code, not `someOtherThread` — a well-known footgun (IDEs typically flag static-method-called-via-instance as suspicious for exactly this reason).

**Standard cancellable-task pattern:**
```java
public void run() {
    while (!Thread.currentThread().isInterrupted()) {
        // do work, check periodically
    }
    // cleanup, exit cleanly
}
```

**Restore-the-flag idiom** — required because catching `InterruptedException` clears the flag. If you catch it somewhere you can't propagate further (e.g., inside `Runnable.run()`, which declares no `throws`), you must re-set it or the cancellation signal is lost for any code higher up the call stack:
```java
try {
    Thread.sleep(1000);
} catch (InterruptedException e) {
    Thread.currentThread().interrupt(); // restore — don't just swallow it
}
```

## 6. Summary table

| Method | Static/instance | Blocks the caller? | Effect |
|---|---|---|---|
| `start()` | instance | No | Spawns new thread; target goes `NEW` → `RUNNABLE` |
| `run()` (called directly) | instance | Executes synchronously | No new thread; no state change |
| `join()` / `join(ms)` | instance | Yes | Caller enters `WAITING`/`TIMED_WAITING` until target terminates (or timeout) |
| `sleep(ms)` | static | Yes (current thread) | Current thread enters `TIMED_WAITING`; holds any locks it has |
| `yield()` | static | No | Pure hint; thread stays `RUNNABLE`, no guaranteed effect |
| `interrupt()` | instance | No | Sets flag; wakes early with `InterruptedException` only if target is in an interruptible blocking call |
| `isInterrupted()` | instance | N/A | Reads flag, doesn't clear |
| `Thread.interrupted()` | static | N/A | Reads **and clears** flag — always for the *current* thread |

## 7. Self-check

Q: Thread `T` is in a tight CPU loop checking `isInterrupted()` each iteration. Main calls `T.interrupt()`. What happens?

Now suppose instead `T` is currently blocked inside `Thread.sleep(5000)` when `interrupt()` is called — what happens, and what does `T.isInterrupted()` report immediately after, assuming the `catch` block does nothing?

A: **CPU loop case:** `interrupt()` just sets the flag; nothing stops immediately. On `T`'s next loop iteration, `isInterrupted()` returns `true` and the loop body can choose to exit — cancellation only takes effect at the next check, entirely dependent on the code cooperating.

**`sleep()` case:** `interrupt()` causes `sleep()` to immediately throw `InterruptedException`, and the interrupt flag is cleared as part of that. If the `catch` block does nothing (doesn't call `Thread.currentThread().interrupt()`), then `T.isInterrupted()` reports **`false`** right after — the cancellation signal has been silently lost.

## 8. Know cold (summary)

- `start()` spawns a new thread; `run()` called directly does not — it's just a method call.
- `join()` blocks the caller until the target finishes; a timed `join()` gives up waiting, it does not cancel the target.
- `sleep()` holds locks; `yield()` is an unreliable scheduler hint with no state change.
- `interrupt()` only forcibly unblocks a thread that's in an interruptible blocking call — otherwise it's just a flag the running code must check itself. Cancellation in Java is cooperative, never forced.
- Catching `InterruptedException` clears the flag — always restore it (`Thread.currentThread().interrupt()`) if you can't propagate the exception, or the cancellation signal disappears.
