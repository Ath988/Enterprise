package com.bilgeadam.service;

import com.bilgeadam.dto.request.AddEmployeeRequest;
import com.bilgeadam.dto.request.CreateCompanyManagerRequest;
import com.bilgeadam.dto.request.ManageEmployeePermissionsRequest;
import com.bilgeadam.dto.request.UpdateEmployeeRequest;
import com.bilgeadam.dto.response.AllEmployeeResponse;
import com.bilgeadam.dto.response.BaseResponse;
import com.bilgeadam.dto.response.EmployeeDetailResponse;
import com.bilgeadam.dto.response.EmployeeSaveResponse;
import com.bilgeadam.entity.Department;
import com.bilgeadam.entity.Employee;
import com.bilgeadam.entity.Position;
import com.bilgeadam.entity.enums.EState;
import com.bilgeadam.entity.enums.EmployeeRole;
import com.bilgeadam.exception.ErrorType;
import com.bilgeadam.exception.OrganisationManagementException;
import com.bilgeadam.manager.AuthManager;
import com.bilgeadam.manager.UserManager;
import com.bilgeadam.repository.EmployeeRepository;
import com.bilgeadam.utility.JwtManager;
import com.bilgeadam.view.VwEmployee;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.bilgeadam.dto.response.BaseResponse.*;

import java.util.*;
import java.util.stream.Collectors;

@Service

public class EmployeeService {
    private final EmployeeRepository employeeRepository;
    private final DepartmentService departmentService;
    private final PositionService positionService;
    private final JwtManager jwtManager;
    private final AuthManager authManager;
    private final UserManager userManager;
    
    public EmployeeService(EmployeeRepository employeeRepository, @Lazy DepartmentService departmentService, PositionService positionService, JwtManager jwtManager, AuthManager authManager, UserManager userManager) {
        this.employeeRepository = employeeRepository;
        this.departmentService = departmentService;
        this.positionService = positionService;
        this.jwtManager = jwtManager;
        this.authManager = authManager;
        this.userManager = userManager;
    }
    
    
    @Transactional
    public Boolean createCompanyManager(CreateCompanyManagerRequest dto) {
        Employee companyManager = Employee.builder()
                                          .authId(dto.authId())
                                          .companyId(dto.authId())
                                          .positionId(1L) // BAŞKAN Position
                                          .email(dto.email())
                                          .role(EmployeeRole.COMPANY_OWNER)
                                          .build();
        employeeRepository.save(companyManager);
        return true;
    }
    
    
    @Transactional
    public EmployeeSaveResponse addNewEmployee(String token, AddEmployeeRequest dto) {
        
        Employee manager = getEmployeeByToken(token);
        Position position = positionService.findById(dto.positionId());
        if (manager.getRole().equals(EmployeeRole.DEPARTMENT_MANAGER)) {
            //Eğer yeni çalışan eklicek kişi departman müdürüyse sadece kendi departmanına ekleyebilsin.
            Long departmentIdOfManager = positionService.findDepartmentIdByPositionId(manager.getPositionId());
            if (departmentIdOfManager.equals(position.getDepartmentId()))
                throw new OrganisationManagementException(ErrorType.UNAUTHORIZED);
        }
        
        //Eklenilen çalışanı authService'e kaydediyoruz.
        Long authId = getDataFromResponse(authManager.register(token, dto.registerRequestDto()));
        
        Employee employee = Employee.builder()
                                    .authId(authId)
                                    .positionId(dto.positionId())
                                    .email(dto.registerRequestDto().email())
                                    .firstName(dto.firstname())
                                    .lastName(dto.lastname())
                                    .companyId(manager.getCompanyId())
                                    .role(dto.role())
                                    .gender(dto.gender())
                                    .build();
        employeeRepository.save(employee);
        return new EmployeeSaveResponse(employee.getId(), employee.getCompanyId());
        
        //Todo: Yeni çalışan eklendikten sonra, MailService üzerinden yöneticilere bilgi maili gönderilebilir.
    }
    
    public List<AllEmployeeResponse> findAllEmployees(String token, Optional<EState> state) {
        Employee employee = getEmployeeByToken(token);
        if (state.isPresent()) {
            return employeeRepository.findAllActiveEmployee(employee.getCompanyId(), state.get());
        }
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
                    managerDepartment.getName(),
                    manager.getGender(),
                    manager.getState()
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
        employee.setGender(dto.gender());
        employeeRepository.save(employee);
        return true;
    }
    
    public Employee findById(Long managerId) {
        return employeeRepository.findById(managerId).orElseThrow(() -> new OrganisationManagementException(ErrorType.EMPLOYEE_NOT_FOUND));
    }
    
    
    public Employee getEmployeeByToken(String token) {
        Long authId = jwtManager.validateToken(token.substring(7)).orElseThrow(() -> new OrganisationManagementException(ErrorType.INVALID_TOKEN));
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
    
    
    public String getEmployeeNameByToken(String token, Optional<Long> employeeId) {
        Employee manager = getEmployeeByToken(token);
        if (employeeId.isPresent()) {
            Long companyId = employeeRepository.findCompanyIdByEmployeeId(employeeId.get()).orElseThrow(()
                                                                                                                -> new OrganisationManagementException(ErrorType.EMPLOYEE_NOT_FOUND));
            checkCompany(manager.getCompanyId(), companyId);
            return employeeRepository.findEmployeeNameByEmployeeId(employeeId.get())
                                     .orElseThrow(() -> new OrganisationManagementException(ErrorType.EMPLOYEE_NOT_FOUND));
        }
        return manager.getFirstName() + " " + manager.getLastName();
    }
    
    public Boolean checkCompanyByToken(String token, Long employeeId) {
        Employee manager = getEmployeeByToken(token);
        Long employeeCompanyId = employeeRepository.findCompanyIdByEmployeeId(employeeId)
                                                   .orElseThrow(() -> new OrganisationManagementException(ErrorType.EMPLOYEE_NOT_FOUND));
        return manager.getCompanyId().equals(employeeCompanyId);
    }
    
    public Long getEmployeeIdFromToken(String token) {
        Employee manager = getEmployeeByToken(token);
        return manager.getId();
    }
    
    public Map<Long, String> findAllEmployeeNamesByEmployeeIdList(List<Long> employeeIdList) {
        List<Object[]> objectList = employeeRepository.findAllEmployeeNamesFromEmployeeIdList(employeeIdList);
        return objectList.stream()
                         .collect(Collectors.toMap(
                                 row -> (Long) (row != null && row[0] != null ? row[0] : -1L),
                                 row -> (row != null && row[1] != null ? (String) row[1] : "UNKNOWN")
                         ));
    }
    
    
    public Boolean updateEmployeePermissions(String token, ManageEmployeePermissionsRequest dto) {
        Employee manager = getEmployeeByToken(token);
        Long companyId = manager.getCompanyId();
        String organization = dto.organization().toLowerCase();
        List<Long> employeeAuthIdList = switch (organization) {
            case "department" ->
                    employeeRepository.findAllEmployeeAuthIdByDepartmentIdListAndCompanyIdAndState(dto.idList(), companyId, EState.ACTIVE);
            case "position" ->
                    employeeRepository.findAllEmployeeAuthIdByPositionIdAndCompanyIdListAndState(dto.idList(), companyId, EState.ACTIVE);
            default -> throw new OrganisationManagementException(ErrorType.VALIDATION_ERROR);
        };
        
        getSuccessFromResponse(userManager.manageUserPermissions(new ManageEmployeePermissionsRequest(null, employeeAuthIdList, dto.grantedPermissions())));
        
        return true;
    }
    
    public Long getCompanyIdByAuthId(Long authId) {
        return employeeRepository.findCompanyIdByAuthId(authId);
    }
}