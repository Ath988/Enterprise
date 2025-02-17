package com.bilgeadam.utility.demoData;

import com.bilgeadam.entity.Employee;
import com.bilgeadam.entity.enums.EGender;
import com.bilgeadam.entity.enums.EmployeeRole;

import java.util.List;

public class GenerateEmployee {

    public static List<Employee> generateEmployee() {
        Employee companyManager     = Employee.builder().companyId(1L).authId(1L).firstName("Vehbi").lastName("Koc").email("vehbi@test.com").role(EmployeeRole.COMPANY_OWNER).gender(EGender.MALE).positionId(1L).build();
        Employee departmentManager1 = Employee.builder().companyId(1L).authId(2L).firstName("Hasan").lastName("Kayar").email("hasan@test.com").role(EmployeeRole.DEPARTMENT_MANAGER).gender(EGender.MALE).positionId(2L).build();
        Employee departmentManager2 = Employee.builder().companyId(1L).authId(3L).firstName("Ayse").lastName("Kulin").email("ayse@test.com").role(EmployeeRole.DEPARTMENT_MANAGER).gender(EGender.FEMALE).positionId(3L).build();
        Employee employee1          = Employee.builder().companyId(1L).authId(4L).firstName("Mehmet").lastName("Oz").email("mehmet@test.com").role(EmployeeRole.EMPLOYEE).gender(EGender.MALE).positionId(2L).build();
        Employee employee2          = Employee.builder().companyId(1L).authId(5L).firstName("Hulya").lastName("Pamuk").email("hulya@test.com").role(EmployeeRole.EMPLOYEE).gender(EGender.FEMALE).positionId(2L).build();



        return List.of(companyManager,departmentManager1,departmentManager2,employee1,employee2);
    }


}
