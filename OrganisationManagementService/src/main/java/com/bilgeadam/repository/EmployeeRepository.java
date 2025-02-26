package com.bilgeadam.repository;

import com.bilgeadam.dto.response.AllEmployeeResponse;
import com.bilgeadam.dto.response.EmployeeDetailResponse;
import com.bilgeadam.entity.Employee;
import com.bilgeadam.entity.enums.EState;
import com.bilgeadam.entity.enums.EmployeeRole;
import com.bilgeadam.view.VwEmployee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface EmployeeRepository extends JpaRepository<Employee,Long> {

    @Query("SELECT NEW com.bilgeadam.dto.response.AllEmployeeResponse(e.id,e.firstName,e.lastName,p.title,d.name,e.gender,e.state) FROM Employee e JOIN Position p ON p.id = e.positionId JOIN Department d ON p.departmentId=d.id " +
            "WHERE e.companyId = ?1 AND e.state = ?2 ORDER BY e.updateAt DESC")
    List<AllEmployeeResponse> findAllActiveEmployee(Long companyId, EState state);

    @Query("SELECT NEW com.bilgeadam.dto.response.AllEmployeeResponse(e.id,e.firstName,e.lastName,p.title,d.name,e.gender,e.state) FROM Employee e JOIN Position p ON p.id = e.positionId JOIN Department d ON p.departmentId=d.id " +
            "WHERE e.companyId = ?1 ORDER BY e.updateAt DESC")
    List<AllEmployeeResponse> findAllEmployee(Long companyId);

    @Query("SELECT NEW com.bilgeadam.dto.response.EmployeeDetailResponse(e.id,e.firstName,e.lastName,e.email,p.title,d.name,e.gender) FROM Employee e JOIN Position p ON p.id = e.positionId JOIN Department d ON p.departmentId=d.id WHERE ?1 = e.id")
    Optional<EmployeeDetailResponse> findEmployeeDetail(Long employeeId);

    @Query("SELECT NEW com.bilgeadam.dto.response.AllEmployeeResponse(e.id,e.firstName,e.lastName,p.title,d.name,e.gender,e.state) FROM Employee e JOIN Position p ON p.id = e.positionId JOIN Department d ON p.departmentId=d.id " +
            "WHERE d.managerId = ?1 AND e.state = com.bilgeadam.entity.enums.EState.ACTIVE AND e.role = com.bilgeadam.entity.enums.EmployeeRole.EMPLOYEE ORDER BY e.updateAt DESC")
    List<AllEmployeeResponse> findAllEmployeeSubordinatesByManagerId(Long employeeId);

    Optional<Employee> findByAuthId(Long authId);

    @Query("SELECT e.companyId FROM Employee e WHERE e.id = ?1")
    Optional<Long> findCompanyIdByEmployeeId(Long employeeId);

    Optional<Employee> findOptionalById(Long id);

    @Query("SELECT NEW com.bilgeadam.dto.response.AllEmployeeResponse(e.id,e.firstName,e.lastName,p.title,d.name,e.gender,e.state) FROM Employee e JOIN Position p ON p.id = e.positionId JOIN Department d ON p.departmentId=d.id " +
            "WHERE d.id = ?1 AND e.state = com.bilgeadam.entity.enums.EState.ACTIVE ORDER BY e.updateAt DESC")
    List<AllEmployeeResponse> findAllEmployeesByDepartmentId(Long departmentId);

    @Query("SELECT CONCAT(e.firstName,' ',e.lastName) FROM Employee e WHERE e.companyId = ?1 AND e.role = com.bilgeadam.entity.enums.EmployeeRole.COMPANY_OWNER")
    Optional<String> findCEOByCompanyId(Long companyId);

    @Query("SELECT NEW com.bilgeadam.view.VwEmployee(e.id,CONCAT(e.firstName,' ',e.lastName)) FROM Employee e JOIN Position p ON p.id = e.positionId JOIN Department d ON p.departmentId = d.id WHERE d.managerId = e.id AND d.id = ?1")
    Optional<VwEmployee> findVwManagerByDepartmentId(Long departmentId);

    @Query("SELECT NEW com.bilgeadam.view.VwEmployee(e.id,CONCAT(e.firstName,' ',e.lastName)) FROM Employee e JOIN Position p ON p.id = e.positionId JOIN Department d ON d.id = p.departmentId " +
            "WHERE d.id = ?1 AND e.role = com.bilgeadam.entity.enums.EmployeeRole.EMPLOYEE AND e.state = com.bilgeadam.entity.enums.EState.ACTIVE ORDER BY e.updateAt DESC")
    List<VwEmployee> findAllVwEmployeesByDepartmentId(Long departmentId);


    @Query("SELECT Employee FROM Employee e WHERE e.companyId = ?1 AND e.role = ?2")
    Optional<Employee> findCompanyManagerByCompanyId(Long companyId, EmployeeRole role);

    @Query("SELECT CONCAT(e.firstName, ' ',e.lastName) FROM Employee e WHERE e.id = ?1")
    Optional<String> findEmployeeNameByEmployeeId(Long employeeId);

    @Query("SELECT e.id,CONCAT(e.firstName,' ',e.lastName) FROM Employee e WHERE e.id IN ?1 ORDER BY e.updateAt DESC")
    List<Object[]> findAllEmployeeNamesFromEmployeeIdList(List<Long> employeeIdList);

    @Query("SELECT e.authId From Employee e JOIN Position p ON e.positionId=p.id JOIN Department d ON d.id = p.departmentId " +
            "WHERE d.id IN ?1 AND e.companyId = ?2 AND e.state=?3 ")
    List<Long> findAllEmployeeAuthIdByDepartmentIdListAndCompanyIdAndState(List<Long> idList,Long companyId, EState state);

    @Query("SELECT e.authId From Employee e JOIN Position p ON e.positionId = p.id WHERE p.id  IN ?1 AND e.companyId = ?2 AND e.state = ?3")
    List<Long> findAllEmployeeAuthIdByPositionIdAndCompanyIdListAndState(List<Long> idList,Long companyId, EState eState);

    @Query("SELECT NEW com.bilgeadam.view.VwEmployee(e.id,CONCAT(e.firstName,' ',e.lastName)) FROM Employee e " +
            "WHERE e.positionId=?1")
    List<VwEmployee> findAllEmployeeByPositionId(Long positionId);
    
    @Query("SELECT e.companyId FROM Employee e WHERE e.authId = :authId")
    Long findCompanyIdByAuthId(@Param("authId") Long authId);
}