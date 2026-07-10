r# Race Conditions & Critical Sections

Where the guarantees from [06](./06-visibility-atomicity-ordering.md) turn into an actual debugging skill: finding the shared mutable state, and sizing the lock to the invariant, not to a single field.

## 1. Race condition, precisely

A race condition is when program correctness depends on the timing/interleaving of threads touching shared mutable state — outcome becomes nondeterministic, varies run to run based on scheduling.

**Sharper term — data race:** the JLS/JMM-specific definition. Two threads access the same variable concurrently, at least one a write, with no happens-before edge between them. A race condition is the broader concept: you can have a *logical* race condition (wrong outcome due to timing) with **zero technical data races**, if every individual access is properly synchronized but the locking is sized wrong (see the `transfer()` example below). This distinction is exactly what "critical section sizing" is about.

## 2. Two shapes of the bug

**Read-modify-write** — `count++`. Read, add, write are three separate steps; any interleaving between them loses an update. Covered in depth in [06](./06-visibility-atomicity-ordering.md).

**Check-then-act (TOCTOU)** — `if (!map.containsKey(k)) map.put(k, v)`. Two threads can both pass the check before either does the act. TOCTOU = "time-of-check to time-of-use" — the same term shows up in security contexts (file system races), same underlying shape: a gap between deciding and acting that another thread can slip into.

## 3. Finding the shared mutable state

Ask, for any class: which fields can two threads see and mutate *simultaneously*? Shared: instance fields on a heap object accessible from multiple threads, static fields, elements of a shared collection. Not shared: local variables (stack), method parameters passed by value for primitives, `ThreadLocal` values. That's your race surface — everything else is safe by construction.

## 4. Sizing the critical section — to the invariant, not the field

Trivial case:
```java
class Counter {
    private int count = 0;
    public void increment() { count++; } // critical section = this whole statement
}
```
One field, one statement — easy. The harder, more interview-relevant case is when the invariant spans multiple fields or multiple method calls:

```java
class Account {
    private double balance;
    void withdraw(double amt) { balance -= amt; }
    void deposit(double amt) { balance += amt; }
}

void transfer(Account from, Account to, double amt) {
    from.withdraw(amt);   // <- window here
    to.deposit(amt);
}
```
Even if `withdraw()` and `deposit()` are each individually `synchronized` (so, individually, zero data races), `transfer()` is still logically racy — another thread can observe a moment where money has left `from` but hasn't arrived at `to`, breaking the "total system balance conserved" invariant. The critical section here isn't either individual method call — it's the whole `transfer()` operation, because that's the actual invariant being protected. Making each piece individually thread-safe does not make *compositions* of those pieces thread-safe.

Same root cause as the classic `Vector`/`Collections.synchronizedList` gotcha: `get`/`add` are each synchronized, but `if (!list.contains(x)) list.add(x)` is still racy, because the invariant ("don't add duplicates") spans two separate synchronized calls with a gap between them.

## 5. Two practical gotchas

**Lock consistency** — every thread touching the shared state must synchronize on the **same** lock object, or mutual exclusion doesn't actually hold. Method A synchronized on `this`, method B synchronized on a separate private lock object → zero real protection between A and B.

**The fake fix** — `synchronized(new Object())`. A fresh lock object per invocation means every call gets its own unshared lock. Compiles, runs, provides no exclusion whatsoever. Easy to miss in review because it *looks* like proper synchronization.

## 6. Why not just lock everything

Over-locking — wrapping a whole method including expensive I/O or computation that never touches shared state — serializes execution for zero correctness benefit and kills throughput. The actual skill: find the **smallest contiguous region** that (a) touches the shared mutable state and (b) preserves the invariant end-to-end. Not smaller (misses part of the invariant, still buggy) and not larger (kills concurrency for nothing).

## 7. Self-check

Q: `ConcurrentHashMap<String, Integer> cache` with individually thread-safe `get`/`put`. Code does:
```java
if (cache.get(key) == null) {
    cache.put(key, computeExpensiveValue());
}
```
Is this thread-safe? Why or why not, and what's the fix?

A: **Not thread-safe**, despite `get`/`put` each being individually atomic on `ConcurrentHashMap`. This is a check-then-act compound action — two threads can both call `get(key)`, both see `null`, and both proceed to call `computeExpensiveValue()` and `put()`. The map itself won't get structurally corrupted, but the logical invariant ("compute this value at most once per key") is violated — duplicate, wasted computation, and one result silently overwrites the other.

Fix: use the atomic compound method the class provides instead of hand-rolling check-then-act:
```java
cache.computeIfAbsent(key, k -> computeExpensiveValue());
```
`computeIfAbsent` performs the check-and-insert atomically per key — no gap for another thread to slip into.

## 8. Know cold (summary)

- Race condition = outcome depends on thread timing. Data race = the JMM-specific case of unsynchronized concurrent access with at least one write. You can have the former without the latter.
- Two shapes: read-modify-write (`count++`) and check-then-act (`if absent, then insert`).
- Shared mutable state = heap-visible instance/static fields and shared collection contents. Stack locals and `ThreadLocal` are safe by construction.
- Size the critical section to the **invariant**, not to a single field — compositions of individually-thread-safe calls are not automatically thread-safe together.
- All threads must lock on the *same* object; a fresh lock per call (`synchronized(new Object())`) is a no-op disguised as a fix.
- Prefer atomic compound operations the library already provides (`computeIfAbsent`, `putIfAbsent`) over hand-rolled check-then-act.
