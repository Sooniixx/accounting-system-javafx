package model;

import java.time.LocalDate;

public class Vacation {
    private int id;
    private int employeeId;
    private LocalDate startDate;
    private LocalDate endDate;
    private String vacationType; // 'paid', 'unpaid', 'day_off'

    public Vacation() {
    }

    public Vacation(int id, int employeeId, LocalDate startDate, LocalDate endDate, String vacationType) {
        this.id = id;
        this.employeeId = employeeId;
        this.startDate = startDate;
        this.endDate = endDate;
        this.vacationType = vacationType;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(int employeeId) {
        this.employeeId = employeeId;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public String getVacationType() {
        return vacationType;
    }

    public void setVacationType(String vacationType) {
        this.vacationType = vacationType;
    }
}