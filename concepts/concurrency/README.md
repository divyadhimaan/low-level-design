 — SDE2 Study Notes

Notes built topic-by-topic for deep concurrency prep (Java), each with mental models, Java specifics, interview gotchas, and a self-check.

## Module 1 — Thread Fundamentals

0. [Glossary](./00-glossary.md) — quick-reference definitions: Checked Exceptions, Unchecked Exceptions, Future, ExecutorService.
1. [Process vs Thread · Concurrency vs Parallelism](./01-process-vs-thread-concurrency-vs-parallelism.md) — what's shared (heap, metaspace, fds) vs per-thread (stack, PC, registers); why thread context switches are cheaper than process ones; Rob Pike's concurrency-is-structure/parallelism-is-execution framing; races are a concurrency problem, not a parallelism problem.
2. [Thread Lifecycle & States](./02-thread-lifecycle-states.md) — `NEW`/`RUNNABLE`/`BLOCKED`/`WAITING`/`TIMED_WAITING`/`TERMINATED`; which calls cause which transitions; `sleep()` vs `wait()`; why `notify()` moves a thread to `BLOCKED` first, not `RUNNABLE`; interrupt semantics per state.
3. [Thread vs Runnable vs Callable](./03-thread-vs-runnable-vs-callable.md) — composition over inheritance for task creation; return values and checked exceptions; `run()` vs `start()`; silent exception swallowing with `submit()` vs `execute()`; code samples for all three creation approaches plus the `FutureTask` bridge.
   - [3a. Raw Thread vs ExecutorService](./03a-raw-thread-vs-executorservice.md) — thread reuse and bounded concurrency in a pool; the "forgot to `shutdown()`" JVM-hang gotcha; `shutdown()` vs `shutdownNow()` vs `awaitTermination()`.
   - [3b. Future vs ExecutorService.submit(...)](./03b-future-vs-executorservice-submit.md) — the three `submit()` overloads; `get()`/`get(timeout)`/`isDone()`/`cancel()`; cancellation is cooperative, not forced; why `CompletableFuture` exists; `Future` vs `FutureTask`.
4. [start vs run · join · sleep · yield · interrupt](./04-start-run-join-sleep-yield-interrupt.md) — `start()` vs directly calling `run()`; `join()`/`join(ms)` and why a timed join doesn't cancel the target; `sleep()` holds locks; `yield()` is an unreliable hint; cooperative cancellation via `interrupt()`/`isInterrupted()`/`Thread.interrupted()` and the restore-the-flag idiom.
5. [Daemon vs User Threads](01-foundations/05-daemon-vs-user-threads.md) — JVM exits once zero non-daemon threads remain; daemon status is inherited from the creating thread at creation time; `setDaemon()` must be called before `start()`; why `ExecutorService`'s non-daemon workers are the reason forgetting `shutdown()` hangs the JVM.
6. [The Three Guarantees — Visibility · Atomicity · Ordering](01-foundations/06-visibility-atomicity-ordering.md) — the lens for every safety bug from here on; why `count++` isn't atomic; why unsynchronized flags can loop forever (visibility); broken double-checked locking (ordering); happens-before as the formal glue; which mechanism (`volatile`/`synchronized`/`Atomic*`) gives which guarantee; deadlock as a liveness bug, not a safety violation.

## Module 2 — Safety & Synchronization

11. [Race Conditions & Critical Sections](02-core-synchronization/11-race-conditions-critical-sections.md) — race condition vs the stricter "data race"; read-modify-write vs check-then-act (TOCTOU); finding the shared mutable state; sizing a critical section to the invariant, not a single field (the account-transfer example); lock-consistency and the `synchronized(new Object())` fake fix; preferring atomic compound ops like `computeIfAbsent` over hand-rolled check-then-act.
