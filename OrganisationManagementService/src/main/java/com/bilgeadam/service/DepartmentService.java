package com.bilgeadam.service;

import com.bilgeadam.exception.ErrorType;
import com.bilgeadam.exception.OrganisationManagementException;
import com.bilgeadam.repository.DepartmentRepository;
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
