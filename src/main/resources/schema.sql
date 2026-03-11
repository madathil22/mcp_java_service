CREATE TABLE department (
    department_id BIGINT PRIMARY KEY,
    department_name VARCHAR(100) NOT NULL
);

CREATE TABLE employee (
    employee_id BIGINT PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    age INTEGER NOT NULL,
    gender VARCHAR(20) NOT NULL
);

CREATE TABLE employee_department (
    employee_id BIGINT NOT NULL,
    department_id BIGINT NOT NULL,
    PRIMARY KEY (employee_id, department_id),
    CONSTRAINT fk_employee_department_employee
        FOREIGN KEY (employee_id) REFERENCES employee (employee_id),
    CONSTRAINT fk_employee_department_department
        FOREIGN KEY (department_id) REFERENCES department (department_id)
);
