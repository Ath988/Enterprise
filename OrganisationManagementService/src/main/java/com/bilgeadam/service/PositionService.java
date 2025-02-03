package com.bilgeadam.service;

import com.bilgeadam.dto.request.AddNewPositionRequest;
import com.bilgeadam.dto.request.UpdatePositionRequest;
import com.bilgeadam.dto.response.PositionDetailResponse;
import com.bilgeadam.entity.Department;
import com.bilgeadam.entity.Employee;
import com.bilgeadam.entity.Position;
import com.bilgeadam.entity.enums.EState;
import com.bilgeadam.exception.ErrorType;
import com.bilgeadam.exception.OrganisationManagementException;
import com.bilgeadam.repository.PositionRepository;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class PositionService {

    private final PositionRepository positionRepository;
    private final EmployeeService employeeService;
    private final DepartmentService departmentService;

    public PositionService(PositionRepository positionRepository, @Lazy EmployeeService employeeService,@Lazy DepartmentService departmentService) {
        this.positionRepository = positionRepository;
        this.employeeService = employeeService;
        this.departmentService = departmentService;
    }

    public Boolean addNewPosition(String token, AddNewPositionRequest dto) {
        Employee manager = employeeService.getEmployeeByToken(token);
        Department department = departmentService.findById(dto.departmentId());
        //Farklı bir firmanın departmanına ekleme yapamasın.
        checkCompany(manager.getCompanyId(), department.getCompanyId());
        Position position = Position.builder()
                .title(dto.title())
                .description(dto.description())
                .departmentId(dto.departmentId())
                .companyId(manager.getCompanyId())
                .build();
        positionRepository.save(position);
        return true;
    }

    public Boolean updatePosition(String token, UpdatePositionRequest dto) {
        Employee manager = employeeService.getEmployeeByToken(token);
        Position position = findById(dto.positionId());
        if (!position.getDepartmentId().equals(dto.departmentId())) {
            //Departmanını değiştirmek istedğimiz adamın eski departmanı yönetici ile aynı şirkete mi ait kontrolü
            Department oldDepartment = departmentService.findById(position.getDepartmentId());
            checkCompany(manager.getCompanyId(), oldDepartment.getCompanyId());
        }
        Department department = departmentService.findById(dto.departmentId());
        //Dtodaki departman aynı firmada mı kontrolü.
        checkCompany(manager.getCompanyId(), department.getCompanyId());
        position.setDepartmentId(dto.departmentId());
        position.setDescription(dto.description());
        position.setTitle(dto.title());
        position.setUpdateAt(LocalDateTime.now());
        positionRepository.save(position);
        return true;
    }

    public Boolean deletePosition(String token,Long positionId){
        Employee manager = employeeService.getEmployeeByToken(token);
        Position position = findById(positionId);
        if(position.getCompanyId().equals(manager.getCompanyId())){
            position.setState(EState.PASSIVE);
            position.setUpdateAt(LocalDateTime.now());
            positionRepository.save(position);
            return true;
        }
        throw new OrganisationManagementException(ErrorType.UNAUTHORIZED);
    }




    public Position findById(Long positionId) {
        return positionRepository.findById(positionId).orElseThrow(() -> new OrganisationManagementException(ErrorType.POSITION_NOT_FOUND));
    }

    public Long findDepartmentIdByPositionId(Long positionId) {
        return positionRepository.findDepartmentIdByPositionId(positionId).orElseThrow(() -> new OrganisationManagementException(ErrorType.DEPARTMENT_NOT_FOUND));
    }

    private void checkCompany(Long employeeCompanyId, Long departmentCompanyId) {
        if (!employeeCompanyId.equals(departmentCompanyId))
            throw new OrganisationManagementException(ErrorType.UNAUTHORIZED);
    }

    public PositionDetailResponse findPositionDetailById(Long positionId) {
        return positionRepository.findPositionDetailById(positionId).orElseThrow(() -> new OrganisationManagementException(ErrorType.POSITION_NOT_FOUND));
    }

    public List<PositionDetailResponse> findAllPositionsByCompanyId(Long companyId) {
        return positionRepository.findAllPositionsByCompanyId(companyId);
    }

    public List<PositionDetailResponse> findAllPositionsByDepartmentId(Long departmentId) {
        return positionRepository.findAllPositionsByDepartmentId(departmentId);
    }

    public List<String> findAllPositionNamesByDepartmentId(Long departmentId) {
        return positionRepository.findAllPositionNamesByDepartmentId(departmentId);
    }

    public void save(Position position) {
        positionRepository.save(position);
    }
}
