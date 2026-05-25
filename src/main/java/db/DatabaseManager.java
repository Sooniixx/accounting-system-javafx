package db;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import java.util.Properties;
import java.util.stream.Collectors;

public class DatabaseManager {
    private static final Properties properties = new Properties();
    private static boolean initialized = false;

    static {
        init();
    }

    private static void init() {
        if (initialized)
            return;
        initialized = true;

        try (InputStream propStream = DatabaseManager.class.getClassLoader().getResourceAsStream("db.properties")) {
            if (propStream == null) {
                throw new RuntimeException("Unable to find db.properties");
            }
            properties.load(propStream);

            try (InputStream schemaStream = DatabaseManager.class.getClassLoader().getResourceAsStream("schema.sql")) {
                if (schemaStream == null) {
                    throw new RuntimeException("Unable to find schema.sql");
                }
                String schemaSql = new BufferedReader(new InputStreamReader(schemaStream))
                        .lines()
                        .collect(Collectors.joining("\n"));

                try (Connection conn = getConnection();
                        Statement stmt = conn.createStatement()) {
                    stmt.execute(schemaSql);
                }
            }

        } catch (Exception e) {
            throw new RuntimeException("Database initialization failed", e);
        }
    }

    public static Connection getConnection() {
        try {
            return DriverManager.getConnection(
                    properties.getProperty("db.url"),
                    properties.getProperty("db.user"),
                    properties.getProperty("db.password"));

        } catch (Exception e) {
            throw new RuntimeException("Failed to connect to the database", e);
        }
    }

}