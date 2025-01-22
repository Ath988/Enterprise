package com.bilgeadam.enterprise.utility.demoData;

import com.bilgeadam.enterprise.repository.DepartmentRepository;
import com.bilgeadam.enterprise.repository.EmployeeRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class GenerateDemoData {


    private final EmployeeRepository employeeRepository;
    private final DepartmentRepository departmentRepository;

    @PostConstruct
    public void generateData(){
        if(employeeRepository.count() == 0){
            employeeRepository.saveAll(GenerateEmployee.generateEmployee());
        }
        if(departmentRepository.count() == 0){
            departmentRepository.saveAll(GenerateDepartments.generateDepartments());
        }
    }
}
