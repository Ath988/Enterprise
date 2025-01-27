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
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class DepartmentService {
    private final DepartmentRepository departmentRepository;
    private final EmployeeService employeeService;

    public Boolean addDepartment(AddNewDepartmentRequest dto) {
        //Todo: yönetici tokenindan yetkinlik kontrolü ve yönetici nesnesi çekilecek.
        Department department = Department.builder()
                .name(dto.departmentName())
                .description(dto.description())
                .managerId(1L) //Todo: Tokendan dönülen yöneticinin Idsi set edilecek.
                .companyId(1L) //Todo: Tokendan dönülen yöneticinin şirket Idsi set edilecek.
                .build();
        if(dto.parentDepartmentId()!=null){
            department.setParentDepartmentId(dto.parentDepartmentId());
        }
        departmentRepository.save(department);
        return true;
    }

    public Boolean updateDepartment(UpdateDepartmentRequest dto){
        //Todo: Token kontrolleri
        Department department = departmentRepository.findById(dto.departmentId()).orElseThrow(()->new OrganisationManagementException(ErrorType.DEPARTMENT_NOT_FOUND));
        //Todo: Departman ve yönetici eşleşiyor mu kontrolü
        department.setName(dto.departmentName());
        department.setDescription(dto.description());
        department.setParentDepartmentId(dto.parentDepartmentId());
        //Todo: Departman yönetici değiştirmek için farklı metod yazılabilir
        departmentRepository.save(department);
        return true;
    }

    public Boolean deleteDepartment(Long departmentId) {
        Department department = findById(departmentId);
        department.setState(EState.PASSIVE);
        departmentRepository.save(department);
        return true;
    }

    public DepartmentDetailResponse findDepartmentDetail(Long departmentId){
        Department department = findById(departmentId);
        DepartmentDetailResponse response = departmentRepository.findDepartmentDetailByDepartmentId(departmentId);
        if(department.getParentDepartmentId()!=null){
            if(departmentRepository.existsById(department.getParentDepartmentId())){
                Department parentDepartment = findById(department.getParentDepartmentId());
                response.setParentDepartment(parentDepartment.getName());
            }
        }
        return response;
    }

    public List<AllDepartmentResponse> findAllDepartments(Optional<Long> companyId){
        if(companyId.isPresent()){
            return new ArrayList<>(); //Todo: Düzeltilecek.
        }
        return departmentRepository.findAllDepartments();
    }


    public List<AllDepartmentResponse> findAllSubDepartments(Long departmentId) {
        //Todo: Token kontrolü ve yönetici Id ile departmentIdden gelen departmanın aynı olması kontrolü.
        return departmentRepository.findAllSubDepartments(departmentId);
    }

    public List<AllDepartmentResponse> findAllDepartmentsOfManager(Long managerId) {
        return departmentRepository.findAllDepartmentsOfManagerByManagerId(managerId);
    }

    public List<AllDepartmentResponse> findDepartmentHierarchy(Long departmentId) {
        Department department = findById(departmentId);
        if(department.getParentDepartmentId()==null) return new ArrayList<>();
        List<AllDepartmentResponse> hierarchyList = new ArrayList<>();
        Long topDepartmentId = department.getParentDepartmentId();
        while(topDepartmentId!=null){
            Department topDepartment = findById(topDepartmentId);
            Employee manager = employeeService.findById(department.getManagerId());
            hierarchyList.add(new AllDepartmentResponse(topDepartmentId, topDepartment.getName(),(manager.getFirstName()+" "+manager.getLastName())));
            topDepartmentId = topDepartment.getParentDepartmentId();
        }
        return hierarchyList;
    }

    public Boolean isDepartmentExistById(Long departmentId){
       return departmentRepository.existsById(departmentId);
    }
    public String getDepartmentNameByDepartmentId(Long departmentId) {
        return departmentRepository.findDepartmentNameByDepartmentId(departmentId).orElseThrow(()->new OrganisationManagementException(ErrorType.DEPARTMENT_NOT_FOUND));
    }

    public Department findById(Long departmentId) {
        return departmentRepository.findById(departmentId).orElseThrow(()->new OrganisationManagementException(ErrorType.DEPARTMENT_NOT_FOUND));
    }



}
