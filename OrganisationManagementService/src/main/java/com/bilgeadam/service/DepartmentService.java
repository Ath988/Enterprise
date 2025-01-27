package com.bilgeadam.service;

import com.bilgeadam.dto.request.AddNewDepartmentRequest;
import com.bilgeadam.dto.request.UpdateDepartmentRequest;
import com.bilgeadam.dto.response.AllDepartmentResponse;
import com.bilgeadam.dto.response.DepartmentDetailResponse;
import com.bilgeadam.entity.Department;
import com.bilgeadam.entity.Employee;
import com.bilgeadam.entity.enums.EState;
import com.bilgeadam.exception.ErrorType;
import com.bilgeadam.exception.OrganisationManagementException;
import com.bilgeadam.repository.DepartmentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestHeader;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class DepartmentService {
    private final DepartmentRepository departmentRepository;
    private final EmployeeService employeeService;

    public DepartmentService(DepartmentRepository departmentRepository, @Lazy EmployeeService employeeService) {
        this.departmentRepository = departmentRepository;
        this.employeeService = employeeService;
    }

    @Transactional
    public Boolean addDepartment(String token, AddNewDepartmentRequest dto) {
        Employee manager = employeeService.getEmployeeByToken(token);
        //ceo veya direktör(genel müdür) değilse yeni departman açamasın.
        //Todo: Bunun gibi kontroller için ileride çalışan rolüne bağlı security yetkilendirme kullanılabilir.
        if (manager.getEmployeeRole().getRoleRank() > 2)
            throw new OrganisationManagementException(ErrorType.UNAUTHORIZED);

        Department department = Department.builder()
                .name(dto.departmentName())
                .description(dto.description())
                .managerId(manager.getId())
                .companyId(manager.getCompanyId())
                .build();
        if (dto.parentDepartmentId() != null) {
            department.setParentDepartmentId(dto.parentDepartmentId());
        }
        departmentRepository.save(department);
        return true;
    }

    @Transactional
    public Boolean updateDepartment(String token, UpdateDepartmentRequest dto) {
        Employee manager = employeeService.getEmployeeByToken(token);
        Department department = departmentRepository.findById(dto.departmentId()).orElseThrow(() -> new OrganisationManagementException(ErrorType.DEPARTMENT_NOT_FOUND));
        //Ana yönetici kadrosu (ceo ve genel müdür) veya o departmanın müdürü güncelleme yapabilsin sadece.
        // 2. ana koşul yöneticilerin farklı şirket departmanlarına istek atmalarını önlemek için.
        if ((manager.getEmployeeRole().getRoleRank() > 2 && !department.getManagerId().equals(manager.getId())))
            throw new OrganisationManagementException(ErrorType.UNAUTHORIZED);

        checkCompany(manager.getCompanyId(), department.getCompanyId());

        department.setName(dto.departmentName());
        department.setDescription(dto.description());
        department.setParentDepartmentId(dto.parentDepartmentId());
        //Todo: Departman yönetici değiştirmek için farklı metod yazılabilir
        departmentRepository.save(department);
        return true;
    }


    @Transactional
    public Boolean deleteDepartment(String token, Long departmentId) {
        Employee manager = employeeService.getEmployeeByToken(token);
        Department department = findById(departmentId);
        //Sadece ceo ve genel müdür departman silme işlemi yapabilsin. Ayrıca kendi şirket departmanları için mi bu istek atılıyor kontrolü.
        if (manager.getEmployeeRole().getRoleRank() > 2 || !department.getCompanyId().equals(manager.getCompanyId()))
            throw new OrganisationManagementException(ErrorType.UNAUTHORIZED);

        department.setState(EState.PASSIVE);
        departmentRepository.save(department);
        return true;
    }

    public DepartmentDetailResponse findDepartmentDetail(String token, Long departmentId) {
        Employee employee = employeeService.getEmployeeByToken(token);
        Department department = findById(departmentId);
        //Aynı şirkette çalışan herkes departman detaylarına ulaşabilsin.
        checkCompany(employee.getCompanyId(), department.getCompanyId());

        DepartmentDetailResponse response = departmentRepository.findDepartmentDetailByDepartmentId(departmentId);
        if (department.getParentDepartmentId() != null) {
            if (departmentRepository.existsById(department.getParentDepartmentId())) {
                Department parentDepartment = findById(department.getParentDepartmentId());
                response.setParentDepartment(parentDepartment.getName());
            }
        }
        return response;
    }

    public List<AllDepartmentResponse> findAllDepartments(String token) {
        Employee employee = employeeService.getEmployeeByToken(token);
        return departmentRepository.findAllDepartments(employee.getCompanyId());
    }


    public List<AllDepartmentResponse> findAllSubDepartments(String token, Long departmentId) {
        Employee employee = employeeService.getEmployeeByToken(token);
        Long departmentCompanyId = departmentRepository.findCompanyIdByDepartmentId(departmentId).orElseThrow(() -> new OrganisationManagementException(ErrorType.DEPARTMENT_NOT_FOUND));
        checkCompany(employee.getCompanyId(), departmentCompanyId);
        return departmentRepository.findAllSubDepartments(departmentId);
    }

    public List<AllDepartmentResponse> findAllDepartmentsOfManager(String token, Long managerId) {
        Employee employee = employeeService.getEmployeeByToken(token);
        Long companyId = departmentRepository.findCompanyIdByDepartmentId(employee.getDepartmentId()).orElseThrow(() -> new OrganisationManagementException(ErrorType.DEPARTMENT_NOT_FOUND));
        checkCompany(employee.getCompanyId(), companyId);
        return departmentRepository.findAllDepartmentsOfManagerByManagerId(managerId);
    }

    public List<AllDepartmentResponse> findDepartmentHierarchy(String token, Long departmentId) {
        Department department = findById(departmentId);
        Employee employee = employeeService.getEmployeeByToken(token);
        checkCompany(employee.getCompanyId(), department.getCompanyId());

        if (department.getParentDepartmentId() == null) return new ArrayList<>();
        List<AllDepartmentResponse> hierarchyList = new ArrayList<>();
        Long topDepartmentId = department.getParentDepartmentId();
        while (topDepartmentId != null) {
            Department topDepartment = findById(topDepartmentId);
            Employee manager = employeeService.findById(department.getManagerId());
            hierarchyList.add(new AllDepartmentResponse(topDepartmentId, topDepartment.getName(), (manager.getFirstName() + " " + manager.getLastName())));
            topDepartmentId = topDepartment.getParentDepartmentId();
        }
        return hierarchyList;
    }

    public Boolean isDepartmentExistById(Long departmentId) {
        return departmentRepository.existsById(departmentId);
    }

    public String getDepartmentNameByDepartmentId(Long departmentId) {
        return departmentRepository.findDepartmentNameByDepartmentId(departmentId).orElseThrow(() -> new OrganisationManagementException(ErrorType.DEPARTMENT_NOT_FOUND));
    }

    public Department findById(Long departmentId) {
        return departmentRepository.findById(departmentId).orElseThrow(() -> new OrganisationManagementException(ErrorType.DEPARTMENT_NOT_FOUND));
    }

    private void checkCompany(Long managerCompanyId, Long departmentCompanyId){
        if(!managerCompanyId.equals(departmentCompanyId))
            throw new OrganisationManagementException(ErrorType.UNAUTHORIZED);
    }


}
