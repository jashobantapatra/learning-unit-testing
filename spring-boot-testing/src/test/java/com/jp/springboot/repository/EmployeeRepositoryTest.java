package com.jp.springboot.repository;

import com.jp.springboot.model.Employee;
import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;
import java.util.Optional;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class EmployeeRepositoryTest {

    @Autowired
    private EmployeeRepository employeeRepository;

    private Employee employee;

    @BeforeEach
    public void setUp(){
        employee = Employee.builder()
                .firstName("Jashobanta")
                .lastName("Patra")
                .email("jashobanta@gmail.com")
                .build();
    }

    //JUnit test for save employee operation
    @DisplayName("JUnit test for save employee operation")
    @Test
    public void givenEmployeeObject_whenSave_thenReturnSavedEmployee(){

        // given - precondition to setup
       /* Employee employee = Employee.builder()
                .firstName("Jashobanta")
                .lastName("Patra")
                .email("jashobanta@gmail.com")
                .build(); */


        // when - action or the behaviour that we are going test
        Employee savedEmployee = employeeRepository.save(employee);

        // then - verify the output
        assertThat(savedEmployee).isNotNull();
        assertThat(savedEmployee.getId()).isGreaterThan(0);

    }

    // Junit test for get all employees operation
    @DisplayName("Junit test for get all employees operation")
    @Test
    public void givenEmployeeList_whenFindAll_thenEmployeeList(){

        // given - precondition or setup
        /*Employee employee = Employee.builder()
                .firstName("Jashobanta")
                .lastName("Patra")
                .email("jashobanta@gmail.com")
                .build();*/

        Employee employee1 = Employee.builder()
                .firstName("Anuradha")
                .lastName("Behura")
                .email("anuradha@gmail.com")
                .build();

        employeeRepository.save(employee);
        employeeRepository.save(employee1);

        // when - action or the behaviour that we are going test

        List<Employee> employeeList=  employeeRepository.findAll();

        // then - verify the output

        assertThat(employeeList).isNotNull();
        assertThat(employeeList.size()).isEqualTo(2);

    }

     // Junit test for get employee by id operation
     @DisplayName("Junit test for get employee by id operation")
     @Test
     public void givenEmployeeObject_whenFindById_thenReturnEmployeeObject(){

         // given - precondition or setup

         /*Employee employee = Employee.builder()
                 .firstName("Jashobanta")
                 .lastName("Patra")
                 .email("jashobanta@gmail.com")
                 .build();*/

         Employee savedEmployee = employeeRepository.save(employee);
         // when - action or the behaviour that we are going test


         Employee employeeDb =  employeeRepository.findById(savedEmployee.getId()).get();

         // then - verify the output
         assertThat(employeeDb).isNotNull();

     }
      // Junit test for get employee by email operation
      @DisplayName("Junit test for get employee by email operation")
      @Test
      public void givenEmployeeEmail_whenFindByEmail_thenEmployeeObject(){

          // given - precondition or setup

          /*Employee employee = Employee.builder()
                  .firstName("Jashobanta")
                  .lastName("Patra")
                  .email("jashobanta@gmail.com")
                  .build();*/

           employeeRepository.save(employee);

          // when - action or the behaviour that we are going test

          Employee  employeeDb=  employeeRepository.findByEmail(employee.getEmail()).get();

          // then - verify the output

          assertThat(employeeDb).isNotNull();

      }
    // Junit test for update employee operation
    @DisplayName("Junit test for update employee operation")
   @Test
   public void givenEmployeeObject_whenUpdateEmployee_thenReturnUpdatedEmployee(){
        // given - precondition or setup

       /*Employee employee = Employee.builder()
               .firstName("Jasho")
               .lastName("Patra")
               .email("jashobanta@gmail.com")
               .build();*/

       employeeRepository.save(employee);

       // when - action or the behaviour that we are going test

       Employee savedEmployee = employeeRepository.findById(employee.getId()).get();
       savedEmployee.setEmail("jashobantapatra@gmail.com");
       savedEmployee.setFirstName("Jashobanta");
       Employee updatedEmployee = employeeRepository.save(savedEmployee);

       // then - verify the output

        assertThat(updatedEmployee.getEmail()).isEqualTo("jashobantapatra@gmail.com");
        assertThat(updatedEmployee.getFirstName()).isEqualTo("Jashobanta");
   }
 // Junit test for delete employee operation
    @DisplayName("Junit test for delete employee operation")
     @Test
     public void givenEmployeeObject_whenDelete_thenRemoveEmployee(){
         // given - precondition or setup

         /*Employee employee = Employee.builder()
                 .firstName("Jashobanta")
                 .lastName("Patra")
                 .email("jashobanta@gmail.com")
                 .build();*/

         employeeRepository.save(employee);

         // when - action or the behaviour that we are going test

         employeeRepository.deleteById(employee.getId());
         Optional<Employee> employeeOptional = employeeRepository.findById(employee.getId());

         // then - verify the output

         assertThat(employeeOptional).isEmpty();

     }
      // Junit test for custom query using JPQL with index
    @DisplayName("Junit test for custom query using JPQL with index")
      @Test
      public void givenFirstNameAndLastName_whenFindByJPQL_thenReturnEmployeeObject(){
      // given - precondition or setup
          /*Employee employee = Employee.builder()
                  .firstName("Jashobanta")
                  .lastName("Patra")
                  .email("jashobanta@gmail.com")
                  .build();*/

          employeeRepository.save(employee);
          String firstName="Jashobanta";
          String lastName="Patra";
      // when - action or the behaviour that we are going test
        Employee savedEmployee = employeeRepository.findByJPQL(firstName,lastName);
      // then - verify the output
          assertThat(savedEmployee).isNotNull();

      }
   // Junit test for custom query using JPQL with Named params
   @DisplayName("Junit test for custom query using JPQL with Named params")
   @Test
   public void givenFirstNameAndLastName_whenFindByJPQLNamedParams_thenReturnEmployeeObject(){
   // given - precondition or setup

       /*Employee employee = Employee.builder()
               .firstName("Jashobanta")
               .lastName("Patra")
               .email("jashobanta@gmail.com")
               .build();*/

       employeeRepository.save(employee);
       String firstName="Jashobanta";
       String lastName="Patra";

   // when - action or the behaviour that we are going test

       Employee savedEmployee = employeeRepository.findByJPQLNamedParams(firstName,lastName);

       // then - verify the output

       assertThat(savedEmployee).isNotNull();

   }

    // Junit test for custom query using native SQL with index
    @DisplayName("Junit test for custom query using native SQL with index")
        @Test
        public void givenFirstNameAndLastName_whenFindByNativeSQL_thenReturnEmployeeObject(){
            // given - precondition or setup
            /*Employee employee = Employee.builder()
                    .firstName("Jashobanta")
                    .lastName("Patra")
                    .email("jashobanta@gmail.com")
                    .build();*/

            employeeRepository.save(employee);
            //String firstName="Jashobanta";
            //String lastName="Patra";

            // when - action or the behaviour that we are going test

            Employee savedEmployee = employeeRepository.
                    findByNativeSQL(employee.getFirstName(),employee.getLastName());

            // then - verify the output

            assertThat(savedEmployee).isNotNull();

        }
    // Junit test for custom query using native SQL with params
    @DisplayName("Junit test for custom query using native SQL with params")
    @Test
    public void givenFirstNameAndLastName_whenFindByNativeSQLNamedParams_thenReturnEmployeeObject(){
        // given - precondition or setup
        /*Employee employee = Employee.builder()
                .firstName("Jashobanta")
                .lastName("Patra")
                .email("jashobanta@gmail.com")
                .build();*/

        employeeRepository.save(employee);
        //String firstName="Jashobanta";
        //String lastName="Patra";

        // when - action or the behaviour that we are going test

        Employee savedEmployee = employeeRepository
                .findByNativeSQLNamed(employee.getFirstName(),employee.getLastName());

        // then - verify the output

        assertThat(savedEmployee).isNotNull();

    }
}
