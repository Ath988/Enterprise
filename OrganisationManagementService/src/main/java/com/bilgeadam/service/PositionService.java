package com.bilgeadam.service;

import com.bilgeadam.dto.request.AddNewPositionRequest;
import com.bilgeadam.dto.request.AssignPositionToEmployeeListRequest;
import com.bilgeadam.dto.request.UpdatePositionRequest;
import com.bilgeadam.dto.response.PositionDetailResponse;
import com.bilgeadam.dto.response.PositionTreeResponse;
import com.bilgeadam.entity.Department;
import com.bilgeadam.entity.Employee;
import com.bilgeadam.entity.Position;
import com.bilgeadam.entity.enums.EState;
import com.bilgeadam.exception.ErrorType;
import com.bilgeadam.exception.OrganisationManagementException;
import com.bilgeadam.repository.EmployeeRepository;
import com.bilgeadam.repository.PositionRepository;
import com.bilgeadam.view.VwPosition;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;

@Service
public class PositionService {

    private final PositionRepository positionRepository;
    private final EmployeeService employeeService;
    private final DepartmentService departmentService;
    private final EmployeeRepository employeeRepository;
    
    public PositionService(PositionRepository positionRepository, @Lazy EmployeeService employeeService, @Lazy DepartmentService departmentService, EmployeeRepository employeeRepository) {
        this.positionRepository = positionRepository;
        this.employeeService = employeeService;
        this.departmentService = departmentService;
        this.employeeRepository = employeeRepository;
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

    public Boolean assignPositionToEmployeeList(String token, AssignPositionToEmployeeListRequest dto){
        Employee manager = employeeService.getEmployeeByToken(token);
        //Pozisyon var mı yok mu kontrol edilebilir.
        for(Long employeeId : dto.employeeIdList()){

            Employee employee = employeeService.findById(employeeId);
            checkCompany(manager.getCompanyId(), employee.getCompanyId());
            employee.setPositionId(dto.positionId());
            employee.setUpdateAt(LocalDateTime.now());
            employeeService.save(employee);
        }
        return true;
    }
    
    public PositionTreeResponse getPositionTree(Long companyId) {
        String companyName = "KOÇ HOLDİNG";
        String CEO = employeeService.findCeoNameByCompanyId(companyId);
        
        PositionTreeResponse response = PositionTreeResponse.builder()
                                                            .companyId(companyId)
                                                            .companyName(companyName)
                                                            .CEO(CEO)
                                                            .positions(new ArrayList<>())
                                                            .build();
        List<VwPosition> rootPositions = positionRepository.findAllVwPositionsByParentPositionId(1L);
        response.getPositions().addAll(rootPositions);
        
        Queue<VwPosition> positionQueue = new LinkedList<>(rootPositions);
        
        while (!positionQueue.isEmpty()) {
            VwPosition currentPosition = positionQueue.poll();
            
            currentPosition.setEmployees(employeeRepository.findAllEmployeeByPositionId(currentPosition.getPositionId()));
            List<VwPosition> subPositions = positionRepository.findAllVwPositionsByParentPositionId(currentPosition.getPositionId());
            if (!subPositions.isEmpty()) {
                currentPosition.setSubPositions(new ArrayList<>(subPositions));
                positionQueue.addAll(subPositions);
                for (VwPosition subPosition : subPositions) {
                    subPosition.setEmployees(employeeRepository.findAllEmployeeByPositionId(subPosition.getPositionId()));
                }
            }
        }
        return response;
    }
    
    
    
    
}