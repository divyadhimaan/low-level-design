# Low-Level Design: Concepts, Code Examples, and Design Examples

This repository serves as a comprehensive guide to Low-Level Design (LLD), aimed at helping developers understand and implement essential design concepts in software development. It contains a collection of:

- Core Concepts: Key principles and techniques in LLD such as SOLID principles, object-oriented design, and common design patterns.
- Code Examples: Practical Java code implementations showcasing various design techniques, including dependency injection, design pattern usage, and object modeling.
- Design Examples: Step-by-step breakdowns of real-world design problems, with detailed explanations of how to design and architect scalable, maintainable software systems.

- Whether you're preparing for interviews, aiming to improve your design skills, or looking to see best practices in action, this repository provides a solid foundation for mastering Low-Level Design.


## Concepts
- [Basic OOP](./concepts/oops/java.md#basic-oop)
  - [Classes in Java](./concepts/oops/java.md#classes-in-java)
  - [Scope](./concepts/oops/java.md#scope)
  - [UML](./concepts/intro/uml.md)
  - [Main OOP Concepts in Java](./concepts/oops/java.md#main-oop-concepts-in-java)
    - [Encapsulation](./concepts/oops/java.md#encapsulation)
    - [Abstraction](./concepts/oops/java.md#abstraction)
    - [Inheritance](./concepts/oops/java.md#inheritance)
    - [Polymorphism](./concepts/oops/java.md#polymorphism-)
  - [Composition](./concepts/oops/java.md#composition)
  - [Collections](./concepts/oops/java.md#collections)
  - [Generics](./concepts/oops/java.md#generics)
  - [Concurrency](./concepts/concurrency.md)
  - [Java Glossary](concepts/java/Glossary.md)
  - [Stream API](./concepts/java/java-stream-practice.md)
- [Springboot](./concepts/spring-boot/sb_overview.md)
  - [Dependency Inversion - IOC](./concepts/spring-boot/di-ioc.md)
  

## SOLID Principles
SOLID is an acronym for five fundamental principles of object-oriented programming and design that help create more maintainable, flexible, and robust software.
- `S` - [Single Responsibility Principle](concepts/solid-principles/single-responsibility-principle.md)
- `O` - [Open Closed Principle](concepts/solid-principles/open-closed-principle.md)
- `L` - [Liskov Substitution Principle](concepts/solid-principles/liskov-substitution-principle.md)
- `I` - [Interface Segmented Principle](concepts/solid-principles/interface-segmented-principle.md)
- `D` - [Dependency Inversion Principle](concepts/solid-principles/dependency-Inversion-principle.md)



## Design Patterns
- Design patterns are reusable solutions to commonly occurring problems in software design. 
- They represent best practices and proven approaches that experienced developers have identified and documented over time.
- Categories of design patterns include Creational, Structural, and Behavioral patterns.
  - `Creational Patterns`: Deal with object creation mechanisms (Singleton, Factory, Builder)
  - `Structural Patterns`: Deal with object composition and relationships (Adapter, Decorator, Facade)
  - `Behavioral Patterns`: Deal with communication between objects and assignment of responsibilities (Observer, strategy.Strategy, Command)
  

| Creational Patterns                                                | Structural Patterns                                | Behavioral Patterns                                                              |
|--------------------------------------------------------------------|----------------------------------------------------|----------------------------------------------------------------------------------|
| [Singleton](concepts/design-patterns/singleton.md)                 | [Adapter](concepts/design-patterns/adapter.md)     | [Chain of Responsibility](./concepts/design-patterns/chain-of-responsibility.md) |
| [Factory Method](./concepts/design-patterns/factory.md)            | [Bridge](./concepts/design-patterns/bridge.md)     | Command                                                                          |
| [Abstract Factory](./concepts/design-patterns/abstract-factory.md) | [Composite](concepts/design-patterns/composite.md) | Iterator                                                                         |
| [Builder](concepts/design-patterns/builder.md)                     | [Decorator](concepts/design-patterns/decorator.md) | Mediator                                                                         |
| Prototype                                                          | [Facade](concepts/design-patterns/facade.md)       | Memento                                                                          |
|                                                                    | Flyweight                                          | [Observer](concepts/design-patterns/observer.md)                                 |
|                                                                    | [Proxy](concepts/design-patterns/proxy.md)         | [State](./concepts/design-patterns/state.md)                                     |
|                                                                    |                                                    | [Strategy](concepts/design-patterns/strategy.md)                                 |
|                                                                    |                                                    | Template Method                                                                  |
|                                                                    |                                                    | Visitor                                                                          |
|                                                                    |                                                    | [Null Object](concepts/design-patterns/null-object.md)                           |

### Common Confusing Patterns

- [Builder vs Decorator](concepts/design-patterns/differences-among-design-patterns.md#builder-vs-decorator)
- [Facade vs Proxy](concepts/design-patterns/differences-among-design-patterns.md#facade-vs-proxy)
- [Facade vs Adapter](concepts/design-patterns/differences-among-design-patterns.md#facade-vs-adapter)

## Design Examples

### Easy Problems
- [Design a Parking Lot](./problems/parking-lot.md)
- [Design a weather PubSub System](./problems/weather-pub-sub.md)
- [Design a Bank Account System](./problems/bank-account-system.md)
- [Design Linkedin Verdict](./problems/linkedin-verdict.md)
- [Design a Coffee Vending Machine](./problems/coffee-vending-machine.md)
- [Design a Traffic Signal Control System](./problems/traffic-signal-control-system.md)
- [Design a Vending Machine](./problems/vending-machine.md)
- [Design a Logging framework](./problems/logging-system.md)
- [Design a Task Management System](./problems/task-management.md)

### Medium Problems
- [Design a LRU Cache](./problems/LRU-based-cache.md)
- [Design a PubSub Model](./problems/pub-sub-model.md)
- [Design a Tic Tac Toe Game](./problems/tic-tac-toe-game.md)
- [Design a Library Management System](./problems/library-management-system.md)
- [Design a Notification System](./problems/notification-service.md)

### Hard Problems
- [Design an Elevator System](./problems/elevator-system.md)
- [Design a Conference Room Booking System](./problems/conference-room-booking.md)
- [Design Splitwise](./problems/splitwise.md)
- [Design a Movie Ticket Booking System](./problems/movie-ticket-booking-system.md)
- [Design Music Streaming Service](./problems/spotify.md)

## Concurrency
Notes built topic-by-topic for deep concurrency prep (Java), each with mental models, Java specifics, interview gotchas, and a self-check.

0. [Glossary](./concepts/concurrency/00-glossary.md) — quick-reference definitions: Checked Exceptions, Unchecked Exceptions, Future, ExecutorService.

## Module 1 — Thread Fundamentals

1. [Process vs Thread · Concurrency vs Parallelism](concepts/concurrency/01-foundations/01-process-vs-thread-concurrency-vs-parallelism.md) — what's shared (heap, metaspace, fds) vs per-thread (stack, PC, registers); why thread context switches are cheaper than process ones; Rob Pike's concurrency-is-structure/parallelism-is-execution framing; races are a concurrency problem, not a parallelism problem.
2. [Thread Lifecycle & States](concepts/concurrency/01-foundations/02-thread-lifecycle-states.md) — `NEW`/`RUNNABLE`/`BLOCKED`/`WAITING`/`TIMED_WAITING`/`TERMINATED`; which calls cause which transitions; `sleep()` vs `wait()`; why `notify()` moves a thread to `BLOCKED` first, not `RUNNABLE`; interrupt semantics per state.
3. [Thread vs Runnable vs Callable](concepts/concurrency/01-foundations/03-thread-vs-runnable-vs-callable.md) — composition over inheritance for task creation; return values and checked exceptions; `run()` vs `start()`; silent exception swallowing with `submit()` vs `execute()`; code samples for all three creation approaches plus the `FutureTask` bridge.
    - [3a. Raw Thread vs ExecutorService](concepts/concurrency/01-foundations/03a-raw-thread-vs-executorservice.md) — thread reuse and bounded concurrency in a pool; the "forgot to `shutdown()`" JVM-hang gotcha; `shutdown()` vs `shutdownNow()` vs `awaitTermination()`.
    - [3b. Future vs ExecutorService.submit(...)](concepts/concurrency/01-foundations/03b-future-vs-executorservice-submit.md) — the three `submit()` overloads; `get()`/`get(timeout)`/`isDone()`/`cancel()`; cancellation is cooperative, not forced; why `CompletableFuture` exists; `Future` vs `FutureTask`.
4. [start vs run · join · sleep · yield · interrupt](concepts/concurrency/01-foundations/04-start-run-join-sleep-yield-interrupt.md) — `start()` vs directly calling `run()`; `join()`/`join(ms)` and why a timed join doesn't cancel the target; `sleep()` holds locks; `yield()` is an unreliable hint; cooperative cancellation via `interrupt()`/`isInterrupted()`/`Thread.interrupted()` and the restore-the-flag idiom.
5. [Daemon vs User Threads](concepts/concurrency/01-foundations/05-daemon-vs-user-threads.md) — JVM exits once zero non-daemon threads remain; daemon status is inherited from the creating thread at creation time; `setDaemon()` must be called before `start()`; why `ExecutorService`'s non-daemon workers are the reason forgetting `shutdown()` hangs the JVM.
6. [The Three Guarantees — Visibility · Atomicity · Ordering](concepts/concurrency/01-foundations/06-visibility-atomicity-ordering.md) — the lens for every safety bug from here on; why `count++` isn't atomic; why unsynchronized flags can loop forever (visibility); broken double-checked locking (ordering); happens-before as the formal glue; which mechanism (`volatile`/`synchronized`/`Atomic*`) gives which guarantee; deadlock as a liveness bug, not a safety violation.

## Module 2 — Safety & Core Synchronization

1. [Race Conditions & Critical Sections](concepts/concurrency/02-core-synchronization/11-race-conditions-critical-sections.md) — race condition vs the stricter "data race"; read-modify-write vs check-then-act (TOCTOU); finding the shared mutable state; sizing a critical section to the invariant, not a single field (the account-transfer example); lock-consistency and the `synchronized(new Object())` fake fix; preferring atomic compound ops like `computeIfAbsent` over hand-rolled check-then-act.

### Coding Problems
Classic multithreading interview problems,each with a `problem.md` (problem statement, think-through prompts, and a walkthrough of the solution) plus working Java implementations.

- [Bounded Blocking Queue (Producer-Consumer)](./code/multithreading/BlockingQueue/problem.md)
- [Print in Order](./code/multithreading/PrintInOrder/problem.md)
- [Round Robin Print](./code/multithreading/RoundRobinPrint/problem.md)
- [Print Zero-Odd-Even](./code/multithreading/ZeroOddEven/problem.md)
- [Print FooBar Alternately](./code/multithreading/FooBar/problem.md)
- [Dining Philosophers](./code/multithreading/DiningPhilosophers/problem.md)
- [Building H2O](./code/multithreading/BuildingH2O/problem.md)


## Java Streams Practice Problems
Small self-contained Java files for practicing streams, OOP, and threading basics — [streams practice](code/java/src/streams-practice).

- [Employee.java](code/java/src/streams-practice/Employee.java) — stream operations (filter, map, sort, group) over a list of employees.
- [JavaStreamPractice.java](code/java/src/streams-practice/JavaStreamPractice.java) — a set of numbered stream tasks (filter/map/collect, sort + limit, map/reduce, etc.).
- [Order.java](code/java/src/streams-practice/Order.java) — grouping/aggregating a list of orders by client and stock using streams.
- [Test.java](code/java/src/streams-practice/Test.java) — stream basics: filter/map/collect, groupingBy, reduce vs mapToInt/sum, counting.

> A progressive, six-level workbook (basic → advanced) for building Java Streams fluency through blank-page exercises — [streams workbook](./code/java/src/streams-practice/streams_workbook.md) ([answer key](./code/java/src/streams-practice/streams_workbook_answers.md)).

## Notations
- Is-A (Inheritance)
- Has-A (Composition/Aggregation)

| Syntax | Symbol | Meaning | Usage                        | Line Style                   |                 |
|--------|--------|---------|------------------------------|------------------------------|-----------------|
| \`<    | --\`   | Solid   | **Class inheritance**        | Concrete or abstract classes | **Solid line**  |
| \`<    | ..\`   | Dotted  | **Interface implementation** | Class implements interface   | **Dotted line** |

## Relationships

![UML Relations](images/uml-relations.png)

## Terminology

- `DAO` (Data Access Object): A design pattern that provides an abstract interface to a database or other persistence mechanism.


## Resources

- Design Patterns with [Refactoring Guru](https://refactoring.guru)

> NOTE: This repository was created during my learning journey in Low-Level Design. If you notice any improvements or corrections, feel free to reach out.