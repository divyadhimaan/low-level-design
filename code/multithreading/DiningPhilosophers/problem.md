# Problem: Dining Philosophers

Five philosophers sit around a circular table with five forks, one between each adjacent pair. To eat, a philosopher needs both forks to their immediate left and right. Each philosopher repeatedly thinks, then wants to eat, which requires picking up both forks, eating, and putting both forks back down.

The classic failure mode: if every philosopher picks up their left fork at the same time, all five are left holding one fork each and waiting forever for the fork to their right — a circular wait with no way out. The implementation must guarantee no deadlock (and no starvation) no matter how the threads are scheduled.

Think through before coding:

Where does the circular wait actually come from — what specifically causes five threads to end up deadlocked in a cycle?

What are the standard ways to break that cycle? Limiting how many philosophers can attempt to eat at once, making one philosopher reach for forks in the opposite order from everyone else, or imposing a global ordering on the forks themselves so every thread acquires them the same way.

If you go with a global ordering, what has to be true about *every* philosopher's acquisition order for a cycle to become impossible?

## Solution: lock forks in a fixed global order (DiningPhilosophers.java)

Each fork is a `ReentrantLock`, five of them in an array. For philosopher `i`, the "left" fork is index `i` and the "right" fork is `(i + 1) % 5` — that's just the physical layout around the table, and it's what the logging callbacks (`pickLeftFork`, `pickRightFork`, etc.) describe.

But acquisition doesn't happen in left-then-right order — it happens in *index* order: `first = min(left, right)` is locked before `second = max(left, right)`. For philosophers 0 through 3, their left index is already smaller than their right index, so this happens to match physical left-then-right anyway. Philosopher 4 is the exception: its left is index 4 and its right is index 0, so `min`/`max` flips it — philosopher 4 locks fork 0 first, then fork 4. That one reversal is what breaks the symmetry that causes deadlock; without it, this would just be "everyone grabs left first," which is exactly the deadlocking case described above.

Once both locks are held, the callbacks run in their normal logical order (pick left, pick right, eat, put down left, put down right) purely for readable output — the actual mutual exclusion already happened via the two `lock()` calls, so the order of the print statements afterward doesn't affect correctness.

Why a fixed global order prevents the cycle: a deadlock needs a chain of philosophers each holding one fork and blocked waiting for another, wrapping all the way around back to the first. That requires at least one philosopher in the chain to be holding a higher-indexed fork while blocked waiting for a lower-indexed one. But every philosopher here always acquires the lower index first — a thread can only be blocked on its *second* `lock()` call, which by construction is always the higher index, while already holding the lower one. No thread ever holds a higher-indexed fork while waiting on a lower-indexed one, so the specific hand-off needed to close the cycle can never happen. Whichever fork has the globally lowest index among the five is never something a thread is left waiting for while it already holds something bigger — that's enough to make the circular wait structurally impossible, not just unlikely.

`try/finally` releases both forks (in reverse order, second then first) even if `eat.run()` or another callback throws, so a philosopher can't leave a fork locked forever due to an exception mid-meal.

This is one of a few standard fixes for this problem — capping the number of philosophers allowed to attempt eating at once (e.g., a `Semaphore(4)`, since with only 4 of 5 seated at least one fork is always free) or having a single "odd one out" philosopher reach right-before-left are the other common approaches. This solution's version of that idea is the global fork ordering: instead of special-casing one philosopher's *behavior*, it special-cases one philosopher's *lock order*, which has the same deadlock-breaking effect with a slightly more general (and arguably more scalable) rule: "always acquire the lower-indexed resource first," which works no matter how many philosophers are at the table.
