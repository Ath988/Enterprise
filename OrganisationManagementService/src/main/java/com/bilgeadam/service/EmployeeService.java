package com.bilgeadam.service;

import com.bilgeadam.dto.request.AddEmployeeRequest;
import com.bilgeadam.dto.request.AssignEmployeesToDepartmentRequest;
import com.bilgeadam.dto.request.UpdateEmployeeRequest;
import com.bilgeadam.dto.response.AllEmployeeResponse;
import com.bilgeadam.dto.response.EmployeeDetailResponse;
import com.bilgeadam.entity.Department;
import com.bilgeadam.entity.Employee;
import com.bilgeadam.entity.enums.EState;
import com.bilgeadam.exception.ErrorType;
import com.bilgeadam.exception.OrganisationManagementException;
import com.bilgeadam.repository.EmployeeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

    public EmployeeDetailResponse findEmployeeDetailById(Long employeeId) {
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
            hierarchyList.add(new AllEmployeeResponse(managerId,manager.getFirstName(), manager.getLastName(), manager.getEmployeeRole(), departmentName));
            managerId = manager.getManagerEmployeeId();
        }
        return hierarchyList;
    }

    public List<AllEmployeeResponse> findEmployeeSubordinates(Long employeeId) {
        if(!employeeRepository.existsById(employeeId)) throw new OrganisationManagementException(ErrorType.EMPLOYEE_NOT_FOUND);
        return employeeRepository.findAllEmployeeSubordinatesByManagerId(employeeId);
    }

    @Transactional
    public boolean assignEmployeesToDepartment(AssignEmployeesToDepartmentRequest dto){
        //tokendan managerId çekilip yetkinlik kontrolleri yapılacak
        List<Long> employeeIdList = dto.employeeIds();
        Department department = departmentService.findById(dto.departmenId()); // managerın companysinde bu departman var mı kontrol edilecek.
        for(Long employeeId : employeeIdList){
            Employee employee = employeeRepository.findById(employeeId).orElseThrow(() -> new OrganisationManagementException(ErrorType.EMPLOYEE_NOT_FOUND));
            //eğer employee companyId ile manager companyId tutuşmuyorsa veya departmanı değiştirilmek istenen
            // çalışanın pozisyonu managerin altında değilse hata fırlatılacak.
            employee.setDepartmentId(department.getId());
            employeeRepository.save(employee);
        }
        return true;
    }

    public boolean deleteEmployee(Long employeeId) {
        //Todo: Token ile yetkinlik kontrolü, çalışan Id ve managerIdsi ile aynı şirket mi değil mi onların kontrolü.
        Employee employee  = employeeRepository.findById(employeeId).orElseThrow(() -> new OrganisationManagementException(ErrorType.EMPLOYEE_NOT_FOUND));
        employee.setState(EState.PASSIVE);
        employeeRepository.save(employee);
        return true;
    }

    public boolean updateEmployee(UpdateEmployeeRequest dto){
        //Todo: Token ile yetkinlik kontrolü.
        Employee employee = employeeRepository.findById(dto.employeeId()).orElseThrow(() -> new OrganisationManagementException(ErrorType.EMPLOYEE_NOT_FOUND));
        //Todo: Çalışan ve Yönetici şirket bilgilerinin eşleşiyor mu, yönetici ve çalışan pozisyon kontrolü.
        employee.setFirstName(dto.firstName());
        employee.setLastName(dto.lastName());
        employee.setEmail(dto.email());
        employee.setManagerEmployeeId(employee.getManagerEmployeeId());
        employee.setDepartmentId(dto.departmentId());
        employeeRepository.save(employee);
        return false;
    }

    public Employee findById(Long managerId) {
        return employeeRepository.findById(managerId).orElseThrow(() -> new OrganisationManagementException(ErrorType.EMPLOYEE_NOT_FOUND));
    }
}
