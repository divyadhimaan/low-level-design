package workbook;


import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class Demo {
    public static void main(String[] args) {
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

        List<Integer> numsEven = nums.stream()
                .filter(n -> n%2 == 0)
                .toList();


        System.out.println("1.1: Even numbers: " + numsEven);


        List<Integer> numSquared = nums.stream()
                .map(x -> x * x)
                .toList();

        System.out.println("1.2: Squared numbers: " + numSquared);


        List<Integer> lengthOfWords = words.stream()
                .map(String::length)
                .toList();

        System.out.println("1.3: Length of words: " + lengthOfWords);


        List<String> wordsStartWithVowel = words.stream()
                .filter(word -> word.startsWith("a")
                        || word.startsWith("e")
                        || word.startsWith("i")
                        || word.startsWith("o")
                        || word.startsWith("u"))
                .toList();

        System.out.println("1.4: Words starting with vowel: " + wordsStartWithVowel);


        List<String> orderClients = orders.stream()
                .map(Order::getClient)
                .toList();

        System.out.println("1.5: Client names for all orders (duplicates allowed): " + orderClients);


        List<String> orderClientsWithoutDuplicates = orders.stream()
                .map(Order::getClient)
                .collect(Collectors.collectingAndThen(
                        Collectors.toSet(),
                        ArrayList::new
                ));

        System.out.println("1.5.a: Client names for all orders (without duplicates): "
                + orderClientsWithoutDuplicates);


        List<String> clientsFromEngDept = employees.stream()
                .filter(e -> e.getDept().equals("Eng"))
                .map(Employee::getName)
                .toList();

        System.out.println("1.6: Client names from Eng Department: " + clientsFromEngDept);


        List<Integer> evenNumsToTen = nums.stream()
                .filter(n -> n % 2 == 0)
                .map(x -> x * 10)
                .toList();

        System.out.println("1.7: Even Numbers multiplied by 10: " + evenNumsToTen);


        List<Double> totaValue = orders.stream()
                .map(o -> o.getQuantity() * o.getPrice())
                .toList();

        System.out.println("1.8: Total value per order (price * quantity): " + totaValue);


        System.out.println("---------------------------------------------------------------------------");

        int wordLengthMoreThanFour = (int) words.stream()
                .filter(w -> w.length() > 4)
                .count();

        System.out.println("2.1: Count of words with length more than 4: " + wordLengthMoreThanFour);

        boolean employeeEarningsMoreThan130000 =  employees.stream()
                .anyMatch(e -> e.getSalary() > 130000);

        System.out.println("2.2: If any employee have Earning more than 130000: " + employeeEarningsMoreThan130000);


        boolean allOrdersQuantityPositive = orders.stream()
                .allMatch(o -> o.getQuantity() > 0);

        System.out.println("2.3: Are all orders for a positive quantity: " + allOrdersQuantityPositive);

        String firstWordStartingWithC = words.stream()
                .filter(w -> w.startsWith("c"))
                .findFirst()
                .orElse("No word found");

        System.out.println("2.4: First word starting with C: " + firstWordStartingWithC);

        Employee highestPaidEmployee = employees.stream()
                .max(Comparator.comparingInt(Employee::getSalary))
                .orElse(null);

        System.out.println("2.5: Highest Paid Employee: " + highestPaidEmployee.getName() + " with salary: " + highestPaidEmployee.getSalary());

        String youngestEmployee = employees.stream()
                .min(Comparator.comparingInt(Employee::getAge))
                .map(Employee::getName)
                .orElse("none");

        System.out.println("2.6: Youngest Employee: " + youngestEmployee);

        int totalQuantityOrdered = orders.stream()
                .mapToInt(Order::getQuantity)
                .sum();
        System.out.println("2.7: Sum of all quantities across all orders: " + totalQuantityOrdered);

        int averageSalary = (int) employees.stream()
                .mapToInt(Employee::getSalary)
                .average()
                .orElse(0);

        System.out.println("2.8: Average salary across all employees: " + averageSalary);

        int maxWordLength = words.stream()
                .mapToInt(String::length)
                .max()
                .orElse(0);

        System.out.println("2.9: The max word length in words.: " + maxWordLength);

        nums.forEach(System.out::println);

    }
}
