DROP TABLE IF EXISTS vacations;
ALTER TABLE IF EXISTS departments DROP CONSTRAINT IF EXISTS fk_manager;
DROP TABLE IF EXISTS employees;
DROP TABLE IF EXISTS departments;

CREATE TABLE departments (
                             id INTEGER PRIMARY KEY AUTO_INCREMENT,
                             name VARCHAR(100) NOT NULL UNIQUE,
                             manager_id INTEGER
);

CREATE TABLE employees (
                           id INTEGER PRIMARY KEY AUTO_INCREMENT,
                           full_name VARCHAR(150) NOT NULL,
                           position VARCHAR(100) NOT NULL,
                           hire_date DATE NOT NULL,
                           annual_paid_leave_days INTEGER NOT NULL CHECK (annual_paid_leave_days > 0),
                           department_id INTEGER NOT NULL,
                           carryover_paid_leave_days INTEGER DEFAULT 0,
                           FOREIGN KEY (department_id) REFERENCES departments(id)
);

ALTER TABLE departments ADD CONSTRAINT fk_manager FOREIGN KEY (manager_id) REFERENCES employees(id);

CREATE TABLE vacations (
                           id INTEGER PRIMARY KEY AUTO_INCREMENT,
                           employee_id INTEGER NOT NULL,
                           start_date DATE NOT NULL,
                           end_date DATE NOT NULL,
                           vacation_type VARCHAR(20) NOT NULL CHECK (vacation_type IN ('paid', 'unpaid', 'day_off')),
                           FOREIGN KEY (employee_id) REFERENCES employees(id)
);

INSERT INTO departments (name) VALUES ('Головна Бухгалтерія');