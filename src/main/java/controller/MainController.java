package controller;

import model.Employee;
import service.EmployeeService;
import java.util.List;

public class MainController {
    private final EmployeeService service;

    public MainController() {
        this.service = new EmployeeService();
    }

    public void addEmployee(String fullName, String position, double salary, String department) {
        validateEmployeeData(fullName, position, salary, department);
        Employee e = new Employee(fullName, position, salary, department);
        service.addEmployee(e);
    }

    public List<Employee> getAllEmployees() {
        return service.getAllEmployees();
    }

    public void updateEmployee(int id, String fullName, String position, double salary, String department) {
        if (id <= 0) {
            throw new IllegalArgumentException("ID must be positive");
        }

        validateEmployeeData(fullName, position, salary, department);
        Employee e = new Employee(id, fullName, position, salary, department);
        service.updateEmployee(e);
    }

    public void deleteEmployee(int id) {
        if (id <= 0) {
            throw new IllegalArgumentException("ID must be positive");
        }
        service.deleteEmployee(id);
    }

    public List<Employee> searchByDepartment(String dept) {
        if (dept == null || dept.trim().isEmpty()) {
            throw new IllegalArgumentException("Department cannot be empty");
        }
        return service.searchByDepartment(dept.trim());
    }

    public List<Employee> searchByPosition(String pos) {
        if (pos == null || pos.trim().isEmpty()) {
            throw new IllegalArgumentException("Position cannot be empty");
        }
        return service.searchByPosition(pos.trim());
    }

    private void validateEmployeeData(String fullName, String position, double salary, String department) {
        if (fullName == null || fullName.trim().isEmpty()) {
            throw new IllegalArgumentException("Full name cannot be empty");
        }
        if (position == null || position.trim().isEmpty()) {
            throw new IllegalArgumentException("Position cannot be empty");
        }
        if (salary <= 0) {
            throw new IllegalArgumentException("Salary must be positive");
        }
        if (department == null || department.trim().isEmpty()) {
            throw new IllegalArgumentException("Department cannot be empty");
        }
    }
}
