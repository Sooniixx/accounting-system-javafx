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
    private static String url;
    private static String user;
    private static String password;

    static {
        try (InputStream input = DatabaseManager.class.getClassLoader().getResourceAsStream("db.properties")) {
            Properties prop = new Properties();
            if (input == null) {
                throw new RuntimeException("Не вдалося знайти db.properties");
            }
            prop.load(input);

            Class.forName(prop.getProperty("db.driver", "org.h2.Driver"));
            url = prop.getProperty("db.url");
            user = prop.getProperty("db.user", "sa");
            password = prop.getProperty("db.password", "");

            try (InputStream schemaStream = DatabaseManager.class.getClassLoader().getResourceAsStream("schema.sql")) {

                if (schemaStream == null) {
                    throw new RuntimeException("Не вдалося знайти schema.sql");
                }

                String sql = new BufferedReader(
                        new InputStreamReader(schemaStream))
                        .lines()
                        .collect(Collectors.joining("\n"));

                // прибрати коментарі
                sql = sql.replaceAll("--.*", "");

                // нормалізація
                sql = sql.replace("\r", " ");

                try (Connection conn = DriverManager.getConnection(url, user, password);
                        Statement stmt = conn.createStatement()) {

                    stmt.execute(sql);

                    System.out.println("Схема виконана успішно!");
                }

            }

            try (InputStream dataStream = DatabaseManager.class.getClassLoader().getResourceAsStream("data.sql")) {

                if (dataStream == null) {
                    throw new RuntimeException("Не вдалося знайти data.sql");
                }

                String dataSql = new BufferedReader(new InputStreamReader(dataStream))
                        .lines()
                        .collect(Collectors.joining("\n"));

                dataSql = dataSql.replaceAll("--.*", "");
                dataSql = dataSql.replace("\r", " ");

                try (Connection conn = DriverManager.getConnection(url, user, password);
                        Statement stmt = conn.createStatement()) {

                    stmt.execute(dataSql);

                    System.out.println("Дані завантажено успішно!");
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Не вдалося ініціалізувати конфігурацію бази даних", e);
        }
    }

    public static Connection getConnection() throws java.sql.SQLException {
        return DriverManager.getConnection(url, user, password);
    }
}