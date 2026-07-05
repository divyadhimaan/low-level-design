# Raw Thread vs ExecutorService — what's actually different

Comparing:
```java
new Thread(runnable).start();
```
vs
```java
ExecutorService pool = Executors.newFixedThreadPool(2);
pool.execute(runnable);
pool.shutdown();
```

## 1. Thread creation & reuse

`new Thread(runnable).start()` creates exactly **one brand-new OS thread**, runs the task once, and the thread terminates for good when `run()` returns. Call this in a loop N times → N OS threads created and destroyed, full creation cost (~1MB stack, kernel scheduling setup) paid every time. This is the "thread-per-task" anti-pattern — doesn't scale, eventually blows up with `OutOfMemoryError: unable to create new native thread` under load.

`Executors.newFixedThreadPool(2)` creates a `ThreadPoolExecutor` with **2 persistent worker threads** backed by a shared task queue (`LinkedBlockingQueue` by default, unbounded). `pool.execute(runnable)` drops the task into the queue; a free worker thread picks it up, runs it, then loops back and blocks waiting for the next task instead of dying. Thread-creation cost is paid once, amortized across every task ever submitted to the pool.

## 2. Bounded vs unbounded concurrency

Raw thread creation has **no cap** — call `start()` 3 times, you get 3 threads truly running concurrently, no queueing. A fixed pool of size 2 caps live concurrency at 2 — a 3rd submitted task just sits in the queue until a worker frees up.

This bounding is the entire reason `ExecutorService` exists (`java.util.concurrent`, added Java 5, Doug Lea) — predictable, bounded resource usage instead of unbounded thread proliferation.

## 3. Lifecycle & the JVM-hang gotcha

A raw `Thread` needs no cleanup — once `run()` finishes it terminates naturally, nothing left dangling.

A pool's worker threads are **non-daemon by default** and sit idle (blocked on `queue.take()`) waiting for more work, indefinitely, unless told to stop.

**This is the real bug pattern:** forget `pool.shutdown()`, and `main()` can return while the JVM hangs forever, because 2 idle non-daemon worker threads are still alive waiting on the queue. One of the most common "why won't my program exit" issues in real Java code.

What `shutdown()` actually guarantees: it stops the pool from *accepting new tasks*. It is **non-blocking** — anything already queued or running keeps executing in the background; `shutdown()` does not wait for that. To actually wait, call `awaitTermination(timeout, unit)` afterward:
```java
pool.shutdown();
if (!pool.awaitTermination(60, TimeUnit.SECONDS)) {
    pool.shutdownNow(); // attempts to interrupt running tasks, drains unstarted queue
}
```
`shutdownNow()` is the aggressive variant — interrupts running tasks and returns the not-yet-started queue contents.

## 4. Exception visibility — no difference here

Both `new Thread(runnable).start()` and `pool.execute(runnable)` route an uncaught exception to the thread's `UncaughtExceptionHandler` (stderr by default) — same visible behavior. The silent-swallowing behavior from the Runnable/Callable note is specific to `submit()`, not `execute()`.

## 5. Summary table

| | `new Thread(runnable).start()` | `pool.execute(runnable)` on fixed pool |
|---|---|---|
| OS threads created | 1 new thread per call, every time | Fixed at pool size, created once, reused |
| Concurrency cap | None — unbounded | Capped at pool size; excess tasks queue |
| Thread lifetime | Dies when `run()` returns | Persists idle, waiting for next task |
| Resource risk at scale | OOM from unbounded thread creation | Bounded, predictable |
| Needs explicit shutdown? | No — self-terminates | **Yes** — idle non-daemon workers keep JVM alive otherwise |
| Uncaught exception | To `UncaughtExceptionHandler` (visible) | Same, via `execute()` |

## 6. Self-check

Q: Submit 5 tasks to `newFixedThreadPool(2)`, each takes 1 second. Roughly how long until all 5 finish, and why?

A: **~3 seconds.** Only 2 tasks run concurrently at a time (pool size = 2). Tasks 1 & 2 run in parallel (finish at ~1s), tasks 3 & 4 start next (finish at ~2s), task 5 runs alone last (finishes at ~3s). Concurrency is capped at the pool size — extra work queues rather than spawning more threads.

## 7. Know cold (summary)

- Raw `Thread`: one-shot, unbounded, no cleanup needed, no reuse.
- `ExecutorService`: reusable worker pool, bounded concurrency, queues excess work.
- Forgetting `shutdown()` on a pool = idle non-daemon threads keep the JVM alive forever — the classic "program won't exit" bug.
- `shutdown()` stops new submissions but doesn't block or wait; use `awaitTermination()` if you need to actually wait for completion.
- `execute()` surfaces uncaught exceptions the same way a raw thread does; it's `submit()` that changes exception behavior (captured in the `Future` instead).
