# The Three Guarantees — Visibility · Atomicity · Ordering

The lens for the rest of concurrency. Every **safety** bug (races, stale reads, corrupted or partially-visible state) is a violation of one of these three. (Deadlock/livelock/starvation are a separate axis — *liveness*, not safety — covered elsewhere.)

- **Visibility** — will thread B see thread A's write?
- **Atomicity** — does the operation appear indivisible to other threads?
- **Ordering** — is action A guaranteed to happen-before action B, from every observing thread's perspective?

## 1. Atomicity

An operation is atomic if no other thread can observe it "half done." Simple reads/writes of most primitives are atomic — compound operations are not.

`count++` looks like one instruction; it's actually three: read, increment, write. Two threads can interleave between those steps and lose an update — no second core required (see [01](01-process-vs-thread-concurrency-vs-parallelism.md), preemption alone is enough). Check-then-act (`if (!map.containsKey(k)) map.put(k, v)`) has the same disease.

**Subtlety:** `long`/`double` reads/writes are **not guaranteed atomic** by the JLS unless `volatile` — a 64-bit value could theoretically be written as two 32-bit half-writes ("word tearing"), letting a reader see half old / half new. Rare in practice on modern 64-bit JVMs, but only spec-guaranteed atomic with `volatile`.

**Tools:** `synchronized` (mutual exclusion makes the block indivisible relative to other threads on the same lock), `java.util.concurrent.atomic.*` (CAS-based, no locking), `Lock` implementations.

## 2. Visibility

Even an atomic write might never be seen by another thread. CPUs cache values locally; without a memory barrier, a write can sit in a core's cache/store buffer while another core keeps reading a stale copy. The JMM only guarantees B sees A's write if there's a **happens-before** edge between them.

**Classic bug:** a plain (non-volatile) `boolean running = true`, one thread sets it `false` to signal shutdown, another spins on `while (running) { }`. Without `volatile`, the compiler may legally cache the read in a register or hoist it out of the loop entirely (it assumes nothing else touches the variable) — producing a loop that never terminates even though the variable was logically changed.

```java
class Worker {
    private boolean running = true;         // BUG: not volatile
    void stop() { running = false; }
    void run() {
        while (running) { /* work */ }      // may loop forever — read can be cached/hoisted
    }
}
```

**Tools:** `volatile` (every write flushes to main memory, every read comes from main memory), `synchronized` (unlock happens-before the next lock of the same monitor — everything written before release becomes visible after acquire), safely-published `final` fields.

## 3. Ordering

The JVM/CPU may reorder instructions for optimization as long as it doesn't change what a *single thread* observes about itself ("as-if-serial" semantics). Invisible within one thread; very visible to another thread watching shared state without synchronization.

**Textbook example — broken double-checked locking:**
```java
class Singleton {
    private static Singleton instance;              // BUG: not volatile
    static Singleton getInstance() {
        if (instance == null) {
            synchronized (Singleton.class) {
                if (instance == null) {
                    instance = new Singleton();      // looks atomic, isn't
                }
            }
        }
        return instance;
    }
}
```
`instance = new Singleton()` is really three steps: allocate memory, run the constructor, assign the reference. Without `volatile`, the JVM may reorder the constructor call and the assignment — another thread could see `instance != null` while the object hasn't finished constructing. Declaring `instance` as `volatile` (requires the JSR-133 / Java 5+ memory model) blocks that specific reordering. This is the canonical ordering bug in interviews.

## 4. Happens-before — the formal glue

- Program order within a single thread.
- A monitor unlock happens-before the next lock of that same monitor.
- A `volatile` write happens-before every subsequent `volatile` read of that variable.
- `Thread.start()` happens-before any action in the started thread.
- All actions in a thread happen-before another thread's successful return from `join()` on it.
- Transitive: A hb B and B hb C ⟹ A hb C.

If A happens-before B, A's writes are guaranteed visible to B, and A is guaranteed ordered before B. Without a happens-before edge, the JMM makes **no guarantee whatsoever** about visibility or order — not "probably fine," genuinely undefined.

## 5. Which mechanism gives which guarantee

| Mechanism | Atomicity | Visibility | Ordering |
|---|---|---|---|
| `volatile` | No (not for compound ops like `x++`) | Yes | Yes (for that variable) |
| `synchronized` | Yes (within the block) | Yes | Yes (can't reorder across the boundary) |
| `Atomic*` classes | Yes (via CAS) | Yes | Yes (for that variable/op) |
| Safely-published `final` | N/A | Yes | Yes |

## 6. Mapping classic bugs to the three guarantees

| Bug | Guarantee violated |
|---|---|
| Lost update from `count++` race | Atomicity |
| Reader never sees a flag flip, loops forever | Visibility |
| Reader sees a partially-constructed object (broken double-checked locking) | Ordering (and visibility — the two are deeply linked via happens-before) |
| Deadlock | **Neither** — a liveness bug, not a safety bug; threads are blocked, not looking at wrong values |

## 7. Self-check

Q: `volatile int counter;` with multiple threads calling `counter++`. Visibility is guaranteed — does that make the final count correct? Why or why not?

A: **No.** `volatile` guarantees every thread sees the latest value and prevents reordering around that variable — but it does nothing for atomicity. `counter++` is still read-increment-write as three separate steps; two threads can each read the same value before either writes back, and one update is lost. `volatile` fixes visibility and ordering, not atomicity — you'd need `AtomicInteger` or `synchronized` here instead.

## 8. Know cold (summary)

- Three safety guarantees: atomicity (indivisible), visibility (will the write be seen), ordering (is A guaranteed before B).
- `count++` is not atomic — it's read-modify-write, three separate steps, racy even on a single core.
- `volatile` = visibility + ordering, NOT atomicity. Don't reach for `volatile` to fix a compound-operation race.
- `synchronized` gives all three within its block, at the cost of mutual exclusion / blocking.
- Happens-before is the formal mechanism underlying both visibility and ordering — no happens-before edge means no guarantee at all, not just "unlikely."
- Deadlock/livelock/starvation are liveness bugs, a separate category from these three safety guarantees.
