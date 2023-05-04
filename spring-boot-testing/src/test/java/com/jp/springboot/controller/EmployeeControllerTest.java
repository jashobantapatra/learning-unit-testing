package com.jp.springboot.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jp.springboot.model.Employee;
import com.jp.springboot.service.EmployeeService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.*;
import static org.hamcrest.CoreMatchers.is;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;

@WebMvcTest
public class EmployeeControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private EmployeeService employeeService;

    @Autowired
    private ObjectMapper objectMapper;

    // Junit test for createEmployee method
    @DisplayName("Junit test for createEmployee method")
     @Test
     public void givenEmployeeObject_whenCreateEmployee_thenReturnSavedEmployee() throws Exception {
         // given - precondition or setup

         Employee employee = Employee.builder()
                 .firstName("Jashobanta")
                 .lastName("Patra")
                 .email("jashobanta@gmail.com")
                 .build();

         given(employeeService.saveEmployee(any(Employee.class)))
                 .willAnswer((invocation) -> invocation.getArgument(0));
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

     // Junit test for getAllEmployees method
      @DisplayName("Junit test for getAllEmployees method")
      @Test
      public void givenListOfEmployees_whenGetAllEmployees_thenReturnEmployeesList() throws Exception {
          // given - precondition or setup

          List<Employee> employeeList = new ArrayList<>();
          employeeList.add(Employee.builder().firstName("Jashobanta").lastName("Patra").email("jasho@gmail.com").build());
          employeeList.add(Employee.builder().firstName("Anuradha").lastName("Behura").email("anuradha@gmail.com").build());
          given(employeeService.getAllEmployees()).willReturn(employeeList);
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

           long employeeId = 1L;
           Employee employee = Employee.builder()
                   .firstName("Jashobanta")
                   .lastName("Patra")
                   .email("jashobanta@gmail.com")
                   .build();
           given(employeeService.getEmployeeById(employeeId)).willReturn(Optional.of(employee));

           // when - action or the behaviour that we are going test

           ResultActions response = mockMvc.perform(get("/api/employees/{id}",employeeId));
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
        given(employeeService.getEmployeeById(employeeId)).willReturn(Optional.empty());

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

         long employeeId = 1L;
         Employee savedEmployee = Employee.builder()
                 .firstName("Jashobanta")
                 .lastName("Patra")
                 .email("jashobanta@gmail.com")
                 .build();

         Employee updatedEmployee = Employee.builder()
                 .firstName("Jashobanta")
                 .lastName("Patra")
                 .email("anuradha@gmail.com")
                 .build();

         given(employeeService.getEmployeeById(employeeId)).willReturn(Optional.of(savedEmployee));

         given(employeeService.updateEmployee(any(Employee.class)))
                 .willAnswer((invocation) -> invocation.getArgument(0));
         // when - action or the behaviour that we are going test

         ResultActions response = mockMvc.perform(put("/api/employees/{id}", employeeId)
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

        long employeeId = 1L;
        Employee savedEmployee = Employee.builder()
                .firstName("Jashobanta")
                .lastName("Patra")
                .email("jashobanta@gmail.com")
                .build();

        Employee updatedEmployee = Employee.builder()
                .firstName("Jashobanta")
                .lastName("Patra")
                .email("anuradha@gmail.com")
                .build();

        given(employeeService.getEmployeeById(employeeId)).willReturn(Optional.empty());

        given(employeeService.updateEmployee(any(Employee.class)))
                .willAnswer((invocation) -> invocation.getArgument(0));
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

            long employeeId = 1L;
            willDoNothing().given(employeeService).deleteEmployee(employeeId);

         // when - action or the behaviour that we are going test
         ResultActions response = mockMvc.perform(delete("/api/employees/{id}", employeeId));

         // then - verify the output
         response.andExpect(status().isOk())
                 .andDo(print());

     }

}
