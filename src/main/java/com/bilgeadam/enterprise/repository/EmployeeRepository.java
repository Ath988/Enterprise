package com.bilgeadam.enterprise.repository;

import com.bilgeadam.enterprise.dto.response.AllEmployeeResponse;
import com.bilgeadam.enterprise.dto.response.EmployeeDetailResponse;
import com.bilgeadam.enterprise.entity.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface EmployeeRepository extends JpaRepository<Employee,Long> {

    @Query("SELECT new com.bilgeadam.enterprise.dto.response.AllEmployeeResponse(e.firstName,e.lastName,e.employeeRole,d.name) FROM Employee e JOIN Department d ON e.departmentId=d.id WHERE e.companyId = ?1")
    List<AllEmployeeResponse> findAllEmployee(Long companyId);

    @Query("SELECT new com.bilgeadam.enterprise.dto.response.EmployeeDetailResponse(e.firstName,e.lastName,e.email,e.hireDate,e.employeeRole,d.name) FROM Employee e JOIN Department d ON d.id=e.departmentId WHERE ?1 = e.id")
    Optional<EmployeeDetailResponse> findEmployeeDetail(Long employeeId);


    @Query("SELECT new com.bilgeadam.enterprise.dto.response.AllEmployeeResponse(e.firstName,e.lastName,e.employeeRole,d.name) FROM Employee e JOIN Department d ON d.id = e.departmentId WHERE e.managerEmployeeId = ?1")
    List<AllEmployeeResponse> findAllEmployeeSubordinatesByManagerId(Long employeeId);

}
