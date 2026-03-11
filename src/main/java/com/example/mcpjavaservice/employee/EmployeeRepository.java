package com.example.mcpjavaservice.employee;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface EmployeeRepository extends JpaRepository<Employee, Long> {

    @Override
    @EntityGraph(attributePaths = {"employeeDepartments", "employeeDepartments.department"})
    List<Employee> findAll();

    @Override
    @EntityGraph(attributePaths = {"employeeDepartments", "employeeDepartments.department"})
    Optional<Employee> findById(Long id);

    @EntityGraph(attributePaths = {"employeeDepartments", "employeeDepartments.department"})
    @Query("""
        select distinct e
        from Employee e
        left join e.employeeDepartments ed
        left join ed.department d
        where lower(d.departmentName) = lower(:departmentName)
        order by e.employeeId
        """)
    List<Employee> findAllByDepartmentNameIgnoreCase(@Param("departmentName") String departmentName);

    @EntityGraph(attributePaths = {"employeeDepartments", "employeeDepartments.department"})
    @Query("""
        select distinct e
        from Employee e
        left join e.employeeDepartments ed
        where ed.department.departmentId = :departmentId
        order by e.employeeId
        """)
    List<Employee> findAllByDepartmentId(@Param("departmentId") Long departmentId);
}
