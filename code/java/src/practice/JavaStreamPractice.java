package practice;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class JavaStreamPractice {
    public static void main(String[] args){
        //TASK 1: Given a list of integers, filter out odd numbers, double the evens, and collect to a list.
        List<Integer> nums = List.of(1, 2, 3, 4, 5, 6, 7, 8);

        List<Integer> result1 = nums.stream()
                .filter(n -> n%2 == 0)
                .map(n -> n*2)
                .collect(Collectors.toList());

        System.out.println("Result 1: " + result1);


        //TASK 2: Given a list of strings, sort by length descending and return only the top 3.
        List<String> words = List.of("apple", "kiwi", "strawberry", "fig", "blueberry", "pear");

        List<String> result2 = words.stream()
                .sorted(Comparator.comparingInt(String::length).reversed())
                .limit(3)
                .collect(Collectors.toList());

        System.out.println("Result 2: " + result2);


        // TASK 3: Square each number in the list, then reduce to a single sum.

        List<Integer> nums2 = List.of(1, 2, 3, 4, 5);

        int result3 = nums2.stream()
                .map(n -> n*n)
                .reduce(0, Integer::sum);

        System.out.println("Result 3: " + result3);

        // TASK 4: Given a list of lists, flatten them into a single stream and collect unique values, sorted ascending.
        List<List<Integer>> nested = List.of(
                List.of(1, 2, 3),
                List.of(2, 3, 4),
                List.of(3, 4, 5)
        );

        List<Integer> result4 = nested.stream()
                .flatMap(Collection::stream)
                .distinct()
                .sorted()
                .collect(Collectors.toList());

        System.out.println("Result 4: " + result4);

        // TASK 5:  Check if any name starts with "A", then find the first name longer than 5 characters.

        List<String> names = List.of("Alice", "Bob", "Charlie", "Ana", "Dave");

        boolean hasA = names.stream().anyMatch(n -> n.startsWith("A"));



        Optional<String> result5 = names.stream()
                .filter(n -> n.length() > 5)
                .findFirst();

        System.out.println("Result 5: " + hasA + ", " + result5);


        // TASK 6: Group a list of strings by their length into a Map<Integer, List<String>>.

        List<String> words2 = List.of("hi", "hey", "go", "yo", "bye", "see");

        Map<Integer, List<String>> result6 = words2.stream()
                .collect(Collectors.groupingBy(String::length));

        System.out.println("Result 6: " + result6);

        // TASK 7: Given a list of names, produce a single comma-separated string with a header and footer.

        List<String> names2 = List.of("Alice", "Bob", "Charlie");
        String result7 = names2.stream()
                .collect(Collectors.joining(",", "[", "]"));
        System.out.println("Result 7: " + result7);

        // TASK 8: Convert a list of strings to a Map<String, Integer> where the key is the word and the value is its length.

        List<String> words3 = List.of("apple", "kiwi", "fig");
        Map<String, Integer> result8 = words3.stream()
                .collect(Collectors.toMap(Function.identity(), String::length));
        System.out.println("Result 8: " + result8);

        // TASK 9: Given a list of words, count how many times each word appears.

        List<String> words4 = List.of("apple", "banana", "apple", "cherry", "banana", "apple");

        Map<String, Long> result9 = words4.stream()
                .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));
        System.out.println("Result 9: " + result9);

        // TASK 10: Partition a list of integers into two groups: those >= 10 and those < 10.
        List<Integer> nums3 = List.of(3, 12, 7, 18, 5, 10, 1);

        Map<Boolean, List<Integer>> result10 = nums3.stream()
                .collect(Collectors.partitioningBy(n -> n >= 10));

        System.out.println("Result 10: " + result10);

        // TASK 11: Use IntStream.rangeClosed to sum all integers from 1 to 100.

        int result11 = IntStream.rangeClosed(1,100).sum();
        System.out.println("Result 11: " + result11);

        // TASK 12: Calculate the average salary from a list of employees.

        class Employee{
            String name;
            int salary;

            Employee(String name, Integer salary){
                this.name = name;
                this.salary = salary;
            }
        }

        List<Employee> employees = List.of(
                new Employee("Alice", 80000),
                new Employee("Bob", 60000),
                new Employee("Charlie", 90000)
        );

        OptionalDouble result12 = employees.stream()
                .mapToInt(e -> e.salary)
                .average();

        System.out.println("Result 12: " + result12);

        // TASK 13: Print a 3×3 multiplication table using IntStream.range.

        IntStream.rangeClosed(1, 3).forEach(
                i -> System.out.println(
                        IntStream.rangeClosed(1,3)
                                .mapToObj(j -> i * j + " ")
                                .collect(Collectors.joining())
                )
        );
    }

}
