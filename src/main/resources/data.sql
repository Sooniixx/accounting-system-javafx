-- Додавання даних для тестування
-- Спочатку додаємо відділи, якщо вони ще не існують
INSERT INTO departments (name)
VALUES ('IT');

INSERT INTO departments (name)
VALUES ('HR');

INSERT INTO departments (name)
VALUES ('Finance');

INSERT INTO departments (name)
VALUES ('Marketing');


-- Тепер додаємо працівників, якщо вони ще не існують
INSERT INTO employees (full_name, position, hire_date, annual_days, department_id, carryover_days)
VALUES ('Ivan Petrenko', 'Developer', '2023-06-01', 24, 1, 5);

INSERT INTO employees (full_name, position, hire_date, annual_days, department_id, carryover_days)
VALUES ('Olha Kovalenko', 'HR Manager', '2022-03-15', 28, 2, 2);

INSERT INTO employees (full_name, position, hire_date, annual_days, department_id, carryover_days)
VALUES ('Andrii Shevchenko', 'Accountant', '2021-11-10', 30, 3, 0);

INSERT INTO employees (full_name, position, hire_date, annual_days, department_id, carryover_days)
VALUES ('Maria Bondar', 'Marketing Specialist', '2024-01-20', 24, 4, 3);

-- Додаємо відпустки, якщо вони ще не існують
INSERT INTO vacations (employee_id, start_date, end_date, vacation_type)
VALUES (1, '2025-07-01', '2025-07-14', 'Annual');

INSERT INTO vacations (employee_id, start_date, end_date, vacation_type)
VALUES (2, '2025-08-10', '2025-08-20', 'Sick');

INSERT INTO vacations (employee_id, start_date, end_date, vacation_type)
VALUES (1, '2025-12-20', '2025-12-27', 'Unpaid');