-- Видаляємо старі таблиці, якщо вони випадково залишилися
DROP TABLE IF EXISTS employees;
DROP TABLE IF EXISTS departments;

-- Створюємо таблицю відділів
CREATE TABLE departments (
                             id INT AUTO_INCREMENT PRIMARY KEY,
                             name VARCHAR(255) NOT NULL
);

-- Створюємо таблицю працівників
CREATE TABLE employees (
                           id INT AUTO_INCREMENT PRIMARY KEY,
                           full_name VARCHAR(255) NOT NULL,
                           position VARCHAR(255) NOT NULL,
                           hire_date DATE NOT NULL,
                           annual_days INT NOT NULL,
                           department_id INT NOT NULL,
                           carryover_days INT NOT NULL,
                           FOREIGN KEY (department_id) REFERENCES departments(id) ON DELETE CASCADE
);