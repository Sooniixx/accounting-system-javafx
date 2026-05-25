package service;

import db.DatabaseManager;
import model.Employee;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class EmployeeService {

    // Create (insert)
    public void addEmployee(Employee e) {

        String sql = "INSERT INTO employees (full_name, position, salary, department) VALUES (?, ?, ?, ?)";

        try (Connection conn = DatabaseManager.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, e.getFullName());
            ps.setString(2, e.getPosition());
            ps.setDouble(3, e.getSalary());
            ps.setString(4, e.getDepartment());

            ps.executeUpdate();

        } catch (SQLException ex) {
            throw new RuntimeException("Failed to add employee: " + e.getFullName(), ex);
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
                e.setSalary(rs.getDouble("salary"));
                e.setDepartment(rs.getString("department"));

                list.add(e);
            }

        } catch (SQLException ex) {
            throw new RuntimeException("Failed to retrieve employees", ex);
        }

        return list;
    }

    // Update
    public void updateEmployee(Employee e) {

        String sql = "UPDATE employees SET full_name=?, position=?, salary=?, department=? WHERE id=?";

        try (Connection conn = DatabaseManager.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, e.getFullName());
            ps.setString(2, e.getPosition());
            ps.setDouble(3, e.getSalary());
            ps.setString(4, e.getDepartment());
            ps.setInt(5, e.getId());

            int rowsAffected = ps.executeUpdate();
            if (rowsAffected == 0) {
                throw new RuntimeException("No employee found with ID: " + e.getId());
            }

        } catch (SQLException ex) {
            throw new RuntimeException("Failed to update employee with ID: " + e.getId(), ex);
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
                throw new RuntimeException("No employee found with ID: " + id);
            }

        } catch (SQLException ex) {
            throw new RuntimeException("Failed to delete employee with ID: " + id, ex);
        }
    }

    // Search by department
    public List<Employee> searchByDepartment(String dept) {
        return search("department", dept);
    }

    // Search by position
    public List<Employee> searchByPosition(String pos) {
        return search("position", pos);
    }

    private List<Employee> search(String field, String value) {

        List<Employee> list = new ArrayList<>();

        String sql = "SELECT * FROM employees WHERE " + field + " = ?";

        try (Connection conn = DatabaseManager.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, value);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {

                    Employee e = new Employee();

                    e.setId(rs.getInt("id"));
                    e.setFullName(rs.getString("full_name"));
                    e.setPosition(rs.getString("position"));
                    e.setSalary(rs.getDouble("salary"));
                    e.setDepartment(rs.getString("department"));

                    list.add(e);
                }
            }

        } catch (SQLException ex) {
            throw new RuntimeException("Failed to search employees by " + field + ": " + value, ex);
        }

        return list;
    }
}
