package com.example.mcpjavaservice.employee;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

public interface EmployeeRepository extends JpaRepository<Employee, Long>, JpaSpecificationExecutor<Employee> {

    @Override
    @EntityGraph(attributePaths = {"employeeDepartments", "employeeDepartments.department"})
    List<Employee> findAll();

    @Override
    @EntityGraph(attributePaths = {"employeeDepartments", "employeeDepartments.department"})
    List<Employee> findAll(Sort sort);

    @Override
    @EntityGraph(attributePaths = {"employeeDepartments", "employeeDepartments.department"})
    List<Employee> findAll(Specification<Employee> spec, Sort sort);
}
