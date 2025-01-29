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
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service

public class EmployeeService {
    private final EmployeeRepository employeeRepository;
    private final DepartmentService departmentService;
    private final JwtService jwtService;

    public EmployeeService(EmployeeRepository employeeRepository, @Lazy DepartmentService departmentService, JwtService jwtService) {
        this.employeeRepository = employeeRepository;
        this.departmentService = departmentService;
        this.jwtService = jwtService;
    }


    @Transactional
    public Boolean addNewEmployee(String token, AddEmployeeRequest dto) {
        Employee manager = getEmployeeByToken(token);
        int rank = manager.getEmployeeRole().getRoleRank();
        if (!departmentService.isDepartmentExistById(dto.departmentId()))
            throw new OrganisationManagementException(ErrorType.DEPARTMENT_NOT_FOUND);
        if (isCeoOrDirector(rank)  // Yöneticinin CEO ya da Genel müdür olma durumu
                || //veya dtodaki departmanın müdürü olması koşulu
                isDepartmentManager(rank, manager.getDepartmentId(), dto.departmentId())) {

            Employee employee = Employee.builder()
                    .departmentId(dto.departmentId())
                    .email(dto.email())
                    .firstName(dto.firstName())
                    .lastName(dto.lastName())
                    .employeeRole(dto.role())
                    .build();
            employeeRepository.save(employee);
            return true;
        }
        throw new OrganisationManagementException(ErrorType.UNAUTHORIZED);
        // Yeni çalışan eklendikten sonra, AuthService kullanıcı adı,şifre kayıtları için mail gönderilebilir.
    }

    public List<AllEmployeeResponse> findAllEmployees(String token) {
        Employee employee = getEmployeeByToken(token);
        return employeeRepository.findAllEmployee(employee.getCompanyId());
    }

    public EmployeeDetailResponse findEmployeeDetailById(String token, Long employeeId) {
        Employee manager = getEmployeeByToken(token);
        Long employeeCompanyId = employeeRepository.findCompanyIdByEmployeeId(employeeId).orElseThrow(() -> new OrganisationManagementException(ErrorType.EMPLOYEE_NOT_FOUND));
        if (!manager.getCompanyId().equals(employeeCompanyId))
            //başka firmanın çalışan detaylarına bakamasın.
            throw new OrganisationManagementException(ErrorType.UNAUTHORIZED);
        return employeeRepository.findEmployeeDetail(employeeId).orElseThrow(() -> new OrganisationManagementException(ErrorType.EMPLOYEE_NOT_FOUND));
    }


    public List<AllEmployeeResponse> findEmployeeHierarchy(String token, Long employeeId) {
        Employee employeeByToken = getEmployeeByToken(token);
        Employee employee = employeeRepository.findById(employeeId).orElseThrow(() -> new OrganisationManagementException(ErrorType.EMPLOYEE_NOT_FOUND));
        if (!employeeByToken.getCompanyId().equals(employee.getCompanyId()))
            throw new OrganisationManagementException(ErrorType.UNAUTHORIZED);
        if (employee.getManagerEmployeeId() == null) return new ArrayList<>();
        List<AllEmployeeResponse> hierarchyList = new ArrayList<>();
        Long managerId = employee.getManagerEmployeeId();
        while (managerId != null) {
            Employee manager = employeeRepository.findById(managerId).orElseThrow(() -> new OrganisationManagementException(ErrorType.EMPLOYEE_NOT_FOUND));
            String departmentName = departmentService.getDepartmentNameByDepartmentId(manager.getDepartmentId());
            hierarchyList.add(new AllEmployeeResponse(managerId, manager.getFirstName(), manager.getLastName(), manager.getEmployeeRole(), departmentName));
            managerId = manager.getManagerEmployeeId();
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
    public boolean assignEmployeesToDepartment(String token, AssignEmployeesToDepartmentRequest dto) {
        Employee manager = getEmployeeByToken(token);
        List<Long> employeeIdList = dto.employeeIds();
        Department department = departmentService.findById(dto.departmenId());
        if (!department.getCompanyId().equals(manager.getCompanyId()))
            // Başka firmamların departmanlarına atama yapılamasın
            throw new OrganisationManagementException(ErrorType.UNAUTHORIZED);
        if (isCeoOrDirector(manager.getEmployeeRole().getRoleRank()) // Ceo ya da genel müdür mü
                || // veya atama yapmak istediği departmanın müdürü mü
                (isDepartmentManager(manager.getEmployeeRole().getRoleRank(), manager.getDepartmentId(), department.getId()))) {
            for (Long employeeId : employeeIdList) {
                Employee employee = employeeRepository.findById(employeeId).orElseThrow(() -> new OrganisationManagementException(ErrorType.EMPLOYEE_NOT_FOUND));
                if (!employee.getCompanyId().equals(department.getCompanyId()))
                    throw new OrganisationManagementException(ErrorType.UNAUTHORIZED);
                employee.setDepartmentId(department.getId());
                employeeRepository.save(employee);
            }
            return true;
        }
        throw new OrganisationManagementException(ErrorType.UNAUTHORIZED);
    }

    @Transactional
    public boolean deleteEmployee(String token, Long employeeId) {
        Employee manager = getEmployeeByToken(token);
        Employee employee = employeeRepository.findById(employeeId).orElseThrow(() -> new OrganisationManagementException(ErrorType.EMPLOYEE_NOT_FOUND));
        if (!manager.getCompanyId().equals(employee.getCompanyId()))
            throw new OrganisationManagementException(ErrorType.UNAUTHORIZED);
        if (isCeoOrDirector(manager.getEmployeeRole().getRoleRank()) ||
                (isDepartmentManager(manager.getEmployeeRole().getRoleRank(), manager.getDepartmentId(), employee.getDepartmentId()))) {
            employee.setState(EState.PASSIVE);
            employeeRepository.save(employee);
            return true;
        }
        throw new OrganisationManagementException(ErrorType.UNAUTHORIZED);
    }

    @Transactional
    public boolean updateEmployee(String token,UpdateEmployeeRequest dto) {
        Employee manager = getEmployeeByToken(token);
        Employee employee = employeeRepository.findById(dto.employeeId()).orElseThrow(() -> new OrganisationManagementException(ErrorType.EMPLOYEE_NOT_FOUND));
        if(!employee.getCompanyId().equals(manager.getCompanyId()))
            throw new OrganisationManagementException(ErrorType.UNAUTHORIZED);

        int rank = manager.getEmployeeRole().getRoleRank();
        if(isCeoOrDirector(rank) || isDepartmentManager(rank, manager.getDepartmentId(), employee.getDepartmentId())) {
            employee.setFirstName(dto.firstName());
            employee.setLastName(dto.lastName());
            employee.setEmail(dto.email());
            employee.setManagerEmployeeId(employee.getManagerEmployeeId());
            employee.setDepartmentId(dto.departmentId());
            employeeRepository.save(employee);
            return true;
        }
        throw new OrganisationManagementException(ErrorType.UNAUTHORIZED);
    }

    public Employee findById(Long managerId) {
        return employeeRepository.findById(managerId).orElseThrow(() -> new OrganisationManagementException(ErrorType.EMPLOYEE_NOT_FOUND));
    }

    public String login(Long authId) {
        if (employeeRepository.existsByAuthId(authId)) {
            return jwtService.createUserToken(authId);
        }
        throw new OrganisationManagementException(ErrorType.INVALID_PASSWORD);
    }

    public Employee getEmployeeByToken(String token) {
        Long authId = jwtService.validateToken(token).orElseThrow(() -> new OrganisationManagementException(ErrorType.INVALID_TOKEN));
        return employeeRepository.findByAuthId(authId).orElseThrow(() -> new OrganisationManagementException(ErrorType.EMPLOYEE_NOT_FOUND));
    }

    private boolean isCeoOrDirector(int rank) {
        return rank < 3;
    }

    private boolean isDepartmentManager(int rank, Long managerDepartmentId, Long departmentId) {
        return rank == 3 && managerDepartmentId.equals(departmentId);
    }


}
