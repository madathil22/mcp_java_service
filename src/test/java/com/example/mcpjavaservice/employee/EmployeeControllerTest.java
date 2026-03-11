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
}
