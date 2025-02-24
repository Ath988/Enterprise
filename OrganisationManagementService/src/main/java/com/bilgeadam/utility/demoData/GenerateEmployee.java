package com.bilgeadam.utility.demoData;

import com.bilgeadam.entity.Employee;
import com.bilgeadam.entity.enums.EGender;
import com.bilgeadam.entity.enums.EmployeeRole;

import java.util.List;

public class GenerateEmployee {

    public static List<Employee> generateEmployee() {
            Employee companyManager = Employee.builder()
                                              .companyId(1L).authId(1L)
                                              .firstName("Vehbi").lastName("Koç")
                                              .email("vehbi@test.com")
                                              .role(EmployeeRole.COMPANY_OWNER)
                                              .gender(EGender.MALE)
                                              .positionId(1L) // CEO
                                              .build();
            
            Employee departmentManager1 = Employee.builder()
                                                  .companyId(1L).authId(2L)
                                                  .firstName("Hasan").lastName("Kayar")
                                                  .email("hasan@test.com")
                                                  .role(EmployeeRole.DEPARTMENT_MANAGER)
                                                  .gender(EGender.MALE)
                                                  .positionId(2L) // CTO (IT Departmanı)
                                                  .build();
            
            Employee departmentManager2 = Employee.builder()
                                                  .companyId(1L).authId(3L)
                                                  .firstName("Ayşe").lastName("Kulin")
                                                  .email("ayse@test.com")
                                                  .role(EmployeeRole.DEPARTMENT_MANAGER)
                                                  .gender(EGender.FEMALE)
                                                  .positionId(3L) // CFO (HR Departmanı)
                                                  .build();
            
            Employee employee1 = Employee.builder()
                                         .companyId(1L).authId(4L)
                                         .firstName("Mehmet").lastName("Öz")
                                         .email("mehmet@test.com")
                                         .role(EmployeeRole.EMPLOYEE)
                                         .gender(EGender.MALE)
                                         .positionId(4L) // Developer (CTO'ya bağlı)
                                         .build();
            
            Employee employee2 = Employee.builder()
                                         .companyId(1L).authId(5L)
                                         .firstName("Hülya").lastName("Pamuk")
                                         .email("hulya@test.com")
                                         .role(EmployeeRole.EMPLOYEE)
                                         .gender(EGender.FEMALE)
                                         .positionId(5L) // Tester (CTO'ya bağlı)
                                         .build();
            
            Employee employee3 = Employee.builder()
                                         .companyId(1L).authId(6L)
                                         .firstName("Kemal").lastName("Sunal")
                                         .email("kemal@test.com")
                                         .role(EmployeeRole.EMPLOYEE)
                                         .gender(EGender.MALE)
                                         .positionId(6L) // Accountant (CFO'ya bağlı)
                                         .build();
            
            return List.of(companyManager, departmentManager1, departmentManager2, employee1, employee2, employee3);
        }
        
    }