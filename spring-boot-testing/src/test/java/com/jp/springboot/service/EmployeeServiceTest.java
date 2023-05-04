package com.jp.springboot.service;

import com.jp.springboot.exception.ResourceNotFoundExceptiion;
import com.jp.springboot.model.Employee;
import com.jp.springboot.repository.EmployeeRepository;
import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willDoNothing;
import static org.mockito.Mockito.*;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
public class EmployeeServiceTest {

    @Mock
    private EmployeeRepository employeeRepository;
    @InjectMocks
    private EmployeeServiceImpl employeeService;

    private Employee employee;

    @BeforeEach
    public void setup(){
        //employeeRepository = Mockito.mock(EmployeeRepository.class);
       // employeeService = new EmployeeServiceImpl(employeeRepository);
        employee = Employee.builder()
                .id(1L)
                .firstName("Jashobanta")
                .lastName("Patra")
                .email("jashobanta@gmail.com")
                .build();
    }
     // Junit test for saveEmployee method
     @DisplayName("Junit test for saveEmployee method")
     @Test
     public void givenEmployeeObject_whenSaveEmployee_thenReturnEmployeeObject(){
     // given - precondition or setup

         given(employeeRepository.findByEmail(employee.getEmail()))
                 .willReturn(Optional.empty());
         given(employeeRepository.save(employee)).willReturn(employee);
     // when - action or the behaviour that we are going test
         Employee savedEmployee = employeeService.saveEmployee(employee);

     // then - verify the output
         assertThat(savedEmployee).isNotNull();

     }
    // Junit test for saveEmployee method which throws exception
    @DisplayName("Junit test for saveEmployee method which throws exception")
    @Test
    public void givenExistingEmail_whenSaveEmployee_thenThrowException(){
        // given - precondition or setup

        given(employeeRepository.findByEmail(employee.getEmail()))
                .willReturn(Optional.of(employee));

        // when - action or the behaviour that we are going test
        Assertions.assertThrows(ResourceNotFoundExceptiion.class,()->{
            employeeService.saveEmployee(employee);
        });

        // then - verify the output
       verify(employeeRepository,never()).save(any(Employee.class));

    }
     // Junit test for getAllEmployees method
     @DisplayName("Junit test for getAllEmployees method")
     @Test
     public void givenEmployeeList_whenGetAllEmployees_thenReturnEmployeesList(){
         // given - precondition or setup

         Employee employee1 = Employee.builder()
                 .id(2L)
                 .firstName("Jasho")
                 .lastName("Patra")
                 .email("jasho@gmail.com")
                 .build();

         given(employeeRepository.findAll()).willReturn(List.of(employee,employee1));
         // when - action or the behaviour that we are going test

         List<Employee> employeeList = employeeService.getAllEmployees();
         // then - verify the output

         assertThat(employeeList).isNotNull();
         assertThat(employeeList.size()).isEqualTo(2);
     }

    // Junit test for getAllEmployees method (negative scenario)
    @DisplayName("Junit test for getAllEmployees method (negative scenario)")
    @Test
    public void givenEmptyEmployeeList_whenGetAllEmployees_thenReturnEmptyEmployeesList(){
        // given - precondition or setup

        Employee employee1 = Employee.builder()
                .id(2L)
                .firstName("Jasho")
                .lastName("Patra")
                .email("jasho@gmail.com")
                .build();

        given(employeeRepository.findAll()).willReturn(Collections.emptyList());
        // when - action or the behaviour that we are going test

        List<Employee> employeeList = employeeService.getAllEmployees();
        // then - verify the output

        assertThat(employeeList).isEmpty();
        assertThat(employeeList.size()).isEqualTo(0);
    }

 // Junit test for getEmployeeById method
    @DisplayName("Junit test for getEmployeeById method")
     @Test
     public void givenEmployeeId_whenGetEmployeeById_thenReturnEmployeeObject(){
         // given - precondition or setup

         given(employeeRepository.findById(employee.getId())).willReturn(Optional.of(employee));
         // when - action or the behaviour that we are going test

         Employee savedEmployee = employeeService.getEmployeeById(employee.getId()).get();
         // then - verify the output

         assertThat(savedEmployee).isNotNull();
     }

      // Junit test for updateEmployee method
     @DisplayName("Junit test for updateEmployee method")
      @Test
      public void givenEmploeeObject_whenUpdateEmployee_thenReturnUpdatedEmployeeObject(){
          // given - precondition or setup

          given(employeeRepository.save(employee)).willReturn(employee);
          employee.setEmail("patra@gmail.com");
          employee.setFirstName("Jasho");
          // when - action or the behaviour that we are going test

          Employee updatedEmployee = employeeService.updateEmployee(employee);
          // then - verify the output

          assertThat(updatedEmployee.getEmail()).isEqualTo("patra@gmail.com");
          assertThat(updatedEmployee.getFirstName()).isEqualTo("Jasho");
      }

       // Junit test for deletedEmployee method
    @DisplayName("Junit test for deletedEmployee method")
       @Test
       public void givenEmployeeId_whenDeleteById_thenNothing(){

        long emplyeeId=1l;
           // given - precondition or setup
        willDoNothing().given(employeeRepository).deleteById(emplyeeId);
           // when - action or the behaviour that we are going test

        employeeService.deleteEmployee(emplyeeId);
           // then - verify the output

        verify(employeeRepository, times(1)).deleteById(emplyeeId);
       }
}
