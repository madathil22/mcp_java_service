package com.example.mcpjavaservice.employee.department;

import com.example.mcpjavaservice.department.Department;
import com.example.mcpjavaservice.employee.Employee;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MapsId;
import jakarta.persistence.Table;

@Entity
@Table(name = "employee_department")
public class EmployeeDepartment {

    @EmbeddedId
    private EmployeeDepartmentId id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @MapsId("employeeId")
    @JoinColumn(name = "employee_id")
    private Employee employee;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @MapsId("departmentId")
    @JoinColumn(name = "department_id")
    private Department department;

    public EmployeeDepartmentId getId() {
        return id;
    }

    public void setId(EmployeeDepartmentId id) {
        this.id = id;
    }

    public Employee getEmployee() {
        return employee;
    }

    public void setEmployee(Employee employee) {
        this.employee = employee;
    }

    public Department getDepartment() {
        return department;
    }

    public void setDepartment(Department department) {
        this.department = department;
    }
}
