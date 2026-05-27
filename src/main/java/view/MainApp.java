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
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.time.LocalDate;

public class MainApp extends Application {

    private MainController controller;
    private TableView<Employee> table;
    private ObservableList<Employee> employeeList;

    private TextField nameField, positionField, annualDaysField, deptIdField, carryoverDaysField;
    private DatePicker hireDatePicker;

    @Override
    public void init() {
        if (controller == null) {
            controller = new MainController();
        }
    }

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Система Бухгалтерії - PRO Версія");

        table = new TableView<>();
        setupTable();

        // Вкладка 1: Працівники (твій старий root)
        BorderPane empTabContent = new BorderPane();
        empTabContent.setCenter(table); empTabContent.setLeft(createFormBox()); empTabContent.setTop(createSearchBox());
        Tab empTab = new Tab("Працівники", empTabContent);

        // Вкладка 2: Відділи
        VBox deptBox = new VBox(10); deptBox.setPadding(new Insets(20));
        TextField deptName = new TextField(); deptName.setPromptText("Назва відділу");
        Button addDeptBtn = new Button("Додати відділ");
        ListView<String> deptList = new ListView<>();

        addDeptBtn.setOnAction(e -> {
            controller.addDepartment(deptName.getText());
            deptList.getItems().setAll(controller.getAllDepartments().stream().map(d -> d.getId() + ": " + d.getName()).toList());
        });

        deptBox.getChildren().addAll(new Label("Назва:"), deptName, addDeptBtn, new Label("Список відділів:"), deptList);
        Tab deptTab = new Tab("Відділи", deptBox);

        TabPane tabPane = new TabPane(empTab, deptTab);
        primaryStage.setScene(new Scene(tabPane, 1100, 650));
        primaryStage.show();
    }

    private void setupTable() {
        TableColumn<Employee, Integer> idCol = new TableColumn<>("ID"); idCol.setCellValueFactory(new PropertyValueFactory<>("id")); idCol.setPrefWidth(50);
        TableColumn<Employee, String> nameCol = new TableColumn<>("ПІБ"); nameCol.setCellValueFactory(new PropertyValueFactory<>("fullName")); nameCol.setPrefWidth(200);
        TableColumn<Employee, String> posCol = new TableColumn<>("Посада"); posCol.setCellValueFactory(new PropertyValueFactory<>("position")); posCol.setPrefWidth(150);
        TableColumn<Employee, LocalDate> dateCol = new TableColumn<>("Дата прийому"); dateCol.setCellValueFactory(new PropertyValueFactory<>("hireDate")); dateCol.setPrefWidth(120);
        TableColumn<Employee, Integer> annualCol = new TableColumn<>("Дні відпустки"); annualCol.setCellValueFactory(new PropertyValueFactory<>("annualPaidLeaveDays")); annualCol.setPrefWidth(100);
        TableColumn<Employee, Integer> deptCol = new TableColumn<>("ID Відділу"); deptCol.setCellValueFactory(new PropertyValueFactory<>("departmentId")); deptCol.setPrefWidth(80);
        TableColumn<Employee, Integer> carryCol = new TableColumn<>("Перенесені дні"); carryCol.setCellValueFactory(new PropertyValueFactory<>("carryoverPaidLeaveDays")); carryCol.setPrefWidth(120);

        table.getColumns().addAll(idCol, nameCol, posCol, dateCol, annualCol, deptCol, carryCol);

        table.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                nameField.setText(newSelection.getFullName()); positionField.setText(newSelection.getPosition());
                hireDatePicker.setValue(newSelection.getHireDate()); annualDaysField.setText(String.valueOf(newSelection.getAnnualPaidLeaveDays()));
                deptIdField.setText(String.valueOf(newSelection.getDepartmentId())); carryoverDaysField.setText(String.valueOf(newSelection.getCarryoverPaidLeaveDays()));
            }
        });
    }

    private VBox createFormBox() {
        VBox box = new VBox(10); box.setPadding(new Insets(10)); box.setStyle("-fx-background-color: #f4f4f4; -fx-border-color: #cccccc; -fx-border-width: 0 1 0 0;"); box.setPrefWidth(280);

        Label titleLabel = new Label("Керування даними"); titleLabel.setStyle("-fx-font-weight: bold; -fx-font-size: 14px;");

        nameField = new TextField(); nameField.setPromptText("ПІБ");
        positionField = new TextField(); positionField.setPromptText("Посада");
        hireDatePicker = new DatePicker(); hireDatePicker.setPromptText("Дата прийому"); hireDatePicker.setMaxWidth(Double.MAX_VALUE);
        annualDaysField = new TextField(); annualDaysField.setPromptText("Днів щорічної відпустки");
        deptIdField = new TextField(); deptIdField.setPromptText("ID Відділу (напр. 1)");
        carryoverDaysField = new TextField(); carryoverDaysField.setPromptText("Перенесені дні");

        Button addButton = new Button("Додати"); addButton.setMaxWidth(Double.MAX_VALUE); addButton.setOnAction(e -> addEmployee());
        Button updateButton = new Button("Оновити обраного"); updateButton.setMaxWidth(Double.MAX_VALUE); updateButton.setOnAction(e -> updateEmployee());
        Button deleteButton = new Button("Видалити обраного"); deleteButton.setMaxWidth(Double.MAX_VALUE); deleteButton.setStyle("-fx-base: #ff6666;"); deleteButton.setOnAction(e -> deleteEmployee());

        Button vacationButton = new Button("Оформити відпустку"); vacationButton.setMaxWidth(Double.MAX_VALUE); vacationButton.setStyle("-fx-base: #ffcc66; -fx-font-weight: bold;"); vacationButton.setOnAction(e -> openVacationDialog());
        Button analyticsButton = new Button("Аналітика: Залишок відпустки"); analyticsButton.setMaxWidth(Double.MAX_VALUE); analyticsButton.setStyle("-fx-base: #66b3ff; -fx-font-weight: bold;"); analyticsButton.setOnAction(e -> checkUnusedLeave());
        Button clearButton = new Button("Очистити форму"); clearButton.setMaxWidth(Double.MAX_VALUE); clearButton.setOnAction(e -> clearForm());

        box.getChildren().addAll(titleLabel, nameField, positionField, hireDatePicker, annualDaysField, deptIdField, carryoverDaysField,
                new Separator(), addButton, updateButton, deleteButton, new Separator(), vacationButton, analyticsButton, new Separator(), clearButton);
        return box;
    }

    private HBox createSearchBox() {
        HBox box = new HBox(10); box.setPadding(new Insets(10)); box.setStyle("-fx-background-color: #e8e8e8; -fx-border-color: #cccccc; -fx-border-width: 0 0 1 0;");
        TextField searchDeptField = new TextField(); searchDeptField.setPromptText("ID відділу"); Button searchDeptBtn = new Button("Знайти"); searchDeptBtn.setOnAction(e -> searchByDepartment(searchDeptField.getText()));
        TextField searchPosField = new TextField(); searchPosField.setPromptText("Посада"); Button searchPosBtn = new Button("Знайти"); searchPosBtn.setOnAction(e -> searchByPosition(searchPosField.getText()));
        Button loadAllBtn = new Button("Показати всіх"); loadAllBtn.setOnAction(e -> refreshTable());
        box.getChildren().addAll(new Label("Пошук за ID відділу:"), searchDeptField, searchDeptBtn, new Separator(), new Label("За посадою:"), searchPosField, searchPosBtn, new Separator(), loadAllBtn);
        return box;
    }

    // --- ВІКНО ОФОРМЛЕННЯ ВІДПУСТКИ ---
    private void openVacationDialog() {
        Employee selected = table.getSelectionModel().getSelectedItem();
        if (selected == null) { showError("Помилка", "Оберіть співробітника у таблиці!"); return; }

        Stage dialog = new Stage(); dialog.initModality(Modality.APPLICATION_MODAL); dialog.setTitle("Оформлення відпустки");
        VBox dialogVbox = new VBox(10); dialogVbox.setPadding(new Insets(20));

        DatePicker startDate = new DatePicker(); startDate.setPromptText("Початок");
        DatePicker endDate = new DatePicker(); endDate.setPromptText("Кінець");
        ComboBox<String> typeBox = new ComboBox<>(FXCollections.observableArrayList("paid", "unpaid", "day_off"));
        typeBox.setValue("paid"); // За замовчуванням оплачувана

        Button saveBtn = new Button("Зберегти");
        saveBtn.setOnAction(e -> {
            try {
                controller.addVacation(selected.getId(), startDate.getValue(), endDate.getValue(), typeBox.getValue());
                dialog.close();
                showInfo("Успіх", "Відпустку успішно додано!");
                checkUnusedLeave(); // Одразу показуємо новий залишок
            } catch (Exception ex) { showError("Помилка", ex.getMessage()); }
        });

        dialogVbox.getChildren().addAll(new Label("Співробітник: " + selected.getFullName()), new Label("Початок:"), startDate, new Label("Кінець:"), endDate, new Label("Тип:"), typeBox, saveBtn);
        dialog.setScene(new Scene(dialogVbox, 300, 250)); dialog.showAndWait();
    }

    private void refreshTable() { try { employeeList = FXCollections.observableArrayList(controller.getAllEmployees()); table.setItems(employeeList); } catch (Exception ex) { showError("Помилка", ex.getMessage()); } }
    private void addEmployee() {
        if (controller == null) { showError("Помилка", "Контролер не ініціалізовано!"); return; }
        try {
            // 1. Отримуємо дані з полів та конвертуємо типи
            String fullName = nameField.getText();
            String position = positionField.getText();
            LocalDate hireDate = hireDatePicker.getValue();
            int annualDays = Integer.parseInt(annualDaysField.getText());
            int deptId = Integer.parseInt(deptIdField.getText());
            int carryoverDays = Integer.parseInt(carryoverDaysField.getText());

            // 2. Викликаємо метод контролера
            controller.addEmployee(fullName, position, hireDate, annualDays, deptId, carryoverDays);

            // 3. Оновлюємо таблицю та очищуємо форму
            refreshTable();
            clearForm();
            showInfo("Успіх", "Співробітника успішно додано!");
        } catch (Exception ex) { showError("Помилка", ex.getMessage()); }
    }
    private void updateEmployee() { Employee s = table.getSelectionModel().getSelectedItem(); if (s != null) { try { controller.updateEmployee(s.getId(), nameField.getText(), positionField.getText(), hireDatePicker.getValue(), Integer.parseInt(annualDaysField.getText()), Integer.parseInt(deptIdField.getText()), Integer.parseInt(carryoverDaysField.getText())); refreshTable(); } catch (Exception ex) { showError("Помилка", ex.getMessage()); } } }
    private void deleteEmployee() { Employee s = table.getSelectionModel().getSelectedItem(); if (s != null) { controller.deleteEmployee(s.getId()); refreshTable(); clearForm(); } }

    private void checkUnusedLeave() {
        Employee selected = table.getSelectionModel().getSelectedItem();
        if (selected == null) { showError("Помилка", "Оберіть співробітника!"); return; }
        try { int remaining = controller.getUnusedLeaveDays(selected.getId()); showInfo("Аналітика відпусток", "Співробітник: " + selected.getFullName() + "\nДоступно днів оплачуваної відпустки: " + remaining); } catch (Exception ex) { showError("Помилка", ex.getMessage()); }
    }

    private void searchByDepartment(String deptIdStr) { try { table.setItems(FXCollections.observableArrayList(controller.searchByDepartmentId(Integer.parseInt(deptIdStr)))); } catch (Exception ex) { showError("Помилка", "ID відділу має бути числом."); } }
    private void searchByPosition(String pos) { try { table.setItems(FXCollections.observableArrayList(controller.searchByPosition(pos))); } catch (Exception ex) { showError("Помилка", ex.getMessage()); } }
    private void clearForm() { nameField.clear(); positionField.clear(); hireDatePicker.setValue(null); annualDaysField.clear(); deptIdField.clear(); carryoverDaysField.clear(); table.getSelectionModel().clearSelection(); }
    private void showError(String h, String c) { Alert a = new Alert(Alert.AlertType.ERROR); a.setHeaderText(h); a.setContentText(c); a.showAndWait(); }
    private void showInfo(String h, String c) { Alert a = new Alert(Alert.AlertType.INFORMATION); a.setHeaderText(h); a.setContentText(c); a.showAndWait(); }
}