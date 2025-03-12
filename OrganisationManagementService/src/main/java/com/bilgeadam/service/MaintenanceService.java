package com.bilgeadam.service;

import com.bilgeadam.dto.request.CreateMaintenanceRequestDto;
import com.bilgeadam.dto.request.UpdateMaintenanceRequestDto;
import com.bilgeadam.dto.response.MaintenanceResponseDto;
import com.bilgeadam.entity.Employee;
import com.bilgeadam.entity.Maintenance;
import com.bilgeadam.entity.enums.EState;
import com.bilgeadam.exception.ErrorType;
import com.bilgeadam.exception.OrganisationManagementException;
import com.bilgeadam.repository.MaintenanceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MaintenanceService {
    private final MaintenanceRepository maintenanceRepository;
    private final EmployeeService employeeService;

    public Boolean createMaintenance(CreateMaintenanceRequestDto dto, String token){
        Employee employee = employeeService.getEmployeeByToken(token);

        Maintenance maintenance = Maintenance.builder()
                .companyId(employee.getCompanyId())
                .employeeId(dto.employeeId())
                .type(dto.type())
                .description(dto.description())
                .build();
        maintenanceRepository.save(maintenance);
        return true;
    }

    public List<MaintenanceResponseDto> getAllMaintenance(String token){
        employeeService.getEmployeeByToken(token);
        List<Maintenance> maintenances = maintenanceRepository.findAllByState(EState.ACTIVE);

        List<MaintenanceResponseDto> maintenanceResponseDtos = new ArrayList<>();
        for(Maintenance maintenance : maintenances){

            Employee employee = employeeService.findById(maintenance.getEmployeeId());

            MaintenanceResponseDto dto = MaintenanceResponseDto.builder()
                    .id(maintenance.getId())
                    .employeeId(employee.getId())
                    .type(maintenance.getType())
                    .description(maintenance.getDescription())
                    .firstName(employee.getFirstName())
                    .lastName(employee.getLastName())
                    .build();

            maintenanceResponseDtos.add(dto);
        }
        return maintenanceResponseDtos;
    }

    public Boolean updateMaintenance(UpdateMaintenanceRequestDto dto, String token){
        employeeService.getEmployeeByToken(token);

        Optional<Maintenance> optionalMaintenance = maintenanceRepository.findById(dto.id());
        if(optionalMaintenance.isEmpty()){
            throw new OrganisationManagementException(ErrorType.MAINTENANCE_NOT_FOUND);
        }
        Maintenance maintenance = optionalMaintenance.get();
        maintenance.setType(dto.type());
        maintenance.setDescription(dto.description());
        maintenance.setEmployeeId(dto.employeeId());
        maintenanceRepository.save(maintenance);
        return true;
    }

    public Boolean deleteMaintenance(String token, Long maintenanceId){
        employeeService.getEmployeeByToken(token);

        Optional<Maintenance> optionalMaintenance = maintenanceRepository.findById(maintenanceId);
        if(optionalMaintenance.isEmpty()){
            throw new OrganisationManagementException(ErrorType.MAINTENANCE_NOT_FOUND);
        }
        Maintenance maintenance = optionalMaintenance.get();
        maintenance.setState(EState.PASSIVE);
        maintenanceRepository.save(maintenance);
        return true;
    }

}
