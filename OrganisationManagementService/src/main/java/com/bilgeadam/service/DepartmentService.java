package com.bilgeadam.service;

import com.bilgeadam.dto.request.AddNewDepartmentRequest;
import com.bilgeadam.dto.request.UpdateDepartmentManagerRequest;
import com.bilgeadam.dto.request.UpdateDepartmentRequest;
import com.bilgeadam.dto.response.AllDepartmentResponse;
import com.bilgeadam.dto.response.DepartmentDetailResponse;
import com.bilgeadam.dto.response.OrganizationTreeResponse;
import com.bilgeadam.entity.Department;
import com.bilgeadam.entity.Employee;
import com.bilgeadam.entity.enums.EState;
import com.bilgeadam.entity.enums.EmployeeRole;
import com.bilgeadam.exception.ErrorType;
import com.bilgeadam.exception.OrganisationManagementException;
import com.bilgeadam.repository.DepartmentRepository;
import com.bilgeadam.view.VwDepartmendAndPosition;
import com.bilgeadam.view.VwDepartment;
import com.bilgeadam.view.VwEmployee;
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
public class    DepartmentService {
    private final DepartmentRepository departmentRepository;
    private final EmployeeService employeeService;
    private final PositionService positionService;

    public DepartmentService(DepartmentRepository departmentRepository, @Lazy EmployeeService employeeService, PositionService positionService) {
        this.departmentRepository = departmentRepository;
        this.employeeService = employeeService;
        this.positionService = positionService;
    }

    @Transactional
    public Boolean addDepartment(String token, AddNewDepartmentRequest dto) {
        Employee manager = employeeService.getEmployeeByToken(token);
        //ceo veya direktör(genel müdür) değilse yeni departman açamasın. Role bağlı yetkilendirme kontrolü

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

    public Boolean updateDepartmentManager(String token, UpdateDepartmentManagerRequest dto) {
        Employee manager = employeeService.getEmployeeByToken(token);
        Employee departmentManager = employeeService.findById(dto.managerId());
        checkCompany(manager.getCompanyId(), departmentManager.getCompanyId());
        Department department = findById(dto.departmentId());
        department.setManagerId(departmentManager.getId());
        if (!departmentManager.getRole().equals(EmployeeRole.DEPARTMENT_MANAGER)) {
            departmentManager.setRole(EmployeeRole.DEPARTMENT_MANAGER);
            employeeService.save(departmentManager);
        }
        departmentRepository.save(department);
        return true;

    }

    @Transactional
    public Boolean updateDepartment(String token, UpdateDepartmentRequest dto) {
        Employee manager = employeeService.getEmployeeByToken(token);
        Department department = findById(dto.departmentId());
        checkCompany(manager.getCompanyId(), department.getCompanyId());

        department.setName(dto.departmentName());
        department.setDescription(dto.description());
        department.setParentDepartmentId(dto.parentDepartmentId());
        departmentRepository.save(department);
        return true;
    }


    @Transactional
    public Boolean deleteDepartment(String token, Long departmentId) {
        Employee manager = employeeService.getEmployeeByToken(token);
        Department department = findById(departmentId);
        checkCompany(manager.getCompanyId(), department.getCompanyId());

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
        List<String> positions = positionService.findAllPositionNamesByDepartmentId(departmentId);
        response.setPositions(positions);
        return response;
    }

    public List<AllDepartmentResponse> findAllDepartments(String token) {
        Employee employee = employeeService.getEmployeeByToken(token);
        return departmentRepository.findAllDepartments(employee.getCompanyId(), EState.ACTIVE);
    }


    public List<AllDepartmentResponse> findAllSubDepartments(String token, Long departmentId) {
        Employee employee = employeeService.getEmployeeByToken(token);
        Long departmentCompanyId = departmentRepository.findCompanyIdByDepartmentId(departmentId).orElseThrow(() -> new OrganisationManagementException(ErrorType.DEPARTMENT_NOT_FOUND));
        checkCompany(employee.getCompanyId(), departmentCompanyId);
        return departmentRepository.findAllSubDepartments(departmentId);
    }

    public List<AllDepartmentResponse> findAllDepartmentsOfManager(String token, Long managerId) {
        Employee employee = employeeService.getEmployeeByToken(token);
        Employee manager = employeeService.findById(managerId);
        if(!manager.getRole().equals(EmployeeRole.DEPARTMENT_MANAGER))
            throw new OrganisationManagementException(ErrorType.MANAGER_NOT_FOUND);
        checkCompany(employee.getCompanyId(), manager.getCompanyId());
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

    private void checkCompany(Long managerCompanyId, Long departmentCompanyId) {
        if (!managerCompanyId.equals(departmentCompanyId))
            throw new OrganisationManagementException(ErrorType.UNAUTHORIZED);
    }


    public OrganizationTreeResponse getOrganizationTree(Long companyId) {
        //Todo: Firma kontrolü
        String companyName = "KOÇ HOLDİNG"; //Şirket ya da membership entitysi açılana kadar geçici.
        String CEO = employeeService.findCeoNameByCompanyId(companyId);
        OrganizationTreeResponse response = OrganizationTreeResponse.builder()
                .companyId(companyId)
                .companyName(companyName)
                .CEO(CEO)
                .departments(new ArrayList<>())
                .build();

        List<VwDepartment> vwDepartments = departmentRepository.findAllVwDepartmentByCompanyId(companyId);
        for (VwDepartment department : vwDepartments) {
            Optional<VwEmployee> vwManagerOpt = employeeService.findVwManagerByDepartmentId(department.getDepartmentId());
            //Departmanın yöneticisi yoksa.
            VwEmployee vwManager = vwManagerOpt.orElseGet(() -> new VwEmployee(-1L,  "-","avatar"));
            department.setManager(vwManager);
            List<VwEmployee> vwEmployees = employeeService.findAllVwEmployeesByDepartmentId(department.getDepartmentId());
            department.setEmployees(vwEmployees);
        }
        response.setDepartments(vwDepartments);
        return response;
    }

    public VwDepartmendAndPosition findDepartmentAndPositionNameByAuthId(String token){
       Employee employee = employeeService.getEmployeeByToken(token);
        System.out.println("auth id =  "+employee.getAuthId());
        return departmentRepository.findVwDepartmentAndPositionNamesByAuthId(employee.getAuthId())
                .orElseThrow(() -> new OrganisationManagementException(ErrorType.EMPLOYEE_NOT_FOUND));
    }


}