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

### Medium Problems
- [Design a LRU Cache](./problems/LRU-based-cache.md)
- [Design a PubSub Model](./problems/pub-sub-model.md)
- [Design a Tic Tac Toe Game](./problems/tic-tac-toe-game.md)
- [Design a Library Management System](./problems/library-management-system.md)

### Hard Problems
- [Design an Elevator System](./problems/elevator-system.md)
- [Design a Conference Room Booking System](./problems/conference-room-booking.md)
- [Design Splitwise](./problems/splitwise.md)
- [Design a Movie Ticket Booking System](./problems/movie-ticket-booking-system.md)
- [Design Music Streaming Service](./problems/spotify.md)



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
- LLD and Concurrency Solution (80+ blogs) [LLDcoding](https://lldcoding.com/)

> NOTE: This repository was created during my learning journey in Low-Level Design. If you notice any improvements or corrections, feel free to reach out.
