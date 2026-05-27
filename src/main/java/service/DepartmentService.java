package service;

import db.DatabaseManager;
import model.Department;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class DepartmentService {

    public void addDepartment(String name) {
        String query = "INSERT INTO departments (name) VALUES (?)";

        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, name);
            stmt.executeUpdate();
            System.out.println("Відділ '" + name + "' успішно додано в H2!");

        } catch (Exception e) {
            System.err.println("ПОМИЛКА SQL у DepartmentService: " + e.getMessage());
            e.printStackTrace();

            if (e.getMessage().contains("not found") || e.getMessage().contains("столбец") || e.getMessage().contains("column")) {
                try (Connection conn = DatabaseManager.getConnection();
                     PreparedStatement stmt2 = conn.prepareStatement("INSERT INTO departments (department_name) VALUES (?)")) {
                    stmt2.setString(1, name);
                    stmt2.executeUpdate();
                    System.out.println("Відділ '" + name + "' успішно додано (через стовпець department_name)!");
                    return;
                } catch (Exception ex) {
                    throw new RuntimeException("Failed to add new department: " + name + " (SQL Error: " + ex.getMessage() + ")", ex);
                }
            }
            throw new RuntimeException("Failed to add new department: " + name + " (" + e.getMessage() + ")", e);
        }
    }

    public List<Department> getAllDepartments() {
        List<Department> list = new ArrayList<>();
        String query = "SELECT * FROM departments";

        try (Connection conn = DatabaseManager.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            while (rs.next()) {
                int id = rs.getInt("id");

                String name = "";
                try {
                    name = rs.getString("name");
                } catch (Exception e) {
                    name = rs.getString("department_name");
                }

                // Передаємо третім параметром null, щоб задовольнити конструктор (int, String, Integer)
                list.add(new Department(id, name, null));
            }
        } catch (Exception e) {
            System.err.println("Помилка завантаження відділів: " + e.getMessage());
        }
        return list;
    }
}