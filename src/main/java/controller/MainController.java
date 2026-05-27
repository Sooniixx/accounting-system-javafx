package controller;

import model.Employee;
import service.EmployeeService;
import java.time.LocalDate;
import java.util.List;

public class MainController {
    private final EmployeeService employeeService;

    public MainController() {
        this.employeeService = new EmployeeService();
    }

    public void addEmployee(String fullName, String position, LocalDate hireDate, int annualDays, int deptId, int carryoverDays) {
        validateEmployeeData(fullName, position, hireDate, annualDays, deptId, carryoverDays);
        Employee e = new Employee(0, fullName, position, hireDate, annualDays, deptId, carryoverDays);
        employeeService.addEmployee(e);
    }

    public List<Employee> getAllEmployees() {
        return employeeService.getAllEmployees();
    }

    public void updateEmployee(int id, String fullName, String position, LocalDate hireDate, int annualDays, int deptId, int carryoverDays) {
        if (id <= 0) {
            throw new IllegalArgumentException("ID співробітника повинен бути додатнім");
        }
        validateEmployeeData(fullName, position, hireDate, annualDays, deptId, carryoverDays);
        Employee e = new Employee(id, fullName, position, hireDate, annualDays, deptId, carryoverDays);
        employeeService.updateEmployee(e);
    }

    public void deleteEmployee(int id) {
        if (id <= 0) {
            throw new IllegalArgumentException("ID співробітника повинен бути додатнім");
        }
        employeeService.deleteEmployee(id);
    }

    public List<Employee> searchByDepartmentId(int deptId) {
        if (deptId <= 0) {
            throw new IllegalArgumentException("ID відділу повинен бути додатнім");
        }
        return employeeService.searchByDepartmentId(deptId);
    }

    public List<Employee> searchByPosition(String position) {
        if (position == null || position.trim().isEmpty()) {
            throw new IllegalArgumentException("Посада не може бути порожньою");
        }
        return employeeService.searchByPosition(position.trim());
    }

    public int getUnusedLeaveDays(int employeeId) {
        if (employeeId <= 0) {
            throw new IllegalArgumentException("ID співробітника повинен бути додатнім");
        }
        return employeeService.getUnusedLeaveDays(employeeId);
    }

    // Приватний метод для жорсткої перевірки даних перед відправкою в БД
    private void validateEmployeeData(String fullName, String position, LocalDate hireDate, int annualDays, int deptId, int carryoverDays) {
        if (fullName == null || fullName.trim().isEmpty()) {
            throw new IllegalArgumentException("ПІБ не може бути порожнім");
        }
        if (position == null || position.trim().isEmpty()) {
            throw new IllegalArgumentException("Посада не може бути порожньою");
        }
        if (hireDate == null) {
            throw new IllegalArgumentException("Дата прийому на роботу не може бути порожньою");
        }
        if (annualDays <= 0) {
            throw new IllegalArgumentException("Кількість днів щорічної відпустки повинна бути більшою за 0");
        }
        if (deptId <= 0) {
            throw new IllegalArgumentException("ID відділу повинен бути додатнім");
        }
        if (carryoverDays < 0) {
            throw new IllegalArgumentException("Перенесені дні відпустки не можуть бути від'ємними");
        }
    }

    public void addVacation(int employeeId, LocalDate startDate, LocalDate endDate, String type) {
        if (startDate == null || endDate == null) throw new IllegalArgumentException("Оберіть дати початку та кінця відпустки");
        if (endDate.isBefore(startDate)) throw new IllegalArgumentException("Дата кінця не може бути раніше дати початку");
        employeeService.addVacation(employeeId, startDate, endDate, type);
    }

    public void addDepartment(String name) {
        if (name == null || name.trim().isEmpty()) throw new IllegalArgumentException("Назва відділу не може бути порожньою");
        new service.DepartmentService().addDepartment(name);
    }

    public List<model.Department> getAllDepartments() {
        return new service.DepartmentService().getAllDepartments();
    }
}