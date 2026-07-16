# Java Streams Workbook

A progressive, hands-on workbook for building stream-writing fluency from scratch.
Six levels, basic → advanced. **Write every exercise from a blank page before checking the answer key.** Recall-from-nothing is the muscle that builds real confidence — reading solutions does not.

**How to use this:**
1. Read the "Concepts" block for a level.
2. Do the exercises **without looking at the answer key** (separate file).
3. Check, note what slipped, redo any you got wrong the next day.
4. Move to the next level only when a level feels automatic.

**Setup — the data classes used throughout:**

```java
public class Order {
    private final String client;
    private final String stock;
    private final int quantity;
    private final double price;
    public Order(String client, String stock, int quantity, double price) {
        this.client = client; this.stock = stock;
        this.quantity = quantity; this.price = price;
    }
    public String getClient()  { return client; }
    public String getStock()   { return stock; }
    public int getQuantity()   { return quantity; }
    public double getPrice()   { return price; }
}

public class Employee {
    private final String name;
    private final String dept;
    private final int salary;
    private final int age;
    public Employee(String name, String dept, int salary, int age) {
        this.name = name; this.dept = dept;
        this.salary = salary; this.age = age;
    }
    public String getName() { return name; }
    public String getDept() { return dept; }
    public int getSalary()  { return salary; }
    public int getAge()     { return age; }
}
```

```java
List<Integer> nums = List.of(5, 2, 8, 1, 9, 3, 7, 4, 6);
List<String>  words = List.of("apple", "banana", "kiwi", "cherry", "fig", "grape");

List<Order> orders = List.of(
    new Order("Alice", "AAPL", 10, 150.0),
    new Order("Alice", "GOOG", 5,  2800.0),
    new Order("Alice", "AAPL", 3,  152.0),
    new Order("Bob",   "TSLA", 8,  700.0),
    new Order("Bob",   "AAPL", 2,  151.0),
    new Order("Carol", "GOOG", 7,  2810.0)
);

List<Employee> employees = List.of(
    new Employee("Alice",   "Eng",     120000, 30),
    new Employee("Bob",     "Eng",     95000,  27),
    new Employee("Carol",   "Sales",   80000,  35),
    new Employee("Dave",    "Sales",   85000,  41),
    new Employee("Eve",     "Eng",     140000, 38),
    new Employee("Frank",   "Finance", 110000, 33)
);
```

---

## Level 1 — Foundations: filter, map, collect

**Concepts**

A stream pipeline has three parts: a **source** (`list.stream()`), zero or more **intermediate operations** (lazy — build the pipeline, do no work), and one **terminal operation** (triggers execution, produces a result).

The three you use constantly:
- `filter(predicate)` — keep elements matching a condition. `Stream<T> -> Stream<T>`.
- `map(function)` — transform each element 1-to-1. `Stream<T> -> Stream<R>`.
- `collect(Collectors.toList())` — gather results into a List (terminal).

Method references: `String::toUpperCase`, `Employee::getName`, `Order::getQuantity` — shorthand for `x -> x.toUpperCase()` etc.

**Worked example**
```java
// Names of words longer than 4 chars, uppercased
List<String> result = words.stream()
    .filter(w -> w.length() > 4)
    .map(String::toUpperCase)
    .collect(Collectors.toList());
```

**Exercises**

1.1  From `nums`, return a list of only the even numbers.
1.2  From `nums`, return each number squared.
1.3  From `words`, return the length of each word (as `List<Integer>`).
1.4  From `words`, return words that start with a vowel.
1.5  From `orders`, return the client name of every order (duplicates OK).
1.6  From `employees`, return names of all employees in the "Eng" department.
1.7  From `nums`, return even numbers, each multiplied by 10 (combine filter + map).
1.8  From `orders`, return the total value (`quantity * price`) of each order as `List<Double>`.

---

## Level 2 — Terminal operations & finding

**Concepts**

Terminal ops beyond `collect`:
- `count()` — number of elements (`long`).
- `anyMatch` / `allMatch` / `noneMatch` — boolean checks, short-circuit.
- `findFirst()` / `findAny()` — return `Optional<T>`; findAny is for parallel efficiency.
- `min(comparator)` / `max(comparator)` — return `Optional<T>`.
- `forEach(action)` — side-effect per element (avoid for building results).

Primitive streams for numbers:
- `mapToInt` / `mapToDouble` → `IntStream` / `DoubleStream`, which have `sum()`, `average()`, `max()`, `min()`, `summaryStatistics()` directly.

**Worked example**
```java
// Highest-paid employee's name, safe on empty
String top = employees.stream()
    .max(Comparator.comparingInt(Employee::getSalary))
    .map(Employee::getName)
    .orElse("none");
```

**Exercises**

2.1  Count how many words have length > 4.
2.2  Does any employee earn more than 130000? (boolean)
2.3  Are all orders for a positive quantity? (boolean)
2.4  Find the first word starting with "c" (Optional, handle absent).
2.5  Find the employee with the highest salary (the whole `Employee`, Optional).
2.6  Find the youngest employee's name (min by age), fallback "none".
2.7  Sum of all quantities across all orders (use `mapToInt`).
2.8  Average salary across all employees (use `mapToInt` + `average`, handle empty).
2.9  The max word length in `words`.
2.10 Print each number in `nums` on its own line (forEach).

---

## Level 3 — Sorting, distinct, limit, skip

**Concepts**

Stateful intermediate ops (need to see multiple/all elements):
- `sorted()` — natural order; `sorted(comparator)` — custom.
- `distinct()` — remove duplicates (uses `equals`).
- `limit(n)` / `skip(n)` — take first n / drop first n. Great for "top N" and pagination.

`Comparator` fluency:
- `Comparator.comparing(Employee::getName)`
- `Comparator.comparingInt(Employee::getSalary).reversed()`
- `.thenComparing(...)` for tie-breakers.

**Worked example**
```java
// Top 3 highest-paid employee names
List<String> top3 = employees.stream()
    .sorted(Comparator.comparingInt(Employee::getSalary).reversed())
    .limit(3)
    .map(Employee::getName)
    .collect(Collectors.toList());
```

**Exercises**

3.1  Sort `nums` ascending into a list.
3.2  Sort `nums` descending into a list.
3.3  From `orders`, the distinct stock symbols.
3.4  From `words`, sort by length (shortest first).
3.5  From `words`, sort by length, then alphabetically for ties (thenComparing).
3.6  The 2 lowest-paid employees' names.
3.7  From `nums` sorted ascending, skip the first 3, return the rest.
3.8  From `employees`, sort by dept ascending then salary descending, return names.
3.9  The 3 largest numbers in `nums`, in descending order.
3.10 Distinct first letters of all words, sorted.

---

## Level 4 — Grouping & aggregation (the GS core)

**Concepts**

`Collectors.groupingBy` is the workhorse. Two forms:
- `groupingBy(classifier)` → `Map<K, List<T>>`
- `groupingBy(classifier, downstream)` → `Map<K, R>`

Downstream collectors (the second argument):
- `counting()` → `Long` per group
- `summingInt(fn)` / `summingDouble(fn)` → sum per group
- `averagingInt(fn)` / `averagingDouble(fn)` → average per group
- `maxBy(comparator)` / `minBy(comparator)` → `Optional<T>` per group
- `mapping(fn, downstream)` → transform then collect
- `joining(delimiter)` → concatenate strings

**The argument-order rule:** `mapping(whatToExtract, howToGather)`, `collectingAndThen(collector, finisher)`. Extract/collect first, transform second.

`partitioningBy(predicate)` → `Map<Boolean, List<T>>` — split into true/false groups.

**Worked examples**
```java
// Total quantity per client
Map<String, Integer> qtyByClient = orders.stream()
    .collect(Collectors.groupingBy(Order::getClient,
             Collectors.summingInt(Order::getQuantity)));

// Distinct stocks per client, as a List (set removes dupes, then back to List)
Map<String, List<String>> stocksByClient = orders.stream()
    .collect(Collectors.groupingBy(Order::getClient,
             Collectors.mapping(Order::getStock,
             Collectors.collectingAndThen(Collectors.toSet(), ArrayList::new))));
```

**Exercises**

4.1  Group employees by department → `Map<String, List<Employee>>`.
4.2  Count of employees per department → `Map<String, Long>`.
4.3  Average salary per department → `Map<String, Double>`.
4.4  Total quantity per client (from `orders`).
4.5  Highest salary per department → `Map<String, Integer>` (use maxBy + collectingAndThen).
4.6  Per client, a comma-joined string of their stocks → `Map<String, String>`.
4.7  Per department, the list of employee *names* → `Map<String, List<String>>` (mapping).
4.8  Partition employees into those earning ≥ 100000 vs below → `Map<Boolean, List<Employee>>`.
4.9  Per client, the max single-order quantity → `Map<String, Integer>`.
4.10 Count of orders per stock → `Map<String, Long>`.
4.11 Per department, comma-joined names sorted alphabetically.
4.12 Group words by their length → `Map<Integer, List<String>>`.

---

## Level 5 — flatMap, Optional, toMap

**Concepts**

`flatMap(fn)` — each element becomes a *stream*, all flattened into one. For nested structures: list-of-lists, or objects containing collections.
```java
List<List<Integer>> nested = ...;
List<Integer> flat = nested.stream()
    .flatMap(List::stream)
    .collect(Collectors.toList());
```

`Collectors.toMap(keyFn, valueFn)` — build a map directly. **Watch for duplicate keys** — add a merge function: `toMap(k, v, (a, b) -> a)`.

`Optional` chaining: `.map(...)`, `.filter(...)`, `.orElse(...)`, `.orElseGet(...)`, `.ifPresent(...)`.

**Worked example**
```java
// All distinct stocks any client ordered, from a Map<String,List<String>>
List<String> all = stocksByClient.values().stream()
    .flatMap(List::stream)
    .distinct()
    .collect(Collectors.toList());
```

**Exercises**

Setup for flatMap: `List<List<Integer>> matrix = List.of(List.of(1,2,3), List.of(4,5), List.of(6));`
Setup: `List<String> sentences = List.of("the quick fox", "jumps over", "the lazy dog");`

5.1  Flatten `matrix` into a single `List<Integer>`.
5.2  Sum of all numbers in `matrix` (flatMap → mapToInt → sum, or flatMapToInt).
5.3  From `sentences`, a list of all individual words (split on space, flatten).
5.4  From `sentences`, count of distinct words.
5.5  Build `Map<String, Integer>` from `employees`: name → salary (toMap).
5.6  Build `Map<String, Double>` from `orders`: stock → price — handle duplicate keys by keeping the max price (toMap with merge).
5.7  From `employees`, the longest word among all names, as Optional → orElse "none".
5.8  From `orders`, all distinct stocks sorted, then joined with ", ".
5.9  From `matrix`, only the even numbers, flattened and sorted.
5.10 Build `Map<String, List<String>>` client → stocks using `toMap` with a merge that concatenates lists (contrast with groupingBy).

---

## Level 6 — Advanced: multi-level grouping, teeing, reduce, summary stats

**Concepts**

Multi-level grouping — nest `groupingBy`:
```java
Map<String, Map<String, List<Employee>>> byDeptThenAge = employees.stream()
    .collect(Collectors.groupingBy(Employee::getDept,
             Collectors.groupingBy(e -> e.getAge() > 35 ? "senior" : "junior")));
```

`summarizingInt(fn)` → `IntSummaryStatistics` (count, sum, min, max, average in one pass).

`reduce` — collapse to one value. Three forms: `reduce(op)` → Optional; `reduce(identity, op)` → value; `reduce(identity, accumulator, combiner)` → parallel.

`teeing(downstream1, downstream2, merger)` (Java 12+) — run two collectors, combine results. E.g. average = sum teed with count.

**Worked example**
```java
// Salary stats per department in one pass
Map<String, IntSummaryStatistics> stats = employees.stream()
    .collect(Collectors.groupingBy(Employee::getDept,
             Collectors.summarizingInt(Employee::getSalary)));
// stats.get("Eng").getAverage(), .getMax(), .getCount() ...
```

**Exercises**

6.1  Product of all numbers in `nums` using `reduce`.
6.2  Concatenate all words into one string using `reduce` (identity "").
6.3  Max of `nums` using `reduce` (returns Optional).
6.4  Two-level group: employees by dept, then by "senior"/"junior" (age > 35).
6.5  Salary `IntSummaryStatistics` per department (summarizingInt).
6.6  Per department: a string like "Eng: 3 employees, avg 118333" (groupingBy + collectingAndThen on stats).
6.7  From `orders`, total portfolio value per client (sum of quantity*price) → `Map<String, Double>`.
6.8  Using `teeing`, compute for `nums` both the min and max, combined into an `int[]{min, max}` or a formatted string.
6.9  Two-level group: orders by client, then total quantity per stock → `Map<String, Map<String, Integer>>`.
6.10 The department with the highest average salary (group → average → max entry → key).
6.11 Count employees per (dept, ageBucket) where ageBucket is decade (20s, 30s, 40s).
6.12 From `orders`, the single most valuable order (max quantity*price), return a description string.

---

## Quick reference — which tool when

| Goal | Reach for |
|---|---|
| Keep matching elements | `filter` |
| Transform 1-to-1 | `map` |
| Transform 1-to-many + flatten | `flatMap` |
| Gather into List/Set | `collect(toList/toSet)` |
| Build a map from key+value fns | `toMap(k, v, mergeFn?)` |
| Group into buckets | `groupingBy(classifier, downstream?)` |
| Split into true/false | `partitioningBy(predicate)` |
| Count per group | downstream `counting()` |
| Sum/avg per group | downstream `summingInt` / `averagingInt` |
| Max/min per group | downstream `maxBy`/`minBy` (+ `collectingAndThen` to unwrap) |
| Extract-then-collect per group | downstream `mapping(fn, collector)` |
| Join strings | `joining(delim)` |
| Post-process a collector result | `collectingAndThen(collector, finisher)` |
| All stats in one pass | `summarizingInt(fn)` |
| Collapse to single value | `reduce(identity, op)` |
| "Which X has the most Y" | `group → count → entrySet().stream() → max(comparingByValue) → map(getKey)` |

**Two rules that fix 80% of bugs:**
1. Collectors go **inside `collect()`**, never inside `map()`.
2. Argument order: **extract/collect first, transform/gather second** — `mapping(fn, collector)`, `collectingAndThen(collector, finisher)`.
