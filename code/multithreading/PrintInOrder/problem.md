# Problem: Print in Order

You have a single object with three methods: first(), second(), third(), each printing "first", "second", "third" respectively. Three different threads call these — but you have no control over which order the threads run. One thread calls first(), another calls second(), another calls third().
Guarantee the output is always "firstsecondthird", regardless of thread scheduling.
Example: even if the thread calling third() runs first, it must wait until first() and second() have completed.
Think through before coding:

What primitive lets one thread signal "I'm done" and another wait for that signal?
You have several options here — Semaphore, CountDownLatch, a synchronized block with wait/notify, or even a volatile flag with busy-waiting. Which is cleanest?
Why is a plain boolean flag (without volatile or synchronization) not safe here?

## Solution 1: CountDownLatch (PrintInOrder.java)

Use one `CountDownLatch(1)` per handoff — `firstDone` and `secondDone`. Each latch starts at count 1 and can only be counted down once, which maps naturally onto a one-shot "this step is complete" signal.

`first()` prints, then counts down `firstDone`. `second()` calls `firstDone.await()` before doing anything, so it blocks until `first()` has run, then prints and counts down `secondDone`. `third()` awaits `secondDone` the same way.

Each thread only ever waits on the one latch it cares about — there's no shared mutable state, no lock to hold, and no need to re-check a condition in a loop, because a `CountDownLatch` can't spuriously "un-fire" or fire early. Once `countDown()` happens, every current and future `await()` on that latch returns immediately.

## Solution 2: synchronized wait/notify with a state variable (PrintInOrder1.java)

A single `int state` (starting at 1) tracks which step is next, guarded by one `lock`. Each method takes the lock, spins in a `while` loop calling `lock.wait()` until `state` reaches the value it's waiting for, then does its print, advances `state`, and calls `notifyAll()` to wake everyone else up so they can re-check.

The `while` loop (rather than `if`) is essential: `wait()` can return for reasons other than the specific state change (spurious wakeups, or a `notifyAll()` meant for a different waiter), so each thread must re-verify the condition itself before proceeding. `notifyAll()` (rather than `notify()`) is used because multiple threads block on the same lock waiting on different state values — `notify()` might wake the wrong one, leaving the correct thread asleep.

## Weighing the two implementations

CountDownLatch is the better fit for this specific problem. Each handoff here is a one-time, one-directional signal ("step N is done"), which is exactly what `CountDownLatch` models — there's no shared state to reason about, no manual locking, and no risk of missing a signal or waking the wrong thread. The intent reads directly off the code: `firstDone.await()` says precisely what it waits for.

The wait/notify version is more general — it can express arbitrary state machines with more than a fixed sequence of one-shot steps, and it's the classic building block worth knowing since `CountDownLatch`, `Semaphore`, and similar utilities are built on the same `wait`/`notify`/monitor foundation. But for this problem it costs more: a shared `state` variable, a lock, a `while`-loop re-check for spurious/misdirected wakeups, and `notifyAll()` waking every thread (including ones that immediately go back to sleep) rather than only the one thread that needed the signal.

In short: reach for `CountDownLatch` (or similar higher-level utilities) when the coordination is a fixed sequence of one-time signals; reach for `wait`/`notify` when you need a reusable, arbitrary condition to gate on repeatedly.