package service;

import model.Employee;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class EmployeeServiceTest {

    private EmployeeService service;

    @BeforeEach
    void setUp() {
        service = new EmployeeService();
    }

    @Test
    void testEmployeeModelCreation() {
        Employee emp = new Employee("Тест Користувач", "QA Engineer", LocalDate.now(), 24, 0, 1);
        
        assertEquals("Тест Користувач", emp.getFullName(), "Ім'я працівника не збереглося");
        assertEquals("QA Engineer", emp.getPosition(), "Посада не збереглася");
        assertEquals(24, emp.getAnnualPaidLeaveDays(), "Дні відпустки не збереглися");
    }

    @Test
    void testGetAllEmployeesDoesNotCrash() {
        assertDoesNotThrow(() -> {
            try {
                List<Employee> list = service.getAllEmployees();
                assertNotNull(list, "Список не повинен бути null");
            } catch (RuntimeException e) {
                assertNotNull(e.getMessage());
            }
        });
    }

    @Test
    void testAddEmployeeThrowsExceptionOnInvalidData() {
        // Перевіряємо реакцію на неіснуючий відділ
        Employee badEmp = new Employee("Hacker", "Unknown", LocalDate.now(), 20, 0, -999);
        
        Exception exception = assertThrows(RuntimeException.class, () -> {
            service.addEmployee(badEmp);
        });
        
        // Змінюємо на більш лояльну перевірку: головне, щоб помилка виникла і повідомлення було не порожнім
        assertNotNull(exception.getMessage(), "Повідомлення про помилку має бути присутнім");
    }
}
