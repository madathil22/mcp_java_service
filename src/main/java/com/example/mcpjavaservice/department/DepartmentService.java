package com.example.mcpjavaservice.department;

import com.example.mcpjavaservice.employee.Employee;
import com.example.mcpjavaservice.employee.EmployeeService;
import com.example.mcpjavaservice.graphql.input.DepartmentFilterInput;
import com.example.mcpjavaservice.graphql.input.EmployeeFilterInput;
import com.example.mcpjavaservice.graphql.model.DepartmentEmployeeCount;
import com.example.mcpjavaservice.graphql.model.GraphQlDepartment;
import com.example.mcpjavaservice.graphql.model.GraphQlEmployeeSummary;
import jakarta.persistence.criteria.JoinType;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;

@Service
public class DepartmentService {

    private final DepartmentRepository departmentRepository;
    private final EmployeeService employeeService;

    public DepartmentService(DepartmentRepository departmentRepository, EmployeeService employeeService) {
        this.departmentRepository = departmentRepository;
        this.employeeService = employeeService;
    }

    public List<GraphQlDepartment> findDepartments(DepartmentFilterInput filter) {
        return departmentRepository.findAll(departmentSpecification(filter), Sort.by(Sort.Direction.ASC, "departmentId"))
            .stream()
            .map(department -> toGraphQlDepartment(department, filter))
            .toList();
    }

    public List<DepartmentEmployeeCount> countEmployeesByDepartment(DepartmentFilterInput filter) {
        return departmentRepository.findAll(departmentSpecification(filter), Sort.by(Sort.Direction.ASC, "departmentId"))
            .stream()
            .map(department -> {
                int employeeCount = filteredEmployees(department, filter).size();
                return new DepartmentEmployeeCount(department.getDepartmentId(), department.getDepartmentName(), employeeCount);
            })
            .toList();
    }

    private GraphQlDepartment toGraphQlDepartment(Department department, DepartmentFilterInput filter) {
        List<GraphQlEmployeeSummary> employees = filteredEmployees(department, filter)
            .stream()
            .sorted(Comparator.comparing(Employee::getEmployeeId))
            .map(employee -> new GraphQlEmployeeSummary(
                employee.getEmployeeId(),
                employee.getName(),
                employee.getAge(),
                employee.getGender()
            ))
            .toList();

        return new GraphQlDepartment(
            department.getDepartmentId(),
            department.getDepartmentName(),
            employees.size(),
            employees
        );
    }

    private List<Employee> filteredEmployees(Department department, DepartmentFilterInput filter) {
        EmployeeFilterInput employeeFilter = new EmployeeFilterInput(
            filter != null ? filter.employeeName() : null,
            null,
            filter != null ? filter.employeeGender() : null,
            filter != null ? filter.employeeMinAge() : null,
            filter != null ? filter.employeeMaxAge() : null
        );

        return department.getEmployeeDepartments()
            .stream()
            .map(employeeDepartment -> employeeDepartment.getEmployee())
            .filter(employee -> employeeService.matchesFilter(employee, employeeFilter))
            .toList();
    }

    private Specification<Department> departmentSpecification(DepartmentFilterInput filter) {
        return (root, query, criteriaBuilder) -> {
            query.distinct(true);

            var predicates = criteriaBuilder.conjunction();
            if (filter == null) {
                return predicates;
            }
            if (hasText(filter.departmentName())) {
                predicates = criteriaBuilder.and(
                    predicates,
                    criteriaBuilder.like(criteriaBuilder.lower(root.get("departmentName")), "%" + filter.departmentName().toLowerCase() + "%")
                );
            }
            if (hasText(filter.employeeName()) || hasText(filter.employeeGender()) || filter.employeeMinAge() != null || filter.employeeMaxAge() != null) {
                var employeeJoin = root.join("employeeDepartments", JoinType.LEFT).join("employee", JoinType.LEFT);
                if (hasText(filter.employeeName())) {
                    predicates = criteriaBuilder.and(
                        predicates,
                        criteriaBuilder.like(criteriaBuilder.lower(employeeJoin.get("name")), "%" + filter.employeeName().toLowerCase() + "%")
                    );
                }
                if (hasText(filter.employeeGender())) {
                    predicates = criteriaBuilder.and(
                        predicates,
                        criteriaBuilder.equal(criteriaBuilder.lower(employeeJoin.get("gender")), filter.employeeGender().toLowerCase())
                    );
                }
                if (filter.employeeMinAge() != null) {
                    predicates = criteriaBuilder.and(predicates, criteriaBuilder.greaterThanOrEqualTo(employeeJoin.get("age"), filter.employeeMinAge()));
                }
                if (filter.employeeMaxAge() != null) {
                    predicates = criteriaBuilder.and(predicates, criteriaBuilder.lessThanOrEqualTo(employeeJoin.get("age"), filter.employeeMaxAge()));
                }
            }
            return predicates;
        };
    }

    private boolean hasText(String value) {
        return value != null && !value.isBlank();
    }
}
