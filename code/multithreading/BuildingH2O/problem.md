# Problem: Building H2O

You're given a stream of threads, each representing either a hydrogen atom or an oxygen atom — for example `"OOHHHH"`. Each hydrogen thread calls `hydrogen(releaseHydrogen)` and each oxygen thread calls `oxygen(releaseOxygen)`, where the callback just prints the atom's letter. The threads arrive in arbitrary order and interleave arbitrarily, but water only forms from exactly two hydrogen atoms and one oxygen atom at a time — so the output must always group into valid H2O molecules, never emitting an "extra" H or O outside a complete triplet, and never blocking forever waiting on the wrong ratio.

Think through before coding:

What limits how many hydrogen atoms are allowed to proceed toward "forming a molecule" at once, versus how many oxygen atoms? What data structure caps a count like that?

The two counters need to reflect the 2:1 ratio of hydrogen to oxygen in water. Why does it matter what each one's *initial* value is — what goes wrong if one of them starts wrong?

Why use a barrier (something that only releases once exactly N parties have arrived) instead of, say, counting atoms manually with a lock? What does a barrier being *cyclic* — reusable across repeated trips — buy you here, given that molecules form repeatedly rather than once?

## Solution: two semaphores + a CyclicBarrier (BuildingH2O.java)

`hydrogenSemaphore` and `oxygenSemaphore` cap how many atoms of each kind are allowed "in flight" toward the current molecule at once. Their initial permit counts encode the 2:1 stoichiometry of water directly: `hydrogenSemaphore` starts with 2 permits (two hydrogen atoms may proceed at a time), `oxygenSemaphore` starts with 1 (one oxygen atom at a time). Every atom acquires its respective semaphore before doing anything else, which is what prevents, say, three hydrogen atoms from racing ahead into the same molecule.

Once past its semaphore, each atom calls `cyclicBarrier.await()` on a shared `CyclicBarrier(3, action)`. The barrier only releases all waiting threads once exactly 3 have arrived — which, thanks to the semaphore caps, can only ever be 2 hydrogens and 1 oxygen. The barrier's action (incrementing `moleculeCount` and printing `"[molecule #n formed]"`) runs exactly once per trip, executed by whichever thread happens to be the one that completes the triplet — `CyclicBarrier` guarantees the action runs once per cycle, not once per thread. Being *cyclic* means the barrier automatically resets after each trip of 3, so the same barrier instance handles every molecule in the stream, not just the first one.

After the barrier releases, each atom prints its letter, then the `finally` block releases its semaphore — handing that permit back so a new hydrogen or oxygen atom can start queuing up for the *next* molecule. This release step is what makes the whole thing repeatable across the full input rather than a one-shot event.

### The bug in the original version, and the fix

The version this was built from initialized `oxygenSemaphore` to `new Semaphore(0)` instead of `new Semaphore(1)`. That's a deadlock: with 0 initial permits and nothing else ever calling `release()` on it — the only `release()` is inside `oxygen()`'s own `finally` block, which only runs *after* a successful `acquire()` — the very first oxygen thread blocks forever on `acquire()` and never reaches the barrier. Since the barrier needs 3 parties and oxygen never shows up, the hydrogen threads that did get past their semaphore then block forever at `cyclicBarrier.await()` too. The whole program hangs, and `Demo.java`'s `t.join()` calls never return.

This was confirmed by simulating the identical semaphore/barrier logic (Python's `threading.Semaphore` and `threading.Barrier` have the same blocking semantics as Java's) — every oxygen thread timed out on acquire, and every hydrogen thread timed out waiting at the barrier, with zero molecules ever formed.

The fix is the one-line change already applied here: `oxygenSemaphore = new Semaphore(1)`. Re-running the same simulation with that fix, across several shuffles of atom arrival order (`"OOHHHH"`, `"HHHHOO"`, `"HOHOHH"`, `"OHOHHH"`), consistently produces exactly 2 molecules with no timeouts, regardless of the order threads happen to be scheduled in — confirming the semaphore counts, not just the barrier, are what make the ratio correct.
