package com.example.mcpjavaservice.employee;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class EmployeeControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void shouldReturnEmployeesWithDepartments() throws Exception {
        mockMvc.perform(get("/api/getallemployees"))
            .andExpect(status().isOk())
            .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$[0].employeeId").value(1))
            .andExpect(jsonPath("$[0].departments[0].departmentName").value("Engineering"))
            .andExpect(jsonPath("$[1].employeeId").value(2))
            .andExpect(jsonPath("$[1].departments[1].departmentName").value("Finance"))
            .andExpect(jsonPath("$[2].employeeId").value(3))
            .andExpect(jsonPath("$[2].departments[0].departmentName").value("Human Resources"));
    }

    @Test
    void shouldReturnDepartmentsWithEmployees() throws Exception {
        mockMvc.perform(get("/api/getalldepartments"))
            .andExpect(status().isOk())
            .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$[0].departmentId").value(10))
            .andExpect(jsonPath("$[0].departmentName").value("Engineering"))
            .andExpect(jsonPath("$[1].departmentId").value(20))
            .andExpect(jsonPath("$[1].departmentName").value("Human Resources"))
            .andExpect(jsonPath("$[2].departmentId").value(30))
            .andExpect(jsonPath("$[2].departmentName").value("Finance"));
    }

    @Test
    void shouldReturnDepartmentById() throws Exception {
        mockMvc.perform(get("/api/departments/10"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.departmentId").value(10))
            .andExpect(jsonPath("$.departmentName").value("Engineering"));
    }

    @Test
    void shouldReturnDepartmentByNameCaseInsensitive() throws Exception {
        mockMvc.perform(get("/api/departments/name/engineering"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.departmentId").value(10))
            .andExpect(jsonPath("$.departmentName").value("Engineering"));
    }

    @Test
    void shouldReturnEmployeesByDepartmentId() throws Exception {
        mockMvc.perform(get("/api/departments/10/employees"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$[0].employeeId").value(1))
            .andExpect(jsonPath("$[1].employeeId").value(2));
    }

    @Test
    void shouldReturnEmployeeById() throws Exception {
        mockMvc.perform(get("/api/employees/2"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.employeeId").value(2))
            .andExpect(jsonPath("$.name").value("Brian Smith"));
    }

    @Test
    void shouldReturnEmployeesByDepartmentName() throws Exception {
        mockMvc.perform(get("/api/employees/by-department/engineering"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$[0].employeeId").value(1))
            .andExpect(jsonPath("$[1].employeeId").value(2));
    }

    @Test
    void shouldReturn404ForMissingDepartment() throws Exception {
        mockMvc.perform(get("/api/departments/999"))
            .andExpect(status().isNotFound())
            .andExpect(jsonPath("$.status").value(404))
            .andExpect(jsonPath("$.error").value("Not Found"))
            .andExpect(jsonPath("$.message").value("Department not found with id: 999"))
            .andExpect(jsonPath("$.path").value("/api/departments/999"))
            .andExpect(jsonPath("$.timestamp").exists());
    }

    @Test
    void shouldReturn404ForMissingEmployee() throws Exception {
        mockMvc.perform(get("/api/employees/999"))
            .andExpect(status().isNotFound())
            .andExpect(jsonPath("$.status").value(404))
            .andExpect(jsonPath("$.error").value("Not Found"))
            .andExpect(jsonPath("$.message").value("Employee not found with id: 999"))
            .andExpect(jsonPath("$.path").value("/api/employees/999"))
            .andExpect(jsonPath("$.timestamp").exists());
    }
}
