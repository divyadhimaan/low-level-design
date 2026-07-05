# Process vs Thread · Concurrency vs Parallelism

## 1. Process vs Thread

| | Process | Thread |
|---|---|---|
| Definition | Independent running instance of a program (e.g. one JVM) | Unit of execution within a process |
| Address space | Own virtual address space | Shares the process's address space |
| Heap | Own heap | **Shared** heap (all threads see the same objects) |
| Method area / metaspace | Own | **Shared** (class metadata, static fields) |
| Stack | N/A (has one thread's worth by default) | **Own** stack per thread (locals, call frames) |
| PC register / registers | Own | Own |
| File descriptors / sockets | Own | **Shared** with sibling threads |
| Creation cost | Expensive — OS allocates new address space + page tables | Cheap — reuses existing address space |
| Context switch cost | Expensive — MMU remaps page tables, TLB flush | Cheap — only registers/stack pointer/PC swap, no address space change |
| Communication | Explicit IPC (pipes, sockets, shared memory, files) | Implicit, via shared heap (fast but needs synchronization) |
| Fault isolation | Crash of one process doesn't affect others | Uncaught exception kills the thread, not the JVM — but memory corruption/OOM is process-wide since heap is shared |

**Java specifics:**
- Each Java process = one JVM instance.
- Pre-Java 21: `Thread` is a thin wrapper over a native OS thread — 1:1 mapping. Thread creation is relatively expensive (~1MB stack by default), which is why thread pools exist.
- Java 21 introduced **virtual threads** (Project Loom) — an M:N model where many virtual threads are scheduled onto a small pool of OS "carrier" threads. Cheap to create (thousands/millions feasible), good for I/O-bound workloads. Still cooperate through the same shared heap, so all the same race-condition rules apply.
- Daemon threads (`setDaemon(true)`) don't prevent JVM shutdown; non-daemon threads do.

**Why races exist, in one sentence:** two threads can hold a reference to the *same object* on the shared heap and mutate it without coordination — two processes can't do this by accident, only via deliberate shared memory/IPC.

## 2. Concurrency vs Parallelism

**Rob Pike's framing:** "Concurrency is about dealing with lots of things at once. Parallelism is about doing lots of things at once."

- **Concurrency** = a property of program *structure*. Tasks are decomposed so they can make progress independently and their execution can be interleaved. Does **not** require multiple cores.
- **Parallelism** = a property of *execution*. Tasks literally run at the same instant, which requires multiple cores/processors.

| | Concurrency | Parallelism |
|---|---|---|
| Concerned with | Structure / correctness of interleaving | Simultaneous execution / throughput |
| Needs multiple cores? | No | Yes |
| Single-core example | 10 threads time-sliced on 1 CPU (scheduler illusion of simultaneity) | Not possible |
| Multi-core example | Same 10 threads, now some genuinely overlap in time | Threads actually executing at the same instant on different cores |
| Can exist without the other? | Yes — concurrency without parallelism (single core) | Debatable — parallelism is usually built on top of a concurrent structure |

### Key insight for interviews
**Race conditions are a concurrency problem, not a parallelism problem.** You can get lost updates on a single-core machine with two threads: the OS can preempt a thread *between* the read and the write of a non-atomic operation (like `count++`, which is read-modify-write) and let another thread interleave. No second core required — just unsynchronized shared mutable state + unpredictable interleaving (preemption).

### Quick self-check
Q: Non-thread-safe `count++` with 2 threads, single-core machine — do you still expect lost updates?
A: **Yes.** Preemption mid-operation causes the same lost-update race as on multi-core; parallelism isn't the root cause, non-atomicity + shared state is.

## 3. Rapid-fire interview Q&A

> **Q: What's shared between threads in the same JVM, and what isn't?**
> 
> A: Shared: heap, metaspace/method area (static fields, class metadata), open file descriptors/sockets. Not shared: stack, PC register, `ThreadLocal` values.

> **Q: Why is a thread called a "lightweight process"?**
> 
> A: It has its own execution context (stack, PC, registers) like a process, but reuses the parent process's address space instead of getting its own — so creation and context-switching are cheaper.

> **Q: Why is a thread context switch cheaper than a process context switch?**
>
> A: No address space change — the MMU doesn't need to remap page tables or flush the TLB. Only the stack pointer, PC, and register set need to be swapped.

> **Q: Can you have concurrency without parallelism? Give an example.**
>
> A: Yes — single-core CPU running a multitasking OS; threads are interleaved via time-slicing but never literally simultaneous.

> **Q: Does more threads always mean more throughput?**
>
> A: No — bounded by number of cores, contention on shared resources (locks, memory bandwidth), and Amdahl's Law (speedup is capped by the serial portion of the work that can't be parallelized).

> **Q: If a thread throws an uncaught exception, does the JVM crash?**
> 
> A: No, only that thread dies (its default `UncaughtExceptionHandler` logs it) — unless it was the last non-daemon thread keeping the JVM alive. Contrast with heap corruption/OOM, which is process-wide since the heap is shared.

## 4. Common misconceptions

- "Parallel" and "concurrent" are **not** interchangeable — concurrency is about design/structure, parallelism is about actual simultaneous hardware execution.
- Multithreading isn't automatically "faster" — on a single core it can be *slower* than sequential code due to context-switch overhead, with the benefit being responsiveness (e.g., not blocking on I/O), not throughput.
- Shared heap doesn't mean "shared by default is safe" — it means every unsynchronized access to mutable shared state is a potential race, which is the entire motivation for the synchronization primitives (locks, `volatile`, atomics) coming up next.

## 5. Know cold (summary)

- Threads share heap + metaspace + fds; each has its own stack + PC + registers.
- Thread creation/context-switch is cheap because no address-space change; process creation/switch is expensive because the MMU has to remap.
- Concurrency = structure (interleaving-capable design). Parallelism = execution (literal simultaneity, needs multiple cores).
- Concurrency without parallelism is possible (single core, time-sliced). Race conditions can happen with just concurrency — no parallelism needed.
