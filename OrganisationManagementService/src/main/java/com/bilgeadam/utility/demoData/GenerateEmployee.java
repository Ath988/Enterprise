package com.bilgeadam.utility.demoData;

import com.bilgeadam.entity.Employee;
import com.bilgeadam.entity.enums.EmployeeRole;

import java.util.List;

public class GenerateEmployee {

    public static List<Employee> generateEmployee() {
        Employee ceo = Employee.builder().companyId(1L).authId(1L).firstName("Vehbi").lastName("Koç").email("vehbi@gmail.com").role(EmployeeRole.COMPANY_OWNER).positionId(1L).build();
        Employee mudur1 = Employee.builder().companyId(1L).authId(2L).firstName("Hasan").lastName("Mezarcı").role(EmployeeRole.DEPARTMENT_MANAGER).positionId(2L).build();
        Employee mudur2 = Employee.builder().companyId(1L).authId(3L).firstName("Ali").lastName("Bayram").role(EmployeeRole.DEPARTMENT_MANAGER).positionId(3L).build();
        Employee personel1  = Employee.builder().companyId(1L).authId(4L).firstName("Personel 1").lastName("Soyisim").role(EmployeeRole.EMPLOYEE).positionId(2L).build();
        Employee personel2  = Employee.builder().companyId(1L).authId(5L).firstName("Personel 2").lastName("Soyisim").role(EmployeeRole.EMPLOYEE).positionId(2L).build();
        Employee personel3  = Employee.builder().companyId(1L).authId(6L).firstName("Personel 3").lastName("Soyisim").role(EmployeeRole.EMPLOYEE).positionId(3L).build();
        Employee personel4  = Employee.builder().companyId(1L).authId(6L).firstName("Personel 4").lastName("Soyisim").role(EmployeeRole.EMPLOYEE).positionId(3L).build();
        return List.of(ceo,mudur1,mudur2,personel1,personel2,personel3,personel4);
    }


}
