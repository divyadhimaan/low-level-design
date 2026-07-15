# Problem: Print FooBar Alternately

You have one object shared by two threads. One thread calls `foo()`, the other calls `bar()`, each in a loop `n` times. The output must strictly alternate: `foobarfoobarfoobar...` — `foo` always printed before the `bar` that follows it, `n` times total for each.

Unlike Print-in-Order, this isn't a one-shot handoff — the two threads have to keep handing control back and forth for `n` rounds, so whatever signaling you use has to be reusable each iteration, not a single fire-once event.

Think through before coding:

What's the minimal shared state needed to know whose turn it is right now?

Could you solve this with two semaphores instead of a lock and a boolean — one representing "foo may go," one representing "bar may go"? What would the initial permit counts need to be, and why does it matter which one starts at 1 and which starts at 0?

Why does the semaphore version not need an explicit shared "turn" variable at all, the way the lock-based version does?

## Solution 1: synchronized wait/notifyAll with a boolean flag (FooBar.java)

A single `fooTurn` boolean, guarded by one `lock`, tracks whose turn it is — `true` means foo should go next, `false` means bar should. Each method loops `n` times; each iteration takes the lock, waits while it isn't its turn, prints, flips `fooTurn` to hand control to the other thread, and calls `notifyAll()` so the other thread (parked waiting on the same lock) wakes up and re-checks.

The `while` loop around `wait()` guards against spurious wakeups the usual way — a thread that wakes up must re-verify `fooTurn` is actually set the way it expects before proceeding, since a wakeup alone doesn't guarantee the condition holds. `notifyAll()` is used here even though there's only one other thread that could possibly care about the change, which makes it safe by default, though with only two threads total a plain `notify()` would also work since there's no ambiguity about which thread should wake.

## Solution 2: two semaphores, no shared turn variable (FooBarSem.java)

`fooSem` starts with 1 permit, `barSem` starts with 0. `foo()` acquires `fooSem`, prints, then releases `barSem`. `bar()` acquires `barSem`, prints, then releases `fooSem`. Because `fooSem` starts "unlocked" and `barSem` starts "locked," the very first `acquire()` call in `foo()` succeeds immediately while `bar()` blocks — that initial imbalance in permit counts *is* the turn-taking; there's no separate boolean anywhere.

Each iteration, whichever thread just printed releases the permit the *other* thread is blocked on, and immediately tries to acquire its own permit again for the next round — which isn't available yet, since the other thread hasn't released it. This naturally throttles both threads to alternate: a thread can never race ahead and print twice in a row, because its own semaphore only ever has 0 or 1 permits available, and the only way it gets replenished is the other thread finishing its turn.

No explicit lock or condition-check loop is needed because a semaphore's `acquire()` already blocks until a permit exists and atomically consumes it — the permit count itself is the state being waited on, so there's nothing extra to protect or re-verify the way `fooTurn` needs re-checking after `wait()`.

## Weighing the two implementations

The semaphore version is the better fit for this problem. It has less state to reason about (no boolean to protect, no lock to explicitly acquire/release, no loop re-checking a condition), and the alternation falls directly out of the initial permit counts rather than being enforced by hand — read `fooSem = new Semaphore(1)` and `barSem = new Semaphore(0)` and the turn order is self-evident. It also generalizes cleanly: nothing about it assumes exactly two threads the way a single boolean flag implicitly does.

The wait/notify version is more verbose for the same result, but it's the more broadly useful pattern to know, since it scales to cases where "whose turn" isn't a simple two-way flip (as seen in Zero-Odd-Even, where a plain semaphore pair wouldn't have been enough on its own). For a strict two-thread alternation like this one, though, the paired semaphores are simpler, shorter, and harder to get subtly wrong.
