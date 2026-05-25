package model;

public class Employee {
    private int id;
    private String fullName;
    private String position;
    private double salary;
    private String department;

    public Employee() {
    }

    public Employee(int id, String fullName, String position, double salary, String department) {
        this.id = id;
        this.fullName = fullName;
        this.position = position;
        this.salary = salary;
        this.department = department;
    }

    public Employee(String fullName, String position, double salary, String department) {
        this.fullName = fullName;
        this.position = position;
        this.salary = salary;
        this.department = department;
    }

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

    public double getSalary() {
        return salary;
    }

    public void setSalary(double salary) {
        this.salary = salary;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }
}
