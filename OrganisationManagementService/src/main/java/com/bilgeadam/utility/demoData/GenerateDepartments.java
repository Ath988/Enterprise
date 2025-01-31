package com.bilgeadam.utility.demoData;

import com.bilgeadam.entity.Department;

import java.util.List;

public class    GenerateDepartments {

    public static List<Department> generateDepartments() {
        Department department1 = Department.builder().name("Genel Müdürlük").managerId(1L).companyId(1L).build();
        Department department2 = Department.builder().name("IT Departmanı").managerId(2L).parentDepartmentId(1L).companyId(1L).build();
        Department department3 = Department.builder().name("HR Departmanı").managerId(3L).parentDepartmentId(1L).companyId(1L).build();
        return List.of(department1, department2, department3);
    }
}
