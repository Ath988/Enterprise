package com.bilgeadam.service;

import com.bilgeadam.dto.request.AddEmployeeRequest;
import com.bilgeadam.dto.request.AssignEmployeesToDepartmentRequest;
import com.bilgeadam.dto.request.CreateCompanyManagerRequest;
import com.bilgeadam.dto.request.UpdateEmployeeRequest;
import com.bilgeadam.dto.response.AllEmployeeResponse;
import com.bilgeadam.dto.response.EmployeeDetailResponse;
import com.bilgeadam.entity.Department;
import com.bilgeadam.entity.Employee;
import com.bilgeadam.entity.Position;
import com.bilgeadam.entity.enums.EState;
import com.bilgeadam.entity.enums.EmployeeRole;
import com.bilgeadam.exception.ErrorType;
import com.bilgeadam.exception.OrganisationManagementException;
import com.bilgeadam.repository.EmployeeRepository;
import com.bilgeadam.utility.JwtManager;
import com.bilgeadam.view.VwEmployee;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service

public class EmployeeService {
    private final EmployeeRepository employeeRepository;
    private final DepartmentService departmentService;
    private final JwtService jwtService;
    private final PositionService positionService;
    private final JwtManager jwtManager;

    public EmployeeService(EmployeeRepository employeeRepository, @Lazy DepartmentService departmentService, JwtService jwtService, PositionService positionService, JwtManager jwtManager) {
        this.employeeRepository = employeeRepository;
        this.departmentService = departmentService;
        this.jwtService = jwtService;
        this.positionService = positionService;
        this.jwtManager = jwtManager;
    }


    @Transactional
    public Boolean createCompanyManager(CreateCompanyManagerRequest dto) {
        Employee companyManager = Employee.builder()
                .authId(dto.authId())
                .companyId(dto.authId()) //Geçici
                .positionId(1L) // BAŞKAN Position
                .email(dto.email())
                .role(EmployeeRole.COMPANY_OWNER)
                .build();
        employeeRepository.save(companyManager);
        return true;
    }


    @Transactional
    public Boolean addNewEmployee(String token, AddEmployeeRequest dto) {
        //Todo: AuthService email kontrolü
        Employee manager = getEmployeeByToken(token);
        Position position = positionService.findById(dto.positionId());
        if (manager.getRole().equals(EmployeeRole.DEPARTMENT_MANAGER)) {
            //Eğer yeni çalışan eklicek kişi departman müdürüyse sadece kendi departmanına ekleyebilsin.
            Long departmentIdOfManager = positionService.findDepartmentIdByPositionId(manager.getPositionId());
            if (departmentIdOfManager.equals(position.getDepartmentId()))
                throw new OrganisationManagementException(ErrorType.UNAUTHORIZED);
        }

        Employee employee = Employee.builder()
                .positionId(dto.positionId())
                .email(dto.email())
                .firstName(dto.firstName())
                .lastName(dto.lastName())
                .companyId(manager.getCompanyId())
                .role(dto.role())
                .build();
        employeeRepository.save(employee);
        return true;

        //Todo: Yeni çalışan eklendikten sonra, MailService üzerinden yöneticilere bilgi maili gönderilebilir.
    }

    public List<AllEmployeeResponse> findAllEmployees(String token) {
        Employee employee = getEmployeeByToken(token);
        System.out.println(employee);
        return employeeRepository.findAllEmployee(employee.getCompanyId());
    }

    public EmployeeDetailResponse findEmployeeDetailById(String token, Long employeeId) {
        Employee manager = getEmployeeByToken(token);
        Long employeeCompanyId = employeeRepository.findCompanyIdByEmployeeId(employeeId).orElseThrow(() -> new OrganisationManagementException(ErrorType.EMPLOYEE_NOT_FOUND));
        checkCompany(manager.getCompanyId(), employeeCompanyId);
        return employeeRepository.findEmployeeDetail(employeeId).orElseThrow(() -> new OrganisationManagementException(ErrorType.EMPLOYEE_NOT_FOUND));
    }

    public List<AllEmployeeResponse> findEmployeeHierarchy(String token, Long employeeId) {
        Employee employeeByToken = getEmployeeByToken(token);
        Employee employee = employeeRepository.findById(employeeId).orElseThrow(() -> new OrganisationManagementException(ErrorType.EMPLOYEE_NOT_FOUND));
        checkCompany(employee.getCompanyId(), employeeByToken.getCompanyId());

        List<AllEmployeeResponse> hierarchyList = new ArrayList<>();

        Position position = positionService.findById(employee.getPositionId());
        Department department = departmentService.findById(position.getDepartmentId());
        Long parentDepartmentId = department.getParentDepartmentId();


        while (parentDepartmentId != null) {

            Department managerDepartment = departmentService.findById(department.getParentDepartmentId());
            Employee manager = findById(managerDepartment.getManagerId());

            hierarchyList.add(new AllEmployeeResponse(
                    manager.getId(),
                    manager.getFirstName(),
                    manager.getLastName(),
                    manager.getRole().name(),
                    managerDepartment.getName()
            ));

            parentDepartmentId = managerDepartment.getParentDepartmentId();
            System.out.println(parentDepartmentId);

        }
        return hierarchyList;
    }

    public List<AllEmployeeResponse> findEmployeeSubordinates(String token, Long employeeId) {
        Employee employeeByToken = getEmployeeByToken(token);
        Long employeeCompanyId = employeeRepository.findCompanyIdByEmployeeId(employeeId).orElseThrow(() -> new OrganisationManagementException(ErrorType.EMPLOYEE_NOT_FOUND));
        if (!employeeByToken.getCompanyId().equals(employeeCompanyId))
            throw new OrganisationManagementException(ErrorType.UNAUTHORIZED);
        return employeeRepository.findAllEmployeeSubordinatesByManagerId(employeeId);
    }


    @Transactional
    public boolean deleteEmployee(String token, Long employeeId) {
        Employee manager = getEmployeeByToken(token);
        Employee employee = employeeRepository.findById(employeeId).orElseThrow(() -> new OrganisationManagementException(ErrorType.EMPLOYEE_NOT_FOUND));
        checkCompany(manager.getCompanyId(), employee.getCompanyId());
        checkDepartmentOfManager(manager, employee);
        employee.setState(EState.PASSIVE);
        employeeRepository.save(employee);
        return true;
    }

    @Transactional
    public boolean updateEmployee(String token, UpdateEmployeeRequest dto) {
        Employee manager = getEmployeeByToken(token);
        Employee employee = employeeRepository.findById(dto.employeeId()).orElseThrow(() -> new OrganisationManagementException(ErrorType.EMPLOYEE_NOT_FOUND));
        checkCompany(manager.getCompanyId(), employee.getCompanyId());
        checkDepartmentOfManager(manager, employee);
        employee.setFirstName(dto.firstName());
        employee.setLastName(dto.lastName());
        employee.setEmail(dto.email());
        employee.setRole(dto.role());
        employeeRepository.save(employee);
        return true;
    }

    public Employee findById(Long managerId) {
        return employeeRepository.findById(managerId).orElseThrow(() -> new OrganisationManagementException(ErrorType.EMPLOYEE_NOT_FOUND));
    }


    public Employee getEmployeeByToken(String token) {
        Long authId = jwtManager.getIdFromToken(token.substring(7)).orElseThrow(() -> new OrganisationManagementException(ErrorType.INVALID_TOKEN));
        return employeeRepository.findByAuthId(authId).orElseThrow(() -> new OrganisationManagementException(ErrorType.EMPLOYEE_NOT_FOUND));
    }

    private void checkCompany(Long managerCompanyId, Long departmentCompanyId) {
        if (!managerCompanyId.equals(departmentCompanyId))
            throw new OrganisationManagementException(ErrorType.UNAUTHORIZED);
    }

    private void checkDepartmentOfManager(Employee manager, Employee employee) {
        if (manager.getRole().equals(EmployeeRole.DEPARTMENT_MANAGER)) {
            Long departmentIdOfManager = positionService.findDepartmentIdByPositionId(manager.getPositionId());
            Long departmentIdOfEmployee = positionService.findDepartmentIdByPositionId(employee.getPositionId());
            if (!departmentIdOfManager.equals(departmentIdOfEmployee))
                throw new OrganisationManagementException(ErrorType.UNAUTHORIZED);
        }
    }

    public void save(Employee departmentManager) {
        employeeRepository.save(departmentManager);
    }

    public List<AllEmployeeResponse> findAllEmployeesByDepartmentId(Long departmentId) {
        return employeeRepository.findAllEmployeesByDepartmentId(departmentId);
    }

    public String findCeoNameByCompanyId(Long companyId) {
        return employeeRepository.findCEOByCompanyId(companyId).orElseThrow(() -> new OrganisationManagementException(ErrorType.EMPLOYEE_NOT_FOUND));
    }

    public Optional<VwEmployee> findVwManagerByDepartmentId(Long departmentId) {
        return employeeRepository.findVwManagerByDepartmentId(departmentId);
    }

    public List<VwEmployee> findAllVwEmployeesByDepartmentId(Long departmentId) {
        return employeeRepository.findAllVwEmployeesByDepartmentId(departmentId);
    }

    public Employee findCompanyManager(Long companyId) {
        return employeeRepository.findCompanyManagerByCompanyId(companyId, EmployeeRole.COMPANY_OWNER)
                .orElseThrow(() -> new OrganisationManagementException(ErrorType.EMPLOYEE_NOT_FOUND));
    }


}
