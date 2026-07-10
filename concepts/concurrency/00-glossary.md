# Glossary

Quick-reference definitions for terms used across these notes. Not a substitute for the full topic notes — just the fast lookup.

## Table of Contents

- [Checked Exceptions](#checked-exceptions)
- [Unchecked Exceptions](#unchecked-exceptions)
- [Future](#future)
- [ExecutorService](#executorservice)

## Checked Exceptions

Subclasses of `Exception` (excluding `RuntimeException`). The compiler enforces handling: a method must either declare them in its `throws` clause or catch them — code doesn't compile otherwise. They represent conditions a caller is expected to anticipate and recover from.

Examples relevant to concurrency: `InterruptedException`, `ExecutionException`, `TimeoutException` (all checked — note their common source is thread/task coordination, not "user input" style errors).

Why it matters here: `Runnable.run()` has no `throws` clause, so checked exceptions must be caught and handled *inside* the method. `Callable.call()` declares `throws Exception`, so checked exceptions can propagate naturally — this is the main structural reason `Callable` exists alongside `Runnable`. See [03](./03-thread-vs-runnable-vs-callable.md).

## Unchecked Exceptions

Subclasses of `RuntimeException` (a sibling category, `Error`, covers JVM-level problems like `OutOfMemoryError` that aren't meant to be caught at all). No compiler enforcement — no `throws` declaration required, no forced catch. They typically represent programming errors or conditions not expected to be handled locally.

Examples: `NullPointerException`, `IllegalStateException`, `IllegalArgumentException`, `UnsupportedOperationException`.

Concurrency-specific gotcha: `CancellationException` (thrown by `Future.get()` when the task was cancelled) is **unchecked** — it extends `IllegalStateException` — unlike `ExecutionException`/`TimeoutException`/`InterruptedException`, which are all checked. Easy to forget to handle since the compiler won't remind you. See [03b](./03b-future-vs-executorservice-submit.md).

## Future

Interface representing a handle to an asynchronous computation's result — obtained by calling `ExecutorService.submit(...)` (never from `execute()`, which returns nothing). Lets the caller block for the result (`get()`), bound the wait (`get(timeout, unit)`), poll without blocking (`isDone()`), or attempt cancellation (`cancel(mayInterruptIfRunning)`). Cancellation is cooperative, not forced — interrupting a task doesn't stop it unless the task itself checks for interruption. Can't chain or combine callbacks; that gap is what `CompletableFuture` closes. Full detail in [03b](01-foundations/03b-future-vs-executorservice-submit.md).

## ExecutorService

Interface (extends `Executor`) for running tasks on a managed pool of reusable worker threads instead of spinning up raw `Thread`s per task. Adds two things over plain `Executor`: task submission that returns a result handle (`submit()` → `Future`), and explicit lifecycle control (`shutdown()`, `shutdownNow()`, `awaitTermination()`). Typically backed by a `ThreadPoolExecutor`, created via `Executors` factory methods (`newFixedThreadPool`, `newCachedThreadPool`, `newSingleThreadExecutor`, `newScheduledThreadPool`, and since Java 21, `newVirtualThreadPerTaskExecutor`). Bounds concurrency and reuses threads — the entire point being avoiding unbounded, one-shot thread creation. Gotcha: its worker threads are non-daemon by default, so forgetting `shutdown()` leaves the JVM hanging even after `main()` returns. Full detail in [03a](01-foundations/03a-raw-thread-vs-executorservice.md).
