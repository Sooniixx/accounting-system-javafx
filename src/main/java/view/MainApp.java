package view;

import controller.MainController;
import model.Employee;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.*;
import javafx.stage.Stage;

public class MainApp extends Application {

    private MainController controller;
    private TableView<Employee> table;
    private ObservableList<Employee> employeeList;

    // Поля для форми
    private TextField nameField, positionField, salaryField, departmentField;

    @Override
    public void init() {
        controller = new MainController();
    }

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Інформаційна система бухгалтерії (Варіант 18)");

        // 1. Створення таблиці (Center)
        table = new TableView<>();
        setupTable();

        // 2. Створення форми додавання/редагування (Left)
        VBox formBox = createFormBox();

        // 3. Створення панелі пошуку (Top)
        HBox searchBox = createSearchBox();

        // Основне компонування
        BorderPane root = new BorderPane();
        root.setCenter(table);
        root.setLeft(formBox);
        root.setTop(searchBox);

        Scene scene = new Scene(root, 950, 600);
        primaryStage.setScene(scene);
        primaryStage.show();

        // Завантаження даних при старті
        refreshTable();
    }

    private void setupTable() {
        TableColumn<Employee, Integer> idCol = new TableColumn<>("ID");
        idCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        idCol.setPrefWidth(50);

        TableColumn<Employee, String> nameCol = new TableColumn<>("ПІБ");
        nameCol.setCellValueFactory(new PropertyValueFactory<>("fullName"));
        nameCol.setPrefWidth(200);

        TableColumn<Employee, String> posCol = new TableColumn<>("Посада");
        posCol.setCellValueFactory(new PropertyValueFactory<>("position"));
        posCol.setPrefWidth(150);

        TableColumn<Employee, Double> salaryCol = new TableColumn<>("Зарплата");
        salaryCol.setCellValueFactory(new PropertyValueFactory<>("salary"));
        salaryCol.setPrefWidth(100);

        TableColumn<Employee, String> deptCol = new TableColumn<>("Відділ");
        deptCol.setCellValueFactory(new PropertyValueFactory<>("department"));
        deptCol.setPrefWidth(150);

        table.getColumns().addAll(idCol, nameCol, posCol, salaryCol, deptCol);

        // Обробник кліку по рядку таблиці (для заповнення форми)
        table.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                nameField.setText(newSelection.getFullName());
                positionField.setText(newSelection.getPosition());
                salaryField.setText(String.valueOf(newSelection.getSalary()));
                departmentField.setText(newSelection.getDepartment());
            }
        });
    }

    private VBox createFormBox() {
        VBox box = new VBox(10);
        box.setPadding(new Insets(10));
        box.setStyle("-fx-background-color: #f4f4f4; -fx-border-color: #cccccc; -fx-border-width: 0 1 0 0;");
        box.setPrefWidth(250);

        Label titleLabel = new Label("Керування даними");
        titleLabel.setStyle("-fx-font-weight: bold; -fx-font-size: 14px;");

        nameField = new TextField();
        nameField.setPromptText("ПІБ");

        positionField = new TextField();
        positionField.setPromptText("Посада");

        salaryField = new TextField();
        salaryField.setPromptText("Зарплата");

        departmentField = new TextField();
        departmentField.setPromptText("Відділ");

        Button addButton = new Button("Додати");
        addButton.setMaxWidth(Double.MAX_VALUE);
        addButton.setOnAction(e -> addEmployee());

        Button updateButton = new Button("Оновити обраного");
        updateButton.setMaxWidth(Double.MAX_VALUE);
        updateButton.setOnAction(e -> updateEmployee());

        Button deleteButton = new Button("Видалити обраного");
        deleteButton.setMaxWidth(Double.MAX_VALUE);
        deleteButton.setStyle("-fx-base: #ff6666;");
        deleteButton.setOnAction(e -> deleteEmployee());

        Button clearButton = new Button("Очистити форму");
        clearButton.setMaxWidth(Double.MAX_VALUE);
        clearButton.setOnAction(e -> clearForm());

        box.getChildren().addAll(titleLabel, nameField, positionField, salaryField, departmentField,
                new Separator(), addButton, updateButton, deleteButton, clearButton);
        return box;
    }

    private HBox createSearchBox() {
        HBox box = new HBox(10);
        box.setPadding(new Insets(10));
        box.setStyle("-fx-background-color: #e8e8e8; -fx-border-color: #cccccc; -fx-border-width: 0 0 1 0;");

        TextField searchDeptField = new TextField();
        searchDeptField.setPromptText("Пошук за відділом");
        Button searchDeptBtn = new Button("Знайти");
        searchDeptBtn.setOnAction(e -> searchByDepartment(searchDeptField.getText()));

        TextField searchPosField = new TextField();
        searchPosField.setPromptText("Пошук за посадою");
        Button searchPosBtn = new Button("Знайти");
        searchPosBtn.setOnAction(e -> searchByPosition(searchPosField.getText()));

        Button loadAllBtn = new Button("Скинути пошук (Показати всіх)");
        loadAllBtn.setOnAction(e -> refreshTable());

        box.getChildren().addAll(
                new Label("Відділ:"), searchDeptField, searchDeptBtn,
                new Separator(),
                new Label("Посада:"), searchPosField, searchPosBtn,
                new Separator(),
                loadAllBtn
        );
        return box;
    }

    private void refreshTable() {
        try {
            employeeList = FXCollections.observableArrayList(controller.getAllEmployees());
            table.setItems(employeeList);
        } catch (Exception ex) {
            showError("Помилка завантаження даних", ex.getMessage());
        }
    }

    private void addEmployee() {
        try {
            double salary = Double.parseDouble(salaryField.getText());
            controller.addEmployee(nameField.getText(), positionField.getText(), salary, departmentField.getText());
            refreshTable();
            clearForm();
            showInfo("Успіх", "Співробітника успішно додано!");
        } catch (NumberFormatException ex) {
            showError("Помилка введення", "Зарплата повинна бути числом.");
        } catch (IllegalArgumentException ex) {
            showError("Помилка валідації", ex.getMessage());
        }
    }

    private void updateEmployee() {
        Employee selected = table.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showError("Помилка", "Оберіть співробітника у таблиці для оновлення.");
            return;
        }
        try {
            double salary = Double.parseDouble(salaryField.getText());
            controller.updateEmployee(selected.getId(), nameField.getText(), positionField.getText(), salary, departmentField.getText());
            refreshTable();
            showInfo("Успіх", "Дані співробітника оновлено!");
        } catch (NumberFormatException ex) {
            showError("Помилка введення", "Зарплата повинна бути числом.");
        } catch (IllegalArgumentException ex) {
            showError("Помилка валідації", ex.getMessage());
        }
    }

    private void deleteEmployee() {
        Employee selected = table.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showError("Помилка", "Оберіть співробітника у таблиці для видалення.");
            return;
        }
        try {
            controller.deleteEmployee(selected.getId());
            refreshTable();
            clearForm();
            showInfo("Успіх", "Співробітника видалено!");
        } catch (Exception ex) {
            showError("Помилка видалення", ex.getMessage());
        }
    }

    private void searchByDepartment(String dept) {
        try {
            employeeList = FXCollections.observableArrayList(controller.searchByDepartment(dept));
            table.setItems(employeeList);
            if (employeeList.isEmpty()) showInfo("Пошук", "За відділом '" + dept + "' нічого не знайдено.");
        } catch (IllegalArgumentException ex) {
            showError("Помилка пошуку", ex.getMessage());
        }
    }

    private void searchByPosition(String pos) {
        try {
            employeeList = FXCollections.observableArrayList(controller.searchByPosition(pos));
            table.setItems(employeeList);
            if (employeeList.isEmpty()) showInfo("Пошук", "За посадою '" + pos + "' нічого не знайдено.");
        } catch (IllegalArgumentException ex) {
            showError("Помилка пошуку", ex.getMessage());
        }
    }

    private void clearForm() {
        nameField.clear();
        positionField.clear();
        salaryField.clear();
        departmentField.clear();
        table.getSelectionModel().clearSelection();
    }

    private void showError(String header, String content) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Помилка");
        alert.setHeaderText(header);
        alert.setContentText(content);
        alert.showAndWait();
    }

    private void showInfo(String header, String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Інформація");
        alert.setHeaderText(header);
        alert.setContentText(content);
        alert.showAndWait();
    }
}