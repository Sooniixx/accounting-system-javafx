import db.DatabaseManager;

public class Main {
    public static void main(String[] args) {
        try {
            DatabaseManager.getConnection();
            System.out.println("Database connection successful!");
        } catch (Exception e) {
            e.printStackTrace();
        }
        javafx.application.Application.launch(service.MainApp.class, args);
    }
}