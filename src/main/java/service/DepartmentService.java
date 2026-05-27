package service;

import db.DatabaseManager;
import model.Department;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DepartmentService {
    public void addDepartment(String name) {
        String sql = "INSERT INTO departments (name) VALUES (?)";
        try (Connection conn = DatabaseManager.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, name);
            ps.executeUpdate();
        } catch (SQLException e) { throw new RuntimeException("Помилка додавання відділу", e); }
    }

    public List<Department> getAllDepartments() {
        List<Department> list = new ArrayList<>();
        String sql = "SELECT * FROM departments";
        try (Connection conn = DatabaseManager.getConnection(); ResultSet rs = conn.prepareStatement(sql).executeQuery()) {
            while (rs.next()) list.add(new Department(rs.getInt("id"), rs.getString("name"), (Integer) rs.getObject("manager_id")));
        } catch (SQLException e) { throw new RuntimeException("Помилка завантаження відділів", e); }
        return list;
    }
}