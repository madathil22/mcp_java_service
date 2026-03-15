package com.example.mcpjavaservice.employee;

import com.example.mcpjavaservice.department.Department;
import com.example.mcpjavaservice.graphql.input.EmployeeFilterInput;
import com.example.mcpjavaservice.graphql.model.GraphQlDepartmentSummary;
import com.example.mcpjavaservice.graphql.model.GraphQlEmployee;
import jakarta.persistence.criteria.JoinType;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;

@Service
public class EmployeeService {

    private final EmployeeRepository employeeRepository;

    public EmployeeService(EmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
    }

    public List<GraphQlEmployee> findEmployees(EmployeeFilterInput filter) {
        return employeeRepository.findAll(employeeSpecification(filter), Sort.by(Sort.Direction.ASC, "employeeId"))
            .stream()
            .map(this::toGraphQlEmployee)
            .toList();
    }

    public boolean matchesFilter(Employee employee, EmployeeFilterInput filter) {
        if (filter == null) {
            return true;
        }
        if (hasText(filter.nameContains()) && !employee.getName().toLowerCase().contains(filter.nameContains().toLowerCase())) {
            return false;
        }
        if (hasText(filter.gender()) && !employee.getGender().equalsIgnoreCase(filter.gender())) {
            return false;
        }
        if (filter.minAge() != null && employee.getAge() < filter.minAge()) {
            return false;
        }
        if (filter.maxAge() != null && employee.getAge() > filter.maxAge()) {
            return false;
        }
        if (hasText(filter.departmentName()) && employee.getEmployeeDepartments().stream()
            .map(employeeDepartment -> employeeDepartment.getDepartment().getDepartmentName())
            .noneMatch(departmentName -> departmentName.equalsIgnoreCase(filter.departmentName()))) {
            return false;
        }
        return true;
    }

    public GraphQlEmployee toGraphQlEmployee(Employee employee) {
        return new GraphQlEmployee(
            employee.getEmployeeId(),
            employee.getName(),
            employee.getAge(),
            employee.getGender(),
            employee.getEmployeeDepartments()
                .stream()
                .map(employeeDepartment -> employeeDepartment.getDepartment())
                .sorted(Comparator.comparing(Department::getDepartmentId))
                .map(department -> new GraphQlDepartmentSummary(
                    department.getDepartmentId(),
                    department.getDepartmentName()
                ))
                .toList()
        );
    }

    private Specification<Employee> employeeSpecification(EmployeeFilterInput filter) {
        return (root, query, criteriaBuilder) -> {
            query.distinct(true);

            var predicates = criteriaBuilder.conjunction();

            if (filter == null) {
                return predicates;
            }
            if (hasText(filter.nameContains())) {
                predicates = criteriaBuilder.and(
                    predicates,
                    criteriaBuilder.like(criteriaBuilder.lower(root.get("name")), "%" + filter.nameContains().toLowerCase() + "%")
                );
            }
            if (hasText(filter.gender())) {
                predicates = criteriaBuilder.and(
                    predicates,
                    criteriaBuilder.equal(criteriaBuilder.lower(root.get("gender")), filter.gender().toLowerCase())
                );
            }
            if (filter.minAge() != null) {
                predicates = criteriaBuilder.and(predicates, criteriaBuilder.greaterThanOrEqualTo(root.get("age"), filter.minAge()));
            }
            if (filter.maxAge() != null) {
                predicates = criteriaBuilder.and(predicates, criteriaBuilder.lessThanOrEqualTo(root.get("age"), filter.maxAge()));
            }
            if (hasText(filter.departmentName())) {
                var departmentJoin = root.join("employeeDepartments", JoinType.LEFT).join("department", JoinType.LEFT);
                predicates = criteriaBuilder.and(
                    predicates,
                    criteriaBuilder.equal(criteriaBuilder.lower(departmentJoin.get("departmentName")), filter.departmentName().toLowerCase())
                );
            }
            return predicates;
        };
    }

    private boolean hasText(String value) {
        return value != null && !value.isBlank();
    }
}
