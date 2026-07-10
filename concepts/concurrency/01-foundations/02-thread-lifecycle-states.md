# Thread Lifecycle & States

## 1. The six states (`Thread.State` enum)

```
NEW --start()--> RUNNABLE --run() returns/throws--> TERMINATED
                    |  ^
      synchronized  |  | monitor acquired
      contention    v  |
                 BLOCKED
                    ^
                    | (notify/notifyAll wakes thread here first,
                    |  must reacquire monitor before resuming)
                    |
       wait()/join()/park()   wait(ms)/join(ms)/sleep(ms)/parkNanos()
                    |                    |
                    v                    v
                WAITING           TIMED_WAITING
```

| State | Meaning | Entered via |
|---|---|---|
| `NEW` | Thread object created, not started | `new Thread(...)` |
| `RUNNABLE` | Eligible to run — covers **both** "actually on a core" and "ready, waiting for scheduler" (Java has no separate RUNNING state) | `start()` called |
| `BLOCKED` | Waiting to acquire an **intrinsic lock** (`synchronized`) held by another thread | Contending for a `synchronized` block/method |
| `WAITING` | Waiting indefinitely for another thread's action | `Object.wait()` (no timeout), `Thread.join()` (no timeout), `LockSupport.park()` |
| `TIMED_WAITING` | Waiting, bounded by a timeout | `Thread.sleep(ms)`, `Object.wait(ms)`, `Thread.join(ms)`, `LockSupport.parkNanos/parkUntil` |
| `TERMINATED` | `run()` completed (normally or via exception) | No way back — `start()` again throws `IllegalThreadStateException` |

**Important:** `BLOCKED` is specific to `synchronized` (intrinsic locks). `java.util.concurrent` locks like `ReentrantLock` use `LockSupport.park()` internally, so a thread contending for a `ReentrantLock` shows up as `WAITING`, not `BLOCKED`.

## 2. The gotchas that actually get asked

**`sleep()` vs `wait()`**
| | `Thread.sleep(ms)` | `Object.wait()` / `wait(ms)` |
|---|---|---|
| Releases held locks? | **No** — keeps all locks it holds | **Yes** — releases the monitor it's waiting on |
| Called from where? | Anywhere | Must be inside a `synchronized` block on that object, else `IllegalMonitorStateException` |
| Resulting state | `TIMED_WAITING` | `WAITING` (no arg) or `TIMED_WAITING` (with timeout) |
| Woken by | Timeout expiry / interrupt | `notify()`/`notifyAll()`, timeout, or interrupt |

**`notify()` doesn't move a thread straight to `RUNNABLE`.** It moves the waiting thread to `BLOCKED` first — it still has to reacquire the monitor lock before it can actually resume execution. Two-step transition, commonly missed.

**`yield()` is a scheduler hint, not a state transition.** The thread stays `RUNNABLE` throughout — there's no intermediate "ready" state it drops into. It just suggests the scheduler could let another same/higher-priority thread run.

**Spurious wakeups are real.** `wait()` can return without `notify()` ever being called (JLS allows it). Always guard with a `while` loop re-checking the condition, never an `if`:
```java
synchronized (lock) {
    while (!conditionMet) {
        lock.wait();
    }
    // proceed
}
```

**Interrupt behavior depends on state:**
- Thread in `WAITING`/`TIMED_WAITING` (or blocked via `lockInterruptibly()`) → `interrupt()` throws `InterruptedException` immediately, clears the interrupt flag.
- Thread in `RUNNABLE` → `interrupt()` just sets a flag; nothing happens unless the code explicitly polls `Thread.isInterrupted()` or `Thread.interrupted()`.

**Thread dumps (`jstack`) diagnose deadlocks via `BLOCKED` state.** Two threads each holding a `synchronized` lock the other wants will both show `BLOCKED` forever — that's the primary signal for a classic lock-order deadlock. (If they were deadlocked via `ReentrantLock.lock()` instead, they'd show `WAITING`, and `jstack` separately detects the cycle and prints "Found one Java-level deadlock" for both intrinsic and `Lock`-based cases.)

## 3. Self-check

Q: Two threads deadlock, each holding one `synchronized` lock and blocked waiting for the other's lock. What state does `jstack` report, and why not `WAITING`?

A: **`BLOCKED`** — they're not waiting on a condition variable (`wait()`), they're contending to *acquire* a monitor that's held by someone else. `BLOCKED` = fighting over a lock; `WAITING` = voluntarily gave up a lock waiting for a signal.

## 4. Know cold (summary)

- `RUNNABLE` in Java ≠ "currently on a CPU" — it means eligible to run; the OS scheduler decides actual execution.
- `sleep()` holds locks, `wait()` releases them. `wait()` must be inside `synchronized`.
- `notify()`/`notifyAll()` → woken thread goes to `BLOCKED` first (must reacquire monitor), not straight to `RUNNABLE`.
- `BLOCKED` = contending for a `synchronized` monitor. `WAITING`/`TIMED_WAITING` = voluntarily parked (`wait`, `join`, `park`, `sleep`) or contending for a `java.util.concurrent` `Lock`.
- Always loop on `wait()` conditions — spurious wakeups are allowed by spec.
- `yield()` doesn't change state; it's a scheduler hint, thread remains `RUNNABLE`.
