package workbook;

import lombok.Getter;

@Getter
public class Employee {
    private final String name;
    private final String dept;
    private final int salary;
    private final int age;
    public Employee(String name, String dept, int salary, int age) {
        this.name = name; this.dept = dept;
        this.salary = salary; this.age = age;
    }
}
