package com.jp.springboot.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jp.springboot.model.Employee;
import com.jp.springboot.repository.EmployeeRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.CoreMatchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willDoNothing;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
public class EmployeeControllerITest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    public void setup(){
        employeeRepository.deleteAll();
    }
    // Integration test for createEmployee method
    @DisplayName("Integration test of createEmployee method")
    @Test
    public void givenEmployeeObject_whenCreateEmployee_thenReturnSavedEmployee() throws Exception {
        // given - precondition or setup

        Employee employee = Employee.builder()
                .firstName("Jashobanta")
                .lastName("Patra")
                .email("jashobanta@gmail.com")
                .build();


        // when - action or the behaviour that we are going test

        ResultActions response = mockMvc.perform(post("/api/employees")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(employee)));
        // then - verify the output

        response.andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.firstName", is(employee.getFirstName())))
                .andExpect(jsonPath("$.lastName", is(employee.getLastName())))
                .andExpect(jsonPath("$.email", is(employee.getEmail())));

    }

    // Integration test for getAllEmployees method
    @DisplayName("Integration test for getAllEmployees method")
    @Test
    public void givenListOfEmployees_whenGetAllEmployees_thenReturnEmployeesList() throws Exception {
        // given - precondition or setup

        List<Employee> employeeList = new ArrayList<>();

        employeeList.add(Employee.builder().firstName("Jashobanta").lastName("Patra").email("jasho@gmail.com").build());
        employeeList.add(Employee.builder().firstName("Anuradha").lastName("Behura").email("anuradha@gmail.com").build());

        employeeRepository.saveAll(employeeList);

        // when - action or the behaviour that we are going test

        ResultActions response = mockMvc.perform(get("/api/employees"));

        // then - verify the output
        response.andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$.size()", is(employeeList.size())));

    }
    // Junit test for getEmployeeById method - (positive scenario with valid employeeid)
    @DisplayName("Junit test for getEmployeeById method - (positive scenario with valid employeeid)")
    @Test
    public void givenValidEmployeeId_whenGetEmployeeById_thenReturnsEmployeeObject() throws Exception {
        // given - precondition or setup

        Employee employee = Employee.builder()
                .firstName("Jashobanta")
                .lastName("Patra")
                .email("jashobanta@gmail.com")
                .build();
        employeeRepository.save(employee);


        // when - action or the behaviour that we are going test

        ResultActions response = mockMvc.perform(get("/api/employees/{id}",employee.getId()));
        // then - verify the output
        response.andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$.firstName",is(employee.getFirstName())))
                .andExpect(jsonPath("$.lastName", is(employee.getLastName())))
                .andExpect(jsonPath("$.email",is(employee.getEmail())));

    }

    // Junit test for getEmployeeById method - (negative scenario with valid employeeid)
    @DisplayName("Junit test for getEmployeeById method - (positive scenario with valid employeeid)")
    @Test
    public void givenInvalidEmployeeId_whenGetEmployeeById_thenReturnsEmployeeObject() throws Exception {
        // given - precondition or setup

        long employeeId = 1L;
        Employee employee = Employee.builder()
                .firstName("Jashobanta")
                .lastName("Patra")
                .email("jashobanta@gmail.com")
                .build();
        employeeRepository.save(employee);

        // when - action or the behaviour that we are going test

        ResultActions response = mockMvc.perform(get("/api/employees/{id}",employeeId));
        // then - verify the output
        response.andExpect(status().isNotFound())
                .andDo(print());
    }

    // Junit test for updateEmployee method - positive scenario
    @DisplayName("Junit test for updateEmployee method - positive scenario")
    @Test
    public void givenUpdatedEmployee_whenUpdateEmployee_thenReturnUpdatedEmployee() throws Exception {
        // given - precondition or setup

        Employee savedEmployee = Employee.builder()
                .firstName("Jashobanta")
                .lastName("Patra")
                .email("jashobanta@gmail.com")
                .build();

        employeeRepository.save(savedEmployee);

        Employee updatedEmployee = Employee.builder()
                .firstName("Jashobanta")
                .lastName("Patra")
                .email("anuradha@gmail.com")
                .build();


        // when - action or the behaviour that we are going test

        ResultActions response = mockMvc.perform(put("/api/employees/{id}", savedEmployee.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updatedEmployee)));

        // then - verify the output
        response.andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$.firstName",is(updatedEmployee.getFirstName())))
                .andExpect(jsonPath("$.lastName",is(updatedEmployee.getLastName())))
                .andExpect(jsonPath("$.email", is(updatedEmployee.getEmail())));

    }

    // Junit test for updateEmployee method - negative scenario
    @DisplayName("Junit test for updateEmployee method - positive scenario")
    @Test
    public void givenUpdatedEmployee_whenUpdateEmployee_thenReturn404() throws Exception {
        // given - precondition or setup

        long employeeId=1L;

        Employee savedEmployee = Employee.builder()
                .firstName("Jashobanta")
                .lastName("Patra")
                .email("jashobanta@gmail.com")
                .build();

        employeeRepository.save(savedEmployee);

        Employee updatedEmployee = Employee.builder()
                .firstName("Jashobanta")
                .lastName("Patra")
                .email("anuradha@gmail.com")
                .build();


        // when - action or the behaviour that we are going test

        ResultActions response = mockMvc.perform(put("/api/employees/{id}", employeeId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updatedEmployee)));

        // then - verify the output
        response.andExpect(status().isNotFound())
                .andDo(print());

    }

    // Junit test for deleteEmployee method
    @DisplayName("Junit test for deleteEmployee method")
    @Test
    public void givenEmployeeId_whenDeleteEmployee_thenReturn200() throws Exception {
        // given - precondition or setup

        Employee savedEmployee = Employee.builder()
                .firstName("Jashobanta")
                .lastName("Patra")
                .email("jashobanta@gmail.com")
                .build();

       employeeRepository.save(savedEmployee);

        // when - action or the behaviour that we are going test
        ResultActions response = mockMvc.perform(delete("/api/employees/{id}", savedEmployee.getId()));

        // then - verify the output
        response.andExpect(status().isOk())
                .andDo(print());

    }
}
