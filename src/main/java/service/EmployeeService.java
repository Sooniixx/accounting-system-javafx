package service;

import db.DatabaseManager;
import model.Employee;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class EmployeeService {

    // Create (insert)
    public void addEmployee(Employee e) {
        String sql = "INSERT INTO employees (full_name, position, hire_date, annual_days, carryover_days, department_id) VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection conn = DatabaseManager.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, e.getFullName());
            ps.setString(2, e.getPosition());
            ps.setDate(3, e.getHireDate() != null ? Date.valueOf(e.getHireDate()) : null);
            ps.setInt(4, e.getAnnualPaidLeaveDays());
            ps.setInt(5, e.getCarryoverPaidLeaveDays());
            ps.setInt(6, e.getDepartmentId());

            ps.executeUpdate();

        } catch (SQLException ex) {
            throw new RuntimeException("Не вдалося додати співробітника: " + e.getFullName(), ex);
        }
    }

    // Read (select all)
    public List<Employee> getAllEmployees() {
        List<Employee> list = new ArrayList<>();
        String sql = "SELECT * FROM employees";

        try (Connection conn = DatabaseManager.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql);
                ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Employee e = new Employee();
                e.setId(rs.getInt("id"));
                e.setFullName(rs.getString("full_name"));
                e.setPosition(rs.getString("position"));

                Date hireDate = rs.getDate("hire_date");
                if (hireDate != null) {
                    e.setHireDate(hireDate.toLocalDate());
                }

                e.setAnnualPaidLeaveDays(rs.getInt("annual_days"));
                e.setCarryoverPaidLeaveDays(rs.getInt("carryover_days"));
                e.setDepartmentId(rs.getInt("department_id"));

                list.add(e);
            }

        } catch (SQLException ex) {
            throw new RuntimeException("Не вдалося отримати співробітників", ex);
        }

        return list;
    }

    // Update
    public void updateEmployee(Employee e) {
        String sql = "UPDATE employees SET full_name=?, position=?, hire_date=?, annual_days=?, carryover_days=?, department_id=? WHERE id=?";

        try (Connection conn = DatabaseManager.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, e.getFullName());
            ps.setString(2, e.getPosition());
            ps.setDate(3, e.getHireDate() != null ? Date.valueOf(e.getHireDate()) : null);
            ps.setInt(4, e.getAnnualPaidLeaveDays());
            ps.setInt(5, e.getCarryoverPaidLeaveDays());
            ps.setInt(6, e.getDepartmentId());
            ps.setInt(7, e.getId());

            int rowsAffected = ps.executeUpdate();
            if (rowsAffected == 0) {
                throw new RuntimeException("Не знайдено працівника з ID: " + e.getId());
            }

        } catch (SQLException ex) {
            throw new RuntimeException("Не вдалося оновити інформацію для співробітника з ID: " + e.getId(), ex);
        }
    }

    // Delete
    public void deleteEmployee(int id) {
        String sql = "DELETE FROM employees WHERE id=?";

        try (Connection conn = DatabaseManager.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, id);
            int rowsAffected = ps.executeUpdate();
            if (rowsAffected == 0) {
                throw new RuntimeException("Не знайдено працівника з ID: " + id);
            }

        } catch (SQLException ex) {
            throw new RuntimeException("Не вдалося видалити співробітника з ID: " + id, ex);
        }
    }

    // Search by Department ID
    public List<Employee> searchByDepartmentId(int deptId) {
        List<Employee> list = new ArrayList<>();
        String sql = "SELECT * FROM employees WHERE department_id = ?";

        try (Connection conn = DatabaseManager.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, deptId);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Employee e = new Employee();
                    e.setId(rs.getInt("id"));
                    e.setFullName(rs.getString("full_name"));
                    e.setPosition(rs.getString("position"));

                    Date hireDate = rs.getDate("hire_date");
                    if (hireDate != null) {
                        e.setHireDate(hireDate.toLocalDate());
                    }

                    e.setAnnualPaidLeaveDays(rs.getInt("annual_days"));
                    e.setCarryoverPaidLeaveDays(rs.getInt("carryover_days"));
                    e.setDepartmentId(rs.getInt("department_id"));

                    list.add(e);
                }
            }

        } catch (SQLException ex) {
            throw new RuntimeException("Не вдалося знайти співробітників за ID відділу: " + deptId, ex);
        }

        return list;
    }

    // Search by Position
    public List<Employee> searchByPosition(String pos) {
        List<Employee> list = new ArrayList<>();
        String sql = "SELECT * FROM employees WHERE position LIKE ?";

        try (Connection conn = DatabaseManager.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, "%" + pos + "%");

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Employee e = new Employee();
                    e.setId(rs.getInt("id"));
                    e.setFullName(rs.getString("full_name"));
                    e.setPosition(rs.getString("position"));

                    Date hireDate = rs.getDate("hire_date");
                    if (hireDate != null) {
                        e.setHireDate(hireDate.toLocalDate());
                    }

                    e.setAnnualPaidLeaveDays(rs.getInt("annual_days"));
                    e.setCarryoverPaidLeaveDays(rs.getInt("carryover_days"));
                    e.setDepartmentId(rs.getInt("department_id"));

                    list.add(e);
                }
            }

        } catch (SQLException ex) {
            throw new RuntimeException("Не вдалося знайти співробітників за посадою: " + pos, ex);
        }

        return list;
    }

    public int getUnusedLeaveDays(int employeeId) {
        String sql = "SELECT annual_days + carryover_days AS total FROM employees WHERE id = ?";
        try (Connection conn = DatabaseManager.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, employeeId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("total");
                }
            }
        } catch (SQLException ex) {
            throw new RuntimeException(
                    "Не вдалося обчислити невикористані дні відпустки для співробітника: " + employeeId, ex);
        }
        return 0;
    }

    public void addVacation(int empId, java.time.LocalDate start, java.time.LocalDate end, String type) {
        String sql = "INSERT INTO vacations (employee_id, start_date, end_date, vacation_type) VALUES (?, ?, ?, ?)";
        try (Connection conn = DatabaseManager.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, empId);
            ps.setDate(2, Date.valueOf(start));
            ps.setDate(3, Date.valueOf(end));
            ps.setString(4, type);

            ps.executeUpdate();
            System.out.println("Vacation successfully registered in DB for employee ID: " + empId);

        } catch (SQLException ex) {
            throw new RuntimeException("Не вдалося додати запис про відпустку для співробітника: " + empId, ex);
        }
    }
}