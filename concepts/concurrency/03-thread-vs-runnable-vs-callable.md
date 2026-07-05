# Creating Threads — Thread vs Runnable vs Callable

## 1. The three ways

| | `extends Thread` | `implements Runnable` | `implements Callable<V>` |
|---|---|---|---|
| Method | `void run()` | `void run()` | `V call() throws Exception` |
| Return value | No | No | **Yes** — `V` |
| Checked exceptions | Must catch inside `run()` | Must catch inside `run()` | **Propagates** — declared `throws Exception` |
| Uses inheritance slot | Yes (extends Thread) | No | No |
| Can pass to `new Thread(...)` | Is the Thread itself | Yes — `new Thread(runnable)` | No — needs `FutureTask` wrapper or `ExecutorService` |
| Works with `ExecutorService` | Not idiomatic | `execute(Runnable)` / `submit(Runnable)` | `submit(Callable<V>)` → `Future<V>` |
| Typical use | Rare / legacy | Fire-and-forget task | Need a result and/or need checked exceptions to propagate |

Both `Runnable` and `Callable` are `@FunctionalInterface`s — lambda-friendly.

## 2. Why composition over inheritance

Prefer `implements Runnable`/`Callable` over `extends Thread`:

1. **Inheritance slot** — Java has single inheritance; extending `Thread` burns your only slot, so the class can't extend anything else.
2. **Decouples task from execution mechanism** — the task (business logic) shouldn't need to know or care whether it runs on a raw `Thread`, in a pool, or scheduled later.
3. **Reusability across execution strategies** — the same `Runnable`/`Callable` object can be handed to `new Thread(...)`, an `ExecutorService`, a `ScheduledExecutorService`, or just invoked directly (`task.run()`) in a unit test — without touching threading at all.

## 3. Three ways to create a thread — full code

### 3a. Extend `Thread`, override `run()`

```java
class HelloThread extends Thread {
    @Override
    public void run() {
        System.out.println("Running in: " + Thread.currentThread().getName());
    }
}

public class ExtendThreadDemo {
    public static void main(String[] args) {
        HelloThread t = new HelloThread();
        t.start(); // new call stack, new OS thread
        // t.run();  // WRONG — would just run synchronously on main, no new thread
    }
}
```
Uses your one inheritance slot; `HelloThread` can't extend anything else. Task and worker are the same object.

### 3b. Implement `Runnable`, pass to `Thread` (or an executor)

```java
class HelloTask implements Runnable {
    @Override
    public void run() {
        System.out.println("Running in: " + Thread.currentThread().getName());
    }
}

public class RunnableDemo {
    public static void main(String[] args) {
        Runnable task = new HelloTask();       // or a lambda: () -> System.out.println(...)
        new Thread(task).start();              // raw thread

        ExecutorService pool = Executors.newFixedThreadPool(2);
        pool.execute(task);                    // or hand the same task to a pool instead
        pool.shutdown();
    }
}
```
Same `task` object works whether it's driven by a raw `Thread` or a pool — that's the decoupling benefit in action.

### 3c. Implement `Callable<V>`, submit to an `ExecutorService`

```java
class SumTask implements Callable<Integer> {
    private final int n;
    SumTask(int n) { this.n = n; }

    @Override
    public Integer call() throws Exception {
        if (n < 0) throw new IllegalArgumentException("n must be >= 0"); // checked/unchecked both propagate
        int sum = 0;
        for (int i = 1; i <= n; i++) sum += i;
        return sum;
    }
}

public class CallableDemo {
    public static void main(String[] args) throws Exception {
        ExecutorService pool = Executors.newFixedThreadPool(2);
        Future<Integer> future = pool.submit(new SumTask(100));
        Integer result = future.get(); // blocks until done; 5050
        System.out.println(result);
        pool.shutdown();
    }
}
```
`Callable` can't be passed to `new Thread(...)` directly — it isn't a `Runnable`. Either go through an `ExecutorService.submit(...)`, or bridge it with `FutureTask` (below) if you specifically need a raw `Thread`.

### Bonus: `FutureTask` bridges Callable → Runnable

```java
Callable<Integer> callableTask = () -> 42;
FutureTask<Integer> futureTask = new FutureTask<>(callableTask); // implements RunnableFuture: Runnable + Future
new Thread(futureTask).start();  // now a raw Thread can run a Callable
int result = futureTask.get();   // 42
```

`submit(Runnable)` returns `Future<?>` (its `get()` just returns `null` on success). `submit(Callable<V>)` returns `Future<V>`.

`submit(Runnable)` returns `Future<?>` (its `get()` just returns `null` on success). `submit(Callable<V>)` returns `Future<V>`.

## 4. Gotchas that actually get asked

**`run()` vs `start()`** — calling `thread.run()` directly does **not** spawn a new thread. It's an ordinary method call executed synchronously on the *current* thread — no new call stack, no concurrency at all. Only `start()` creates a new OS-backed thread. Classic "spot the bug" question.

**Silent exception swallowing** — this is the one that bites in production, and it depends on *how* the task was handed to the executor:
- `executor.execute(runnable)` → an uncaught exception propagates to the thread's `UncaughtExceptionHandler`, which by default prints the stack trace to stderr. Visible.
- `executor.submit(runnable)` or `submit(callable)` → the exception is captured **inside the `Future`** instead of being thrown anywhere. Nothing is logged. It stays invisible until someone calls `future.get()`, which rethrows it wrapped in `ExecutionException`. If nobody ever calls `get()`, the failure silently disappears — a real, common bug pattern (batches of `submit()`ed tasks whose `Future`s are never checked).

**Checked exceptions in `Runnable`** — the `run()` signature has no `throws` clause, so any checked exception inside it must be caught and handled (or rethrown as unchecked) within the method body. `Callable.call()` is the escape hatch when you need checked exceptions to propagate naturally.

**`Future.get()` blocks indefinitely by default** — use `get(timeout, unit)` if you don't want an unresponsive task to hang the calling thread forever.

## 5. Self-check

Q: You `submit(callable)` where `call()` throws `IOException`. You call `future.get()` with no try/catch. What actually propagates out of `get()`, and what's inside it?

A: `ExecutionException` propagates (a checked exception `get()` itself declares) — call `getCause()` on it to retrieve the original `IOException`. The original checked exception is never thrown directly by `get()`; it's always wrapped.

## 6. Know cold (summary)

- `Runnable`: no return, no checked exceptions, fire-and-forget.
- `Callable<V>`: returns `V`, `call()` throws `Exception` — checked exceptions propagate (wrapped in `ExecutionException` via `Future.get()`).
- Prefer implementing `Runnable`/`Callable` over extending `Thread` — preserves inheritance, decouples task from execution strategy.
- `run()` called directly ≠ new thread. Only `start()` spawns one.
- `execute()` surfaces uncaught exceptions to the default handler; `submit()` swallows them into the `Future` until `get()` is called — check your `Future`s.
