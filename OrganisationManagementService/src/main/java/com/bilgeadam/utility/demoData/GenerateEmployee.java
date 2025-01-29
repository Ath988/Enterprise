package com.bilgeadam.utility.demoData;

import com.bilgeadam.entity.Employee;
import com.bilgeadam.entity.enums.EmployeeRole;

import java.util.List;

public class GenerateEmployee {

    public static List<Employee> generateEmployee() {
        Employee ceo = Employee.builder().companyId(1L).authId(1L).firstName("Vehbi").lastName("Koç").email("vehbi@gmail.com").employeeRole(EmployeeRole.CEO).departmentId(1L).build();
        Employee mudur1 = Employee.builder().companyId(1L).authId(2L).firstName("Hasan").lastName("Mezarcı").employeeRole(EmployeeRole.MANAGER).departmentId(2L).managerEmployeeId(1L).build();
        Employee mudur2 = Employee.builder().companyId(1L).authId(3L).firstName("Ali").lastName("Bayram").employeeRole(EmployeeRole.MANAGER).departmentId(3L).managerEmployeeId(1L).build();
        Employee personel1  = Employee.builder().companyId(1L).authId(4L).firstName("Personel 1").employeeRole(EmployeeRole.STAFF).departmentId(2L).managerEmployeeId(2L).build();
        Employee personel2  = Employee.builder().companyId(1L).authId(5L).firstName("Personel 2").employeeRole(EmployeeRole.STAFF).departmentId(2L).managerEmployeeId(2L).build();
        Employee personel3  = Employee.builder().companyId(1L).authId(6L).firstName("Personel 3").employeeRole(EmployeeRole.STAFF).departmentId(2L).managerEmployeeId(2L).build();
        return List.of(ceo,mudur1,mudur2,personel1,personel2,personel3);
    }


}
