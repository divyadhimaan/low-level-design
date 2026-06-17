package RoomBookingSystem.repository;

import RoomBookingSystem.model.Employee;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class EmployeeInventory {
    Map<UUID, Employee> employeeList;

    public EmployeeInventory() {
        this.employeeList = new ConcurrentHashMap<>();
    }

    public synchronized void addEmployee(Employee employee) {
        employeeList.put(employee.getEmployeeId(), employee);
    }

    public synchronized Employee getEmployeeByName(String name) {
        for (Employee e : employeeList.values()) {
            if (e.getEmployeeName().equals(name)) { //shouldn't ignore case here.
                return e;
            }
        }
        return null;
    }

    public synchronized Map<UUID, Employee> getAllEmployees() {
        return new HashMap<>(employeeList);
    }

    public synchronized boolean checkEmployeeExists(String name) {
        return getEmployeeByName(name) != null;
    }
}
