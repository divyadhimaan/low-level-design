# Java Streams Workbook — Answer Key

Check these **only after** attempting from a blank page. If you got one wrong, note *why* (which rule slipped) and redo it the next day, not immediately.

Assume `import java.util.*; import java.util.stream.*;` throughout.

---

## Level 1 — filter, map, collect

```java
// 1.1 even numbers
nums.stream().filter(n -> n % 2 == 0).collect(Collectors.toList());

// 1.2 squared
nums.stream().map(n -> n * n).collect(Collectors.toList());

// 1.3 word lengths
words.stream().map(String::length).collect(Collectors.toList());

// 1.4 words starting with a vowel
words.stream().filter(w -> "aeiou".indexOf(w.charAt(0)) >= 0).collect(Collectors.toList());

// 1.5 client of every order
orders.stream().map(Order::getClient).collect(Collectors.toList());

// 1.6 Eng employees' names
employees.stream().filter(e -> e.getDept().equals("Eng"))
    .map(Employee::getName).collect(Collectors.toList());

// 1.7 evens * 10
nums.stream().filter(n -> n % 2 == 0).map(n -> n * 10).collect(Collectors.toList());

// 1.8 order values
orders.stream().map(o -> o.getQuantity() * o.getPrice()).collect(Collectors.toList());
```

---

## Level 2 — terminal ops & finding

```java
// 2.1
words.stream().filter(w -> w.length() > 4).count();

// 2.2
employees.stream().anyMatch(e -> e.getSalary() > 130000);

// 2.3
orders.stream().allMatch(o -> o.getQuantity() > 0);

// 2.4
words.stream().filter(w -> w.startsWith("c")).findFirst().orElse("none");

// 2.5
employees.stream().max(Comparator.comparingInt(Employee::getSalary));

// 2.6
employees.stream().min(Comparator.comparingInt(Employee::getAge))
    .map(Employee::getName).orElse("none");

// 2.7
orders.stream().mapToInt(Order::getQuantity).sum();

// 2.8
employees.stream().mapToInt(Employee::getSalary).average().orElse(0.0);

// 2.9
words.stream().mapToInt(String::length).max().orElse(0);

// 2.10
nums.forEach(System.out::println);
```

---

## Level 3 — sorting, distinct, limit, skip

```java
// 3.1
nums.stream().sorted().collect(Collectors.toList());

// 3.2
nums.stream().sorted(Comparator.reverseOrder()).collect(Collectors.toList());

// 3.3
orders.stream().map(Order::getStock).distinct().collect(Collectors.toList());

// 3.4
words.stream().sorted(Comparator.comparingInt(String::length)).collect(Collectors.toList());

// 3.5
words.stream()
    .sorted(Comparator.comparingInt(String::length).thenComparing(Comparator.naturalOrder()))
    .collect(Collectors.toList());

// 3.6
employees.stream().sorted(Comparator.comparingInt(Employee::getSalary))
    .limit(2).map(Employee::getName).collect(Collectors.toList());

// 3.7
nums.stream().sorted().skip(3).collect(Collectors.toList());

// 3.8
employees.stream()
    .sorted(Comparator.comparing(Employee::getDept)
            .thenComparing(Comparator.comparingInt(Employee::getSalary).reversed()))
    .map(Employee::getName).collect(Collectors.toList());

// 3.9
nums.stream().sorted(Comparator.reverseOrder()).limit(3).collect(Collectors.toList());

// 3.10
words.stream().map(w -> w.charAt(0)).distinct().sorted().collect(Collectors.toList());
```

---

## Level 4 — grouping & aggregation

```java
// 4.1
employees.stream().collect(Collectors.groupingBy(Employee::getDept));

// 4.2
employees.stream().collect(Collectors.groupingBy(Employee::getDept, Collectors.counting()));

// 4.3
employees.stream().collect(Collectors.groupingBy(Employee::getDept,
    Collectors.averagingInt(Employee::getSalary)));

// 4.4
orders.stream().collect(Collectors.groupingBy(Order::getClient,
    Collectors.summingInt(Order::getQuantity)));

// 4.5
employees.stream().collect(Collectors.groupingBy(Employee::getDept,
    Collectors.collectingAndThen(
        Collectors.maxBy(Comparator.comparingInt(Employee::getSalary)),
        opt -> opt.map(Employee::getSalary).orElse(0))));

// 4.6
orders.stream().collect(Collectors.groupingBy(Order::getClient,
    Collectors.mapping(Order::getStock, Collectors.joining(","))));

// 4.7
employees.stream().collect(Collectors.groupingBy(Employee::getDept,
    Collectors.mapping(Employee::getName, Collectors.toList())));

// 4.8
employees.stream().collect(Collectors.partitioningBy(e -> e.getSalary() >= 100000));

// 4.9
orders.stream().collect(Collectors.groupingBy(Order::getClient,
    Collectors.collectingAndThen(
        Collectors.maxBy(Comparator.comparingInt(Order::getQuantity)),
        opt -> opt.map(Order::getQuantity).orElse(0))));

// 4.10
orders.stream().collect(Collectors.groupingBy(Order::getStock, Collectors.counting()));

// 4.11
employees.stream().collect(Collectors.groupingBy(Employee::getDept,
    Collectors.mapping(Employee::getName,
        Collectors.collectingAndThen(Collectors.toList(),
            list -> { Collections.sort(list); return String.join(",", list); }))));

// 4.12
words.stream().collect(Collectors.groupingBy(String::length));
```

---

## Level 5 — flatMap, Optional, toMap

```java
// 5.1
matrix.stream().flatMap(List::stream).collect(Collectors.toList());

// 5.2
matrix.stream().flatMap(List::stream).mapToInt(Integer::intValue).sum();
// or: matrix.stream().flatMapToInt(l -> l.stream().mapToInt(Integer::intValue)).sum();

// 5.3
sentences.stream().flatMap(s -> Arrays.stream(s.split(" "))).collect(Collectors.toList());

// 5.4
sentences.stream().flatMap(s -> Arrays.stream(s.split(" "))).distinct().count();

// 5.5
employees.stream().collect(Collectors.toMap(Employee::getName, Employee::getSalary));

// 5.6  duplicate stock keys → keep max price
orders.stream().collect(Collectors.toMap(Order::getStock, Order::getPrice, Double::max));

// 5.7
Arrays.stream(new String[]{}) ; // (illustrative)
employees.stream().map(Employee::getName)
    .max(Comparator.comparingInt(String::length)).orElse("none");

// 5.8
orders.stream().map(Order::getStock).distinct().sorted()
    .collect(Collectors.joining(", "));

// 5.9
matrix.stream().flatMap(List::stream).filter(n -> n % 2 == 0)
    .sorted().collect(Collectors.toList());

// 5.10  toMap building lists + merge
orders.stream().collect(Collectors.toMap(
    Order::getClient,
    o -> new ArrayList<>(List.of(o.getStock())),
    (a, b) -> { a.addAll(b); return a; }));
```

---

## Level 6 — advanced

```java
// 6.1
nums.stream().reduce(1, (a, b) -> a * b);

// 6.2
words.stream().reduce("", (a, b) -> a + b);

// 6.3
nums.stream().reduce(Integer::max);   // Optional<Integer>

// 6.4
employees.stream().collect(Collectors.groupingBy(Employee::getDept,
    Collectors.groupingBy(e -> e.getAge() > 35 ? "senior" : "junior")));

// 6.5
employees.stream().collect(Collectors.groupingBy(Employee::getDept,
    Collectors.summarizingInt(Employee::getSalary)));

// 6.6
employees.stream().collect(Collectors.groupingBy(Employee::getDept,
    Collectors.collectingAndThen(
        Collectors.summarizingInt(Employee::getSalary),
        s -> s.getCount() + " employees, avg " + (long) s.getAverage())));

// 6.7
orders.stream().collect(Collectors.groupingBy(Order::getClient,
    Collectors.summingDouble(o -> o.getQuantity() * o.getPrice())));

// 6.8  teeing min & max
nums.stream().collect(Collectors.teeing(
    Collectors.minBy(Comparator.naturalOrder()),
    Collectors.maxBy(Comparator.naturalOrder()),
    (min, max) -> "min=" + min.orElse(0) + ", max=" + max.orElse(0)));

// 6.9  two-level: client → stock → total qty
orders.stream().collect(Collectors.groupingBy(Order::getClient,
    Collectors.groupingBy(Order::getStock,
        Collectors.summingInt(Order::getQuantity))));

// 6.10  dept with highest avg salary
employees.stream()
    .collect(Collectors.groupingBy(Employee::getDept,
        Collectors.averagingInt(Employee::getSalary)))
    .entrySet().stream()
    .max(Map.Entry.comparingByValue())
    .map(Map.Entry::getKey).orElse("none");

// 6.11  count per (dept, decade)
employees.stream().collect(Collectors.groupingBy(Employee::getDept,
    Collectors.groupingBy(e -> (e.getAge() / 10) * 10 + "s",
        Collectors.counting())));

// 6.12  most valuable order description
orders.stream()
    .max(Comparator.comparingDouble(o -> o.getQuantity() * o.getPrice()))
    .map(o -> o.getClient() + " " + o.getStock() + " worth "
             + (o.getQuantity() * o.getPrice()))
    .orElse("none");
```

---

## Common mistakes to self-check against

- Put a collector inside `map()` instead of `collect()`. → Collectors always live in `collect()`.
- `max()`/`min()` given an identity arg (that's `reduce`). → They take **only** a comparator.
- Flipped `mapping` args → it's `mapping(extractFn, downstreamCollector)`.
- Flipped `collectingAndThen` args → it's `collectingAndThen(collector, finisher)`.
- Forgot `toMap` merge function when keys can collide → runtime `IllegalStateException`.
- Used `map` where the element expands to many → needed `flatMap`.
- Forgot `.orElse(...)` on an `Optional`-returning terminal (`findFirst`, `max`, `reduce` one-arg).
