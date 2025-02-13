package com.bilgeadam.utility.demoData;

import com.bilgeadam.repository.DepartmentRepository;
import com.bilgeadam.repository.EmployeeRepository;
import com.bilgeadam.repository.PositionRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class GenerateDemoData {


    private final EmployeeRepository employeeRepository;
    private final DepartmentRepository departmentRepository;
    private final PositionRepository positionRepository;

    @PostConstruct
    public void generateData(){
//        if(employeeRepository.count() == 0){
//            employeeRepository.saveAll(GenerateEmployee.generateEmployee());
//        }
        if(departmentRepository.count() == 0){
            departmentRepository.saveAll(GenerateDepartments.generateDepartments());
        }
        if(positionRepository.count() == 0){
            positionRepository.saveAll(GeneratePositions.generatePositions());
        }
    }
}
