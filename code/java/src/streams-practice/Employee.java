import lombok.Getter;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Getter
public class Employee {
    String name;
    String dept;
    int salary;

    Employee(String name, String dept, int salary) {
        this.name = name;
        this.dept = dept;
        this.salary = salary;
    }

}

class Demo{
    public static void main(String[] args) {
        Employee emp1 = new Employee("Alice", "A", 50000);
        Employee emp2 = new Employee("Bob", "B", 60000);
        Employee emp3 = new Employee("Charlie", "A", 80000);
        Employee emp4 = new Employee("David", "C", 70000);

        List<Employee> employees = new ArrayList<>();
        employees.add(emp1);
        employees.add(emp2);
        employees.add(emp3);
        employees.add(emp4);

        //P1. Return the names of all employees earning more than 100000, sorted alphabetically.
        List<String> names = employees.stream()
                .filter(e -> e.getSalary() > 60000)
                .map(Employee::getName)
                .sorted()
                .collect(Collectors.toList());

        System.out.println(names);

        Map<String, Double> avgSalaryByDept = employees.stream()
                .collect(Collectors.groupingBy(Employee::getDept,
                        Collectors.averagingDouble(Employee::getSalary)));

        System.out.println(avgSalaryByDept);

        String highestPaidEmployee = employees.stream()
                .max(Comparator.comparingInt(Employee::getSalary))
                .map(Employee::getName)
                .orElse("No employees found");

        System.out.println(highestPaidEmployee);
    }
}
