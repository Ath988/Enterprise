package com.bilgeadam.repository;

import com.bilgeadam.dto.response.AllEmployeeResponse;
import com.bilgeadam.dto.response.EmployeeDetailResponse;
import com.bilgeadam.entity.Employee;
import com.bilgeadam.view.VwEmployee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface EmployeeRepository extends JpaRepository<Employee,Long> {

    @Query("SELECT NEW com.bilgeadam.dto.response.AllEmployeeResponse(e.id,e.firstName,e.lastName,p.title,d.name) FROM Employee e JOIN Position p ON p.id = e.positionId JOIN Department d ON p.departmentId=d.id WHERE e.companyId = ?1 AND e.state = com.bilgeadam.entity.enums.EState.ACTIVE")
    List<AllEmployeeResponse> findAllEmployee(Long companyId);

    @Query("SELECT NEW com.bilgeadam.dto.response.EmployeeDetailResponse(e.id,e.firstName,e.lastName,e.email,e.createAt,p.title,d.name) FROM Employee e JOIN Position p ON p.id = e.positionId JOIN Department d ON p.departmentId=d.id WHERE ?1 = e.id")
    Optional<EmployeeDetailResponse> findEmployeeDetail(Long employeeId);

    @Query("SELECT NEW com.bilgeadam.dto.response.AllEmployeeResponse(e.id,e.firstName,e.lastName,p.title,d.name) FROM Employee e JOIN Position p ON p.id = e.positionId JOIN Department d ON p.departmentId=d.id WHERE d.managerId = ?1 AND e.state = com.bilgeadam.entity.enums.EState.ACTIVE AND e.role = com.bilgeadam.entity.enums.EmployeeRole.EMPLOYEE")
    List<AllEmployeeResponse> findAllEmployeeSubordinatesByManagerId(Long employeeId);

    Optional<Employee> findByAuthId(Long authId);

    @Query("SELECT e.companyId FROM Employee e WHERE e.id = ?1")
    Optional<Long> findCompanyIdByEmployeeId(Long employeeId);

    Optional<Employee> findOptionalById(Long id);

    @Query("SELECT NEW com.bilgeadam.dto.response.AllEmployeeResponse(e.id,e.firstName,e.lastName,p.title,d.name) FROM Employee e JOIN Position p ON p.id = e.positionId JOIN Department d ON p.departmentId=d.id WHERE d.id = ?1 AND e.state = com.bilgeadam.entity.enums.EState.ACTIVE")
    List<AllEmployeeResponse> findAllEmployeesByDepartmentId(Long departmentId);

    @Query("SELECT CONCAT(e.firstName,' ',e.lastName) FROM Employee e WHERE e.companyId = ?1 AND e.role = com.bilgeadam.entity.enums.EmployeeRole.COMPANY_OWNER")
    Optional<String> findCEOByCompanyId(Long companyId);

    @Query("SELECT NEW com.bilgeadam.view.VwEmployee(e.id,CONCAT(e.firstName,' ',e.lastName),p.title) FROM Employee e JOIN Position p ON p.id = e.positionId JOIN Department d ON p.departmentId = d.id WHERE d.managerId = e.id AND d.id = ?1")
    Optional<VwEmployee> findVwManagerByDepartmentId(Long departmentId);

    @Query("SELECT NEW com.bilgeadam.view.VwEmployee(e.id,CONCAT(e.firstName,' ',e.lastName),p.title) FROM Employee e JOIN Position p ON p.id = e.positionId JOIN Department d ON d.id = p.departmentId WHERE d.id = ?1 AND e.role = com.bilgeadam.entity.enums.EmployeeRole.EMPLOYEE AND e.state = com.bilgeadam.entity.enums.EState.ACTIVE")
    List<VwEmployee> findAllVwEmployeesByDepartmentId(Long departmentId);



}
