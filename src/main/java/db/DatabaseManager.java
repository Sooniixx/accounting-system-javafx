package db;

import java.sql.Connection;
import java.sql.DriverManager;

public class DatabaseManager {
    private static final String URL = "jdbc:h2:./database/accounting_db";

    private static final String USER = "sa";

    private static final String PASSWORD = "";

    public static Connection getConnection() {
        try {
            return DriverManager.getConnection(URL, USER, PASSWORD);

        } catch (Exception e) {
            throw new RuntimeException("Failed to connect to the database", e);
        }
    }

}