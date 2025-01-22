package com.bilgeadam.enterprise.service;

import com.bilgeadam.enterprise.entity.Department;
import com.bilgeadam.enterprise.exception.ErrorType;
import com.bilgeadam.enterprise.exception.OrganisationManagementException;
import com.bilgeadam.enterprise.repository.DepartmentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DepartmentService {
    private final DepartmentRepository departmentRepository;

    public Boolean isDepartmentExistById(Long departmentId){
       return departmentRepository.existsById(departmentId);
    }


    public String getDepartmentNameByDepartmentId(Long departmentId) {
        return departmentRepository.findDepartmentNameByDepartmentId(departmentId).orElseThrow(()->new OrganisationManagementException(ErrorType.DEPARTMENT_NOT_FOUND));
    }
}
