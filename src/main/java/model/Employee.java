package model;
import java.time.LocalDate;

public class Employee {
    private int id;
    private String fullName;
    private String position;
    private LocalDate hireDate;
    private int annualPaidLeaveDays;
    private int departmentId;
    private int carryoverPaidLeaveDays;

    public Employee() {}

    public Employee(int id, String fullName, String position, LocalDate hireDate, int annualPaidLeaveDays, int departmentId, int carryoverPaidLeaveDays) {
        this.id = id; this.fullName = fullName; this.position = position;
        this.hireDate = hireDate; this.annualPaidLeaveDays = annualPaidLeaveDays;
        this.departmentId = departmentId; this.carryoverPaidLeaveDays = carryoverPaidLeaveDays;
    }

    // Гетери та сетери
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getFullName() { return fullName; }
    public void setFullName(String fullName) { this.fullName = fullName; }
    public String getPosition() { return position; }
    public void setPosition(String position) { this.position = position; }
    public LocalDate getHireDate() { return hireDate; }
    public void setHireDate(LocalDate hireDate) { this.hireDate = hireDate; }
    public int getAnnualPaidLeaveDays() { return annualPaidLeaveDays; }
    public void setAnnualPaidLeaveDays(int annualPaidLeaveDays) { this.annualPaidLeaveDays = annualPaidLeaveDays; }
    public int getDepartmentId() { return departmentId; }
    public void setDepartmentId(int departmentId) { this.departmentId = departmentId; }
    public int getCarryoverPaidLeaveDays() { return carryoverPaidLeaveDays; }
    public void setCarryoverPaidLeaveDays(int carryoverPaidLeaveDays) { this.carryoverPaidLeaveDays = carryoverPaidLeaveDays; }
}