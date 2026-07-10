# Future vs ExecutorService.submit(...)

`submit()` and `Future` aren't alternatives — `submit()` is how you obtain a `Future` in the first place. `execute()` gives you nothing back; `submit()` hands you a live handle to the running computation.

## 1. The three `submit()` overloads

```java
Future<?>      submit(Runnable task);            // get() returns null on success
<T> Future<T>  submit(Runnable task, T result);   // get() returns the supplied `result` on success
<T> Future<T>  submit(Callable<T> task);          // get() returns the Callable's computed value
```

The 2-arg `Runnable` overload is the lesser-known one: wrap a `Runnable` (no return value) but hand back a fixed result once it completes — useful when you want a `Future` handle for a void task but still want a sentinel value from `get()`.

## 2. What `Future<V>` gives you

| Method | Behavior |
|---|---|
| `V get()` | Blocks indefinitely. Returns result, or throws `ExecutionException` (task threw) / `InterruptedException` (caller interrupted while waiting) |
| `V get(long timeout, TimeUnit unit)` | Bounded wait; throws `TimeoutException` if deadline passes first |
| `boolean isDone()` | Non-blocking completion check — lets you poll instead of blocking |
| `boolean cancel(boolean mayInterruptIfRunning)` | Attempt to cancel (see below) |
| `boolean isCancelled()` | Was it cancelled |

## 3. Cancellation is cooperative, not forced

- Task still queued, not started → `cancel()` removes it cleanly, guaranteed, returns `true`.
- Task already running, `cancel(true)` → calls `Thread.interrupt()` on the worker. Only actually stops the task if it's currently in an interruptible operation (`sleep`, `wait`, blocking I/O) or explicitly polls `Thread.currentThread().isInterrupted()`. A tight CPU loop that checks nothing **keeps running to completion** regardless.
- Task already running, `cancel(false)` → lets it finish either way; `isCancelled()` becomes `true` afterward and `get()` throws `CancellationException`.
- Task already completed → `cancel()` returns `false`, no effect.

**Interrupt is a flag, not a kill switch.** This is the single most important thing to internalize about cancellation in Java.

## 4. Code

```java
ExecutorService pool = Executors.newFixedThreadPool(2);

// submit(Runnable) -> Future<?>
Future<?> f1 = pool.submit(() -> System.out.println("task ran"));
f1.get(); // blocks, returns null

// submit(Runnable, T result) -> Future<T>
Future<String> f2 = pool.submit(() -> doWork(), "done-marker");
String marker = f2.get(); // "done-marker" once task completes normally

// submit(Callable<T>) -> Future<T>
Future<Integer> f3 = pool.submit(() -> {
    Thread.sleep(2000);
    return 42;
});

// poll instead of block
while (!f3.isDone()) {
    // do other work
}
int result = f3.get(); // won't actually block, isDone() already true

// bounded wait
try {
    int r = f3.get(500, TimeUnit.MILLISECONDS);
} catch (TimeoutException e) {
    // still running after 500ms
}

// cancellation — only works if the task cooperates
Future<Integer> f4 = pool.submit(() -> {
    while (!Thread.currentThread().isInterrupted()) {
        // long-running work, cooperatively checking interrupt flag
    }
    return -1;
});
f4.cancel(true); // sets interrupt flag on the worker running f4
```

## 5. The structural limitation — why `CompletableFuture` exists

A plain `Future` can only be **blocked on** or **polled**. There's no way to register "run this callback when done," and no way to combine or chain multiple `Future`s together — you either call `get()` and block the calling thread, or busy-poll `isDone()` and waste cycles. `CompletableFuture` (Java 8) exists specifically to close this gap (callbacks, chaining, combining) — natural next topic.

## 6. Under the hood: `Future` vs `FutureTask`

`Future` is just the read-only interface (`get`/`cancel`/`isDone`). `FutureTask` is the concrete class that actually **holds** your `Callable`, runs it, and completes the `Future` when done — it implements `RunnableFuture` (`Runnable` + `Future`). When you call `pool.submit(callable)`, the executor wraps your task in a `FutureTask` internally and returns the `Future` view of it. This is the same bridge class used to run a `Callable` on a raw `Thread` (see the Thread/Runnable/Callable note).

## 7. Self-check

Q: You call `future.cancel(true)` on a task mid-way through a plain `for` loop doing arithmetic — no I/O, no `sleep`, never checks `isInterrupted()`. Does the task actually stop? What do `isCancelled()` and a subsequent `get()` report?

A: The task **does not stop** — `interrupt()` only sets a flag; a loop that never checks it runs to completion untouched. `isCancelled()` still reports `true` (the cancel request itself succeeded from the `Future`'s bookkeeping perspective), and once the task finishes on its own, `get()` throws `CancellationException` rather than returning the computed result — the result is discarded even though the computation ran to completion.

## 8. Know cold (summary)

- `submit()` is how you get a `Future`; `execute()` gives you nothing back.
- Three `submit()` overloads: `Runnable`→`Future<?>` (null result), `Runnable+result`→`Future<T>` (fixed result), `Callable<T>`→`Future<T>` (real result).
- `get()` blocks; `get(timeout, unit)` bounds the wait; `isDone()` lets you poll without blocking.
- Cancellation is cooperative — `cancel(true)` just interrupts; a non-cooperative task ignores it and keeps running, though `get()` afterward still throws `CancellationException`.
- Plain `Future` can't do callbacks or composition — that's what `CompletableFuture` is for.
- `FutureTask` is the concrete `Runnable`+`Future` implementation that executors use under the hood for every `submit()` call.
