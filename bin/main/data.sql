INSERT INTO department (department_id, department_name) VALUES
    (10, 'Engineering'),
    (20, 'Human Resources'),
    (30, 'Finance');

INSERT INTO employee (employee_id, name, age, gender) VALUES
    (1, 'Alice Johnson', 29, 'Female'),
    (2, 'Brian Smith', 35, 'Male'),
    (3, 'Carla Gomez', 41, 'Female');

INSERT INTO employee_department (employee_id, department_id) VALUES
    (1, 10),
    (2, 10),
    (2, 30),
    (3, 20);
