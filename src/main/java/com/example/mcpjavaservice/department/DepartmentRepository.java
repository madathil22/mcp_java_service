package com.example.mcpjavaservice.department;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

public interface DepartmentRepository extends JpaRepository<Department, Long>, JpaSpecificationExecutor<Department> {

    @Override
    @EntityGraph(attributePaths = {"employeeDepartments", "employeeDepartments.employee"})
    List<Department> findAll();

    @Override
    @EntityGraph(attributePaths = {"employeeDepartments", "employeeDepartments.employee"})
    List<Department> findAll(Sort sort);

    @Override
    @EntityGraph(attributePaths = {"employeeDepartments", "employeeDepartments.employee"})
    List<Department> findAll(Specification<Department> spec, Sort sort);
}
