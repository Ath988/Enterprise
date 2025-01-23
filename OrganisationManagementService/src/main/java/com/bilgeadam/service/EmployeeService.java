package com.bilgeadam.service;

import com.bilgeadam.dto.request.AddEmployeeRequest;
import com.bilgeadam.dto.response.AllEmployeeResponse;
import com.bilgeadam.dto.response.EmployeeDetailResponse;
import com.bilgeadam.entity.Employee;
import com.bilgeadam.exception.ErrorType;
import com.bilgeadam.exception.OrganisationManagementException;
import com.bilgeadam.repository.EmployeeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class EmployeeService {
    private final EmployeeRepository employeeRepository;
    private final DepartmentService departmentService;

    public Boolean addNewEmployee(AddEmployeeRequest dto) {
        /*
        dtodaki token ile token geçerliliği ve yetki kontrolü yapılacak, manager bilgileri buradan daha
        sonra çekilecek
        */
        if (!departmentService.isDepartmentExistById(dto.departmentId()))
            throw new OrganisationManagementException(ErrorType.DEPARTMENT_NOT_FOUND);
        Employee employee = Employee.builder()
                .departmentId(dto.departmentId())
                .email(dto.email())
                .firstName(dto.firstName())
                .lastName(dto.lastName())
                .employeeRole(dto.role())
                .build();

        employeeRepository.save(employee);
        // Yeni çalışan eklendikten sonra, AuthService kullanıcı adı,şifre kayıtları için mail gönderilebilir.

        return true;
    }

    public List<AllEmployeeResponse> findAllEmployees(Long companyId) {
        //Todo: şirket Idsi yönetici tokenından çekilecek.
        return employeeRepository.findAllEmployee(companyId);
    }

    public EmployeeDetailResponse findEmployeeById(Long employeeId) {
        return employeeRepository.findEmployeeDetail(employeeId).orElseThrow(() -> new OrganisationManagementException(ErrorType.EMPLOYEE_NOT_FOUND));
    }


    public List<AllEmployeeResponse> findEmployeeHierarchy(Long employeeId) {
        Employee employee = employeeRepository.findById(employeeId).orElseThrow(() -> new OrganisationManagementException(ErrorType.EMPLOYEE_NOT_FOUND));
        if (employee.getManagerEmployeeId() == null) return new ArrayList<>();
        List<AllEmployeeResponse> hierarchyList = new ArrayList<>();
        Long managerId = employee.getManagerEmployeeId();
        while (managerId != null) {
            Employee manager = employeeRepository.findById(managerId).orElseThrow(() -> new OrganisationManagementException(ErrorType.EMPLOYEE_NOT_FOUND));
            String departmentName = departmentService.getDepartmentNameByDepartmentId(manager.getDepartmentId());
            hierarchyList.add(new AllEmployeeResponse(manager.getFirstName(), manager.getLastName(), manager.getEmployeeRole(), departmentName));
            managerId = manager.getManagerEmployeeId();
        }
        return hierarchyList;
    }

    public List<AllEmployeeResponse> findEmployeeSubordinates(Long employeeId) {
        Employee employee = employeeRepository.findById(employeeId).orElseThrow(() -> new OrganisationManagementException(ErrorType.EMPLOYEE_NOT_FOUND));
        return employeeRepository.findAllEmployeeSubordinatesByManagerId(employeeId);
    }
}
