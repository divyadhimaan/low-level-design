# Problem: Round Robin Print

You have N threads (numbered 0 to N-1) and a shared counter that must be printed from 1 up to some maxCount. The threads must print the counter values in strict round-robin order by thread id: thread 0 prints 1, thread 1 prints 2, thread 2 prints 3, ..., wrapping back to thread 0 for N+1, and so on, until maxCount is reached. Every thread must stop cleanly once maxCount is passed, regardless of how many "turns" it would otherwise get.

Example with 3 threads and maxCount 10:
T1: 1, T2: 2, T3: 3, T1: 4, T2: 5, T3: 6, T1: 7, T2: 8, T3: 9, T1: 10

Think through before coding:

How does a thread know it's "its turn"? What's the relationship between the shared counter and a thread's id?
Why do you need a `while` loop around the wait condition instead of a single `if` check?
How does every thread — not just the one that prints last — know when to stop, including threads whose turn never comes again?

## Solution: synchronized wait/notifyAll with a shared counter (RoundRobinPrint.java)

A single shared `current` counter (starting at 1) and one `lock` are shared by every thread. A thread's turn is determined by `(current - 1) % totalThreads == threadId` — i.e., counter value 1 belongs to thread 0, value 2 to thread 1, ..., value N belongs to thread N-1, then value N+1 wraps back to thread 0, and so on.

Each thread runs an infinite loop, taking the lock each iteration. Inside, it waits while two things both hold: `current` hasn't exceeded `maxCount` yet, and it isn't this thread's turn. Once one of those flips — either the count is exhausted or the turn arrives — the thread wakes and re-checks.

If `current > maxCount`, the thread calls `notifyAll()` (to make sure any other sleeping threads also wake up and see the same terminal condition) and returns, exiting its loop for good. Otherwise it must be this thread's turn, so it prints, increments `current`, calls `notifyAll()` to wake every other waiting thread so they can re-evaluate whose turn it now is, and loops back around to wait again.

A few details matter here:

The `while` loop around `wait()` (rather than `if`) is necessary for the usual reasons — spurious wakeups, and `notifyAll()` waking every thread rather than a specific one — so each thread must re-verify the condition itself before acting.

`notifyAll()` rather than `notify()` is required because up to N-1 threads can be parked on the same lock at once, each waiting for a different value of `current`. `notify()` gives no guarantee about which one wakes, so it could repeatedly wake the wrong thread while the one whose turn it actually is stays asleep. `notifyAll()` wakes everyone, each re-checks its own condition, and only the correct one proceeds while the rest go back to waiting.

The termination check (`current > maxCount`) is folded into the same wait condition as the turn check, so a thread that's parked waiting for its own turn will also wake up and exit once the counting is done, rather than waiting forever for a turn that will never come. The extra `notifyAll()` in the termination branch exists to propagate that wakeup onward to any other threads still parked, since the thread that pushed `current` past `maxCount` may already have exited its own wait, but others might still be sleeping.

`Demo.java` drives this with `totalThreads = 3` and `maxCount = 10`, starting the threads in reverse order (thread 2 first) specifically to show that start order doesn't matter — the shared counter and turn check enforce the print order regardless of which thread the OS happens to schedule first.
