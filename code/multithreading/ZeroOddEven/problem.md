# Problem: Print Zero-Odd-Even

You have one object with three methods, each called by its own thread in a loop: `zero()` prints `0` and is called `n` times, `even()` prints even numbers, `odd()` prints odd numbers.

The output must be `0102030405...` up to `n` — a `0` before every number, with odds and evens appearing in order. For `n = 5`: `0102030405`, read as `0,1,0,2,0,3,0,4,0,5`.

The twist that makes this harder than round-robin: it isn't a simple cycle of one thread after another. The `zero` thread runs every other turn (before each number), and control alternates between `odd` and `even` in between. So the turn sequence is `zero → odd → zero → even → zero → odd → ...` — three threads, but not an equal rotation, since zero fires twice as often as either of the other two.

Think through before coding:

What state do you need to track? Not just "whose turn" — you need to know whether it's zero's turn, and if not, whether the next number is odd or even.

This is where per-thread signaling shines — three separate `Semaphore`s or `Condition`s, each thread waiting on its own, each releasing the next.

Why is a `Semaphore` particularly clean here? Think about initial permit counts — which thread should be allowed to go first?

## Solution: ReentrantLock with three Conditions (ZeroOddEven.java)

The state is two variables guarded by one `ReentrantLock`: `curr`, the next number to print, and `zeroTurn`, a boolean that says whose "half" of the cycle it is. Rather than three semaphores, this solution uses three `Condition`s off the same lock — `zeroCondition`, `oddCondition`, `evenCondition` — one per thread, so each thread can be parked and woken individually, the same idea the hint describes for semaphores.

`zeroTurn` answers the first half of "whose turn" — is it zero's turn or not. When it isn't zero's turn, `curr % 2` answers the second half — is the pending number odd or even, which tells you which of the other two threads should act. This is exactly the two-part state the hint calls out: not just a single turn counter, but a turn flag plus a parity check.

`printZero()` waits while there's still work left (`curr <= max`) and it isn't zero's turn. Once woken, if `curr > max` the run is over and it returns; otherwise it prints `0`, flips `zeroTurn` to false to hand off control, and signals exactly one of `oddCondition` or `evenCondition` based on `curr % 2` — whichever thread is due to print the next number.

`printOdd()` and `printEven()` mirror each other: each waits while it isn't yet time for it specifically — meaning either it's still zero's turn, or `curr`'s parity doesn't match its own role. Once its condition is met, it prints `curr`, increments it, sets `zeroTurn` back to true, and signals `zeroCondition`, since zero always goes next regardless of which of odd/even just ran.

Because each thread parks on its own dedicated `Condition`, every steady-state signal is targeted — `signal()`, not `signalAll()` — waking exactly the one thread whose turn is next, never the wrong one. This is the same benefit the hint attributes to per-thread semaphores with staggered initial permits (zero starting with a permit, the other two starting empty so they block until released): each thread's wakeup channel is private, so there's no need to wake everyone and let them fight over re-checking a shared condition, the way `notifyAll()` on a single monitor would require.

The one place this breaks down is shutdown. When `curr` finally exceeds `max`, only one thread naturally gets signaled next by the normal handoff — but at that point another thread may already be sitting in `await()` on a condition nobody plans to signal again. To avoid leaving it stuck forever, each method's termination branch explicitly signals the *other two* conditions before returning, so every thread eventually wakes, re-checks `curr > max`, and exits its own loop. This differs from the steady-state path, where waking exactly one specific thread is enough.
