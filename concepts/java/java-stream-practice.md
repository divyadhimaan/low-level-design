# Java Streams Practice Challenges

A comprehensive set of hands-on challenges to master the Java Stream API. Each challenge includes sample data, expected output, hints, and the full solution.

---

## How to use this guide

1. Read the challenge description and sample data.
2. Try writing the stream expression yourself before looking at the hint or solution.
3. Each challenge targets specific stream operations — study the **concepts** listed under each one.

---

## Level 1 — Foundations

### 1. Filter + map — double the evens

**Task:** Given a list of integers, filter out odd numbers, double the evens, and collect to a list.

```java
List<Integer> nums = List.of(1, 2, 3, 4, 5, 6, 7, 8);
```

**Expected output:** `[4, 8, 12, 16]`

**Concepts:** `filter`, `map`, `collect`

**Hint:** Chain `.filter(n -> n % 2 == 0)` before `.map(n -> n * 2)`.

**Solution:**
```java
List<Integer> result = nums.stream()
    .filter(n -> n % 2 == 0)
    .map(n -> n * 2)
    .collect(Collectors.toList());
```

---

### 2. Sort + limit — top 3 longest words

**Task:** Given a list of strings, sort by length descending and return only the top 3.

```java
List<String> words = List.of("apple", "kiwi", "strawberry", "fig", "blueberry", "pear");
```

**Expected output:** `[strawberry, blueberry, apple]`

**Concepts:** `sorted`, `Comparator`, `limit`

**Hint:** Use `Comparator.comparingInt(String::length).reversed()` inside `.sorted()`.

**Solution:**
```java
List<String> result = words.stream()
    .sorted(Comparator.comparingInt(String::length).reversed())
    .limit(3)
    .collect(Collectors.toList());
```

---

### 3. Reduce — sum of squares

**Task:** Square each number in the list, then reduce to a single sum.

```java
List<Integer> nums = List.of(1, 2, 3, 4, 5);
```

**Expected output:** `55`

**Concepts:** `map`, `reduce`

**Hint:** Map each element to `n * n`, then use `reduce(0, Integer::sum)`.

**Solution:**
```java
int result = nums.stream()
    .map(n -> n * n)
    .reduce(0, Integer::sum);
```

---

### 4. FlatMap — flatten nested lists

**Task:** Given a list of lists, flatten them into a single stream and collect unique values, sorted ascending.

```java
List<List<Integer>> nested = List.of(
    List.of(1, 2, 3),
    List.of(2, 3, 4),
    List.of(3, 4, 5)
);
```

**Expected output:** `[1, 2, 3, 4, 5]`

**Concepts:** `flatMap`, `distinct`, `sorted`

**Hint:** Use `.flatMap(Collection::stream)` to unwrap each inner list.

**Solution:**
```java
List<Integer> result = nested.stream()
    .flatMap(Collection::stream)
    .distinct()
    .sorted()
    .collect(Collectors.toList());
```

---

### 5. anyMatch + findFirst

**Task:** Check if any name starts with `"A"`, then find the first name longer than 5 characters.

```java
List<String> names = List.of("Alice", "Bob", "Charlie", "Ana", "Dave");
```

**Expected output:**
- `anyMatch: true`
- `findFirst: Optional[Charlie]`

**Concepts:** `anyMatch`, `findFirst`, `Optional`

**Hint:** These are two separate stream pipelines on the same list.

**Solution:**
```java
boolean hasA = names.stream().anyMatch(n -> n.startsWith("A"));

Optional<String> first = names.stream()
    .filter(n -> n.length() > 5)
    .findFirst();
```

---

## Level 2 — Collectors

### 6. GroupingBy — group by string length

**Task:** Group a list of strings by their length into a `Map<Integer, List<String>>`.

```java
List<String> words = List.of("hi", "hey", "go", "yo", "bye", "see");
```

**Expected output:** `{2=[hi, go, yo], 3=[hey, bye, see]}`

**Concepts:** `collect`, `groupingBy`

**Solution:**
```java
Map<Integer, List<String>> grouped = words.stream()
    .collect(Collectors.groupingBy(String::length));
```

---

### 7. Collectors.joining — build a CSV string

**Task:** Given a list of names, produce a single comma-separated string with a header and footer.

```java
List<String> names = List.of("Alice", "Bob", "Charlie");
```

**Expected output:** `[Alice, Bob, Charlie]`

**Concepts:** `collect`, `Collectors.joining`

**Hint:** `Collectors.joining(delimiter, prefix, suffix)`

**Solution:**
```java
String result = names.stream()
    .collect(Collectors.joining(", ", "[", "]"));
```

---

### 8. Collectors.toMap — list to map

**Task:** Convert a list of strings to a `Map<String, Integer>` where the key is the word and the value is its length.

```java
List<String> words = List.of("apple", "kiwi", "fig");
```

**Expected output:** `{apple=5, kiwi=4, fig=3}`

**Concepts:** `collect`, `Collectors.toMap`

**Hint:** `Collectors.toMap(Function.identity(), String::length)`

**Solution:**
```java
Map<String, Integer> result = words.stream()
    .collect(Collectors.toMap(Function.identity(), String::length));
```

---

### 9. counting + groupingBy — frequency map

**Task:** Given a list of words, count how many times each word appears.

```java
List<String> words = List.of("apple", "banana", "apple", "cherry", "banana", "apple");
```

**Expected output:** `{apple=3, banana=2, cherry=1}`

**Concepts:** `groupingBy`, `Collectors.counting`

**Solution:**
```java
Map<String, Long> freq = words.stream()
    .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));
```

---

### 10. partitioningBy — split into two groups

**Task:** Partition a list of integers into two groups: those >= 10 and those < 10.

```java
List<Integer> nums = List.of(3, 12, 7, 18, 5, 10, 1);
```

**Expected output:** `{false=[3, 7, 5, 1], true=[12, 18, 10]}`

**Concepts:** `collect`, `Collectors.partitioningBy`

**Solution:**
```java
Map<Boolean, List<Integer>> partitioned = nums.stream()
    .collect(Collectors.partitioningBy(n -> n >= 10));
```

---

## Level 3 — Numeric Streams

### 11. IntStream range — sum 1 to 100

**Task:** Use `IntStream.rangeClosed` to sum all integers from 1 to 100.

**Expected output:** `5050`

**Concepts:** `IntStream`, `rangeClosed`, `sum`

**Solution:**
```java
int result = IntStream.rangeClosed(1, 100).sum();
```

---

### 12. mapToInt — average salary

**Task:** Calculate the average salary from a list of employees.

```java
record Employee(String name, int salary) {}

List<Employee> employees = List.of(
    new Employee("Alice", 80000),
    new Employee("Bob", 60000),
    new Employee("Charlie", 90000)
);
```

**Expected output:** `OptionalDouble[76666.666...]`

**Concepts:** `mapToInt`, `average`, `OptionalDouble`

**Solution:**
```java
OptionalDouble avg = employees.stream()
    .mapToInt(Employee::salary)
    .average();
```

---

### 13. IntStream — generate multiplication table

**Task:** Print a 3×3 multiplication table using `IntStream.range`.

**Expected output:**
```
1 2 3
2 4 6
3 6 9
```

**Concepts:** `IntStream.range`, `forEach`, nested streams

**Solution:**
```java
IntStream.rangeClosed(1, 3).forEach(i ->
    System.out.println(
        IntStream.rangeClosed(1, 3)
            .mapToObj(j -> i * j + " ")
            .collect(Collectors.joining())
    )
);
```

---

## Level 4 — Advanced Patterns

### 14. Chained collectors — top earner per department

**Task:** Given a list of employees with departments, find the highest-paid employee in each department.

```java
record Employee(String name, String dept, int salary) {}

List<Employee> employees = List.of(
    new Employee("Alice", "Eng", 95000),
    new Employee("Bob", "Eng", 80000),
    new Employee("Carol", "HR", 70000),
    new Employee("Dave", "HR", 75000)
);
```

**Expected output:** `{Eng=Employee[Alice, Eng, 95000], HR=Employee[Dave, HR, 75000]}`

**Concepts:** `groupingBy`, `Collectors.maxBy`, `Comparator`

**Solution:**
```java
Map<String, Optional<Employee>> topEarners = employees.stream()
    .collect(Collectors.groupingBy(
        Employee::dept,
        Collectors.maxBy(Comparator.comparingInt(Employee::salary))
    ));
```

---

### 15. peek — debug a pipeline

**Task:** Use `peek` to print each element at two stages of the pipeline (after filter, after map), then collect.

```java
List<Integer> nums = List.of(1, 2, 3, 4, 5, 6);
```

**Concepts:** `peek` (debugging only — never for side effects in production)

**Solution:**
```java
List<Integer> result = nums.stream()
    .filter(n -> n % 2 == 0)
    .peek(n -> System.out.println("after filter: " + n))
    .map(n -> n * n)
    .peek(n -> System.out.println("after map: " + n))
    .collect(Collectors.toList());
```

> **Note:** `peek` is intended for debugging. Avoid relying on it for side effects in production code.

---

### 16. Parallel stream — measure speedup

**Task:** Sum 10 million random doubles using both a sequential and a parallel stream. Observe the difference in time.

**Concepts:** `parallelStream`, `mapToDouble`, `sum`

**Solution:**
```java
List<Double> data = new Random().doubles(10_000_000).boxed()
    .collect(Collectors.toList());

// Sequential
long start = System.currentTimeMillis();
double seqSum = data.stream().mapToDouble(Double::doubleValue).sum();
System.out.println("Sequential: " + (System.currentTimeMillis() - start) + "ms");

// Parallel
start = System.currentTimeMillis();
double parSum = data.parallelStream().mapToDouble(Double::doubleValue).sum();
System.out.println("Parallel: " + (System.currentTimeMillis() - start) + "ms");
```

> **Note:** Parallel streams add overhead for small datasets. Benefit is typically seen with large data and CPU-bound operations.

---

### 17. Custom collector — product of all elements

**Task:** Write a custom `Collector` that computes the product (multiplication) of all integers in a stream.

```java
List<Integer> nums = List.of(1, 2, 3, 4, 5);
```

**Expected output:** `120`

**Concepts:** `Collector.of`, custom collector

**Solution:**
```java
Collector<Integer, int[], Integer> productCollector = Collector.of(
    () -> new int[]{1},
    (acc, val) -> acc[0] *= val,
    (a, b) -> { a[0] *= b[0]; return a; },
    acc -> acc[0]
);

int product = nums.stream().collect(productCollector);
```

---

### 18. Stream.iterate — Fibonacci sequence

**Task:** Generate the first 10 Fibonacci numbers using `Stream.iterate`.

**Expected output:** `[0, 1, 1, 2, 3, 5, 8, 13, 21, 34]`

**Concepts:** `Stream.iterate`, `limit`, two-element state with array

**Solution:**
```java
List<Long> fibs = Stream.iterate(new long[]{0, 1}, f -> new long[]{f[1], f[0] + f[1]})
    .limit(10)
    .map(f -> f[0])
    .collect(Collectors.toList());
```

---

### 19. takeWhile / dropWhile (Java 9+)

**Task:** Given a sorted list, take elements while they are less than 5, then separately drop elements while they are less than 5.

```java
List<Integer> nums = List.of(1, 2, 3, 4, 5, 6, 7);
```

**Expected output:**
- `takeWhile: [1, 2, 3, 4]`
- `dropWhile: [5, 6, 7]`

**Concepts:** `takeWhile`, `dropWhile` (Java 9+)

**Solution:**
```java
List<Integer> taken = nums.stream()
    .takeWhile(n -> n < 5)
    .collect(Collectors.toList());

List<Integer> dropped = nums.stream()
    .dropWhile(n -> n < 5)
    .collect(Collectors.toList());
```

---

### 20. Collectors.teeing (Java 12+)

**Task:** In a single pass, compute both the min and max of a list of integers.

```java
List<Integer> nums = List.of(3, 1, 4, 1, 5, 9, 2, 6);
```

**Expected output:** `Min: 1, Max: 9`

**Concepts:** `Collectors.teeing`, dual downstream collectors

**Solution:**
```java
record MinMax(int min, int max) {}

MinMax result = nums.stream()
    .collect(Collectors.teeing(
        Collectors.minBy(Comparator.naturalOrder()),
        Collectors.maxBy(Comparator.naturalOrder()),
        (min, max) -> new MinMax(min.get(), max.get())
    ));
```

---

## Quick reference

| Operation | Type | Description |
|---|---|---|
| `filter(Predicate)` | Intermediate | Keep elements matching condition |
| `map(Function)` | Intermediate | Transform each element |
| `flatMap(Function)` | Intermediate | Flatten nested streams |
| `sorted(Comparator)` | Intermediate | Sort elements |
| `distinct()` | Intermediate | Remove duplicates |
| `limit(n)` | Intermediate | Take first n elements |
| `skip(n)` | Intermediate | Skip first n elements |
| `peek(Consumer)` | Intermediate | Debug/inspect (no mutation) |
| `takeWhile(Predicate)` | Intermediate | Take while condition holds (Java 9+) |
| `dropWhile(Predicate)` | Intermediate | Drop while condition holds (Java 9+) |
| `collect(Collector)` | Terminal | Accumulate into collection/map/string |
| `reduce(identity, BinaryOp)` | Terminal | Fold to single value |
| `forEach(Consumer)` | Terminal | Side-effect per element |
| `count()` | Terminal | Count elements |
| `findFirst()` | Terminal | First element as Optional |
| `anyMatch / allMatch / noneMatch` | Terminal | Predicate checks → boolean |
| `min / max` | Terminal | Min/max as Optional |
| `sum / average` | Terminal | Numeric streams only |