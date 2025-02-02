package com.bilgeadam.utility.demoData;

import com.bilgeadam.entity.Employee;
import com.bilgeadam.entity.enums.EmployeeRole;

import java.util.List;

public class GenerateEmployee {

    public static List<Employee> generateEmployee() {
        Employee ceo = Employee.builder().companyId(1L).authId(9990L).firstName("Vehbi").lastName("Koç").email("vehbi@gmail.com").role(EmployeeRole.COMPANY_OWNER).positionId(1L).build();
        Employee mudur1 = Employee.builder().companyId(1L).authId(9991L).firstName("Hasan").lastName("Mezarcı").role(EmployeeRole.DEPARTMENT_MANAGER).positionId(2L).build();
        Employee mudur2 = Employee.builder().companyId(1L).authId(9992L).firstName("Ali").lastName("Bayram").role(EmployeeRole.DEPARTMENT_MANAGER).positionId(3L).build();
        Employee personel1  = Employee.builder().companyId(1L).authId(9993L).firstName("Personel 1").lastName("Soyisim").role(EmployeeRole.EMPLOYEE).positionId(2L).build();
        Employee personel2  = Employee.builder().companyId(1L).authId(9994L).firstName("Personel 2").lastName("Soyisim").role(EmployeeRole.EMPLOYEE).positionId(2L).build();
        Employee personel3  = Employee.builder().companyId(1L).authId(9995L).firstName("Personel 3").lastName("Soyisim").role(EmployeeRole.EMPLOYEE).positionId(3L).build();
        Employee personel4  = Employee.builder().companyId(1L).authId(9996L).firstName("Personel 4").lastName("Soyisim").role(EmployeeRole.EMPLOYEE).positionId(3L).build();
        return List.of(ceo,mudur1,mudur2,personel1,personel2,personel3,personel4);
    }


}
