package service;

import db.DatabaseManager;
import model.Employee;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class EmployeeService {

    public void addEmployee(Employee e) {
        String sql = "INSERT INTO employees (full_name, position, hire_date, annual_paid_leave_days, department_id, carryover_paid_leave_days) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection conn = DatabaseManager.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, e.getFullName());
            ps.setString(2, e.getPosition());
            ps.setDate(3, Date.valueOf(e.getHireDate()));
            ps.setInt(4, e.getAnnualPaidLeaveDays());
            ps.setInt(5, e.getDepartmentId());
            ps.setInt(6, e.getCarryoverPaidLeaveDays());
            ps.executeUpdate();
        } catch (SQLException ex) { throw new RuntimeException("Помилка додавання співробітника", ex); }
    }

    public List<Employee> getAllEmployees() {
        return fetchEmployees("SELECT * FROM employees");
    }

    public void updateEmployee(Employee e) {
        String sql = "UPDATE employees SET full_name=?, position=?, hire_date=?, annual_paid_leave_days=?, department_id=?, carryover_paid_leave_days=? WHERE id=?";
        try (Connection conn = DatabaseManager.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, e.getFullName());
            ps.setString(2, e.getPosition());
            ps.setDate(3, Date.valueOf(e.getHireDate()));
            ps.setInt(4, e.getAnnualPaidLeaveDays());
            ps.setInt(5, e.getDepartmentId());
            ps.setInt(6, e.getCarryoverPaidLeaveDays());
            ps.setInt(7, e.getId());
            ps.executeUpdate();
        } catch (SQLException ex) { throw new RuntimeException("Помилка оновлення", ex); }
    }

    public void deleteEmployee(int id) {
        String sql = "DELETE FROM employees WHERE id = ?";
        try (Connection conn = DatabaseManager.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            ps.executeUpdate();
        } catch (SQLException ex) { throw new RuntimeException("Помилка видалення", ex); }
    }

    // Повернуті методи пошуку
    public List<Employee> searchByDepartmentId(int deptId) {
        String sql = "SELECT * FROM employees WHERE department_id = ?";
        return fetchEmployeesWithParam(sql, deptId);
    }

    public List<Employee> searchByPosition(String position) {
        String sql = "SELECT * FROM employees WHERE position = ?";
        return fetchEmployeesWithStringParam(sql, position);
    }

    // Складний аналітичний запит
    public int getUnusedLeaveDays(int employeeId) {
        String sql = "SELECT (annual_paid_leave_days + carryover_paid_leave_days) - " +
                "COALESCE((SELECT SUM(DATEDIFF(DAY, start_date, end_date) + 1) " +
                "FROM vacations WHERE employee_id = ? AND vacation_type = 'paid'), 0) AS remaining " +
                "FROM employees WHERE id = ?";
        try (Connection conn = DatabaseManager.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, employeeId);
            ps.setInt(2, employeeId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return rs.getInt("remaining");
            }
        } catch (SQLException ex) { throw new RuntimeException("Помилка підрахунку відпусток", ex); }
        return 0;
    }

    // Допоміжні методи для уникнення дублювання коду
    private List<Employee> fetchEmployees(String sql) {
        List<Employee> list = new ArrayList<>();
        try (Connection conn = DatabaseManager.getConnection(); PreparedStatement ps = conn.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                list.add(new Employee(rs.getInt("id"), rs.getString("full_name"), rs.getString("position"), rs.getDate("hire_date").toLocalDate(), rs.getInt("annual_paid_leave_days"), rs.getInt("department_id"), rs.getInt("carryover_paid_leave_days")));
            }
        } catch (SQLException ex) { throw new RuntimeException("Помилка завантаження", ex); }
        return list;
    }

    private List<Employee> fetchEmployeesWithParam(String sql, int param) {
        List<Employee> list = new ArrayList<>();
        try (Connection conn = DatabaseManager.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, param);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) list.add(new Employee(rs.getInt("id"), rs.getString("full_name"), rs.getString("position"), rs.getDate("hire_date").toLocalDate(), rs.getInt("annual_paid_leave_days"), rs.getInt("department_id"), rs.getInt("carryover_paid_leave_days")));
            }
        } catch (SQLException ex) { throw new RuntimeException("Помилка пошуку", ex); }
        return list;
    }

    private List<Employee> fetchEmployeesWithStringParam(String sql, String param) {
        List<Employee> list = new ArrayList<>();
        try (Connection conn = DatabaseManager.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, param);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) list.add(new Employee(rs.getInt("id"), rs.getString("full_name"), rs.getString("position"), rs.getDate("hire_date").toLocalDate(), rs.getInt("annual_paid_leave_days"), rs.getInt("department_id"), rs.getInt("carryover_paid_leave_days")));
            }
        } catch (SQLException ex) { throw new RuntimeException("Помилка пошуку", ex); }
        return list;
    }

    public void addVacation(int employeeId, LocalDate startDate, LocalDate endDate, String type) {
        String sql = "INSERT INTO vacations (employee_id, start_date, end_date, vacation_type) VALUES (?, ?, ?, ?)";
        try (Connection conn = DatabaseManager.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, employeeId);
            ps.setDate(2, Date.valueOf(startDate));
            ps.setDate(3, Date.valueOf(endDate));
            ps.setString(4, type);
            ps.executeUpdate();
        } catch (SQLException ex) { throw new RuntimeException("Помилка додавання відпустки", ex); }
    }
}