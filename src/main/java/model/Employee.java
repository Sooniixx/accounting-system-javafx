package model;

import java.time.LocalDate;

public class Employee {
    private int id;
    private String fullName;
    private String position;
    private LocalDate hireDate;
    private int annualPaidLeaveDays;
    private int carryoverPaidLeaveDays;
    private int departmentId;

    // Порожній конструктор
    public Employee() {
    }

    // Повний конструктор
    public Employee(int id, String fullName, String position, LocalDate hireDate,
            int annualPaidLeaveDays, int carryoverPaidLeaveDays, int departmentId) {
        this.id = id;
        this.fullName = fullName;
        this.position = position;
        this.hireDate = hireDate;
        this.annualPaidLeaveDays = annualPaidLeaveDays;
        this.carryoverPaidLeaveDays = carryoverPaidLeaveDays;
        this.departmentId = departmentId;
    }

    // Конструктор без ID (для додавання нового співробітника)
    public Employee(String fullName, String position, LocalDate hireDate,
            int annualPaidLeaveDays, int carryoverPaidLeaveDays, int departmentId) {
        this.fullName = fullName;
        this.position = position;
        this.hireDate = hireDate;
        this.annualPaidLeaveDays = annualPaidLeaveDays;
        this.carryoverPaidLeaveDays = carryoverPaidLeaveDays;
        this.departmentId = departmentId;
    }

    // Гетери та сетери
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public LocalDate getHireDate() {
        return hireDate;
    }

    public void setHireDate(LocalDate hireDate) {
        this.hireDate = hireDate;
    }

    public int getAnnualPaidLeaveDays() {
        return annualPaidLeaveDays;
    }

    public void setAnnualPaidLeaveDays(int annualPaidLeaveDays) {
        this.annualPaidLeaveDays = annualPaidLeaveDays;
    }

    public int getCarryoverPaidLeaveDays() {
        return carryoverPaidLeaveDays;
    }

    public void setCarryoverPaidLeaveDays(int carryoverPaidLeaveDays) {
        this.carryoverPaidLeaveDays = carryoverPaidLeaveDays;
    }

    public int getDepartmentId() {
        return departmentId;
    }

    public void setDepartmentId(int departmentId) {
        this.departmentId = departmentId;
    }

    @Override
    public String toString() {
        return "Employee{" + "id=" + id + ", fullName='" + fullName + '\'' + ", position='" + position + '\'' + '}';
    }
}