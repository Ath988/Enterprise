package com.bilgeadam.utility.demoData;

import com.bilgeadam.entity.Department;

import java.util.List;

public class GenerateDepartments {

    public static List<Department> generateDepartments() {
        Department department1 = Department.builder().name("GENEL MÜDÜRLÜK").companyId(1L).build();
        Department department2 = Department.builder().name("Mali ve İdari İşler").parentDepartmentId(1L).companyId(1L).build();
        Department department3 = Department.builder().name("İnsan Kaynakları").parentDepartmentId(2L).companyId(1L).build();
        return List.of(department1, department2, department3);
    }
}
