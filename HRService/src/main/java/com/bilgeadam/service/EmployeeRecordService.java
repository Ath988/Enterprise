package com.bilgeadam.service;

import com.bilgeadam.dto.request.AddEmployeeRecordRequest;
import com.bilgeadam.dto.request.UpdateEmployeeRecordRequest;
import com.bilgeadam.dto.response.AllEmployeeRecordResponse;
import com.bilgeadam.dto.response.BaseResponse;
import com.bilgeadam.dto.response.EmployeeRecordResponse;
import com.bilgeadam.dto.response.otherServices.AllEmployeeResponse;
import com.bilgeadam.dto.response.otherServices.EmployeeDetailResponse;
import com.bilgeadam.dto.response.otherServices.EmployeeSaveResponse;
import com.bilgeadam.entity.EmployeeRecord;
import com.bilgeadam.entity.enums.EState;
import com.bilgeadam.exception.ErrorType;
import com.bilgeadam.exception.HRException;
import com.bilgeadam.manager.FileManager;
import com.bilgeadam.manager.OrganisationManagementManager;
import com.bilgeadam.repository.EmployeeRecordRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import static com.bilgeadam.dto.response.BaseResponse.*;

@Service
@RequiredArgsConstructor
public class EmployeeRecordService {
    private final EmployeeRecordRepository employeeRecordRepository;
    private final OrganisationManagementManager organisationManagementManager;
    private final FileManager fileManager;


    public Boolean addNewEmployeeRecord(String token, AddEmployeeRecordRequest dto) {

        EmployeeSaveResponse response =
                getDataFromResponse(organisationManagementManager.addEmployee(token, dto.addEmployeeRequest()));

        EmployeeRecord employeeRecord = EmployeeRecord.builder()
                .employeeId(response.employeeId())
                .companyId(response.companyId())
                .startDate(dto.startDate())
                .endDate(dto.endDate())
                .build();
        employeeRecordRepository.save(employeeRecord);
        return true;
    }


    //Todo: Kontrol et, Pagination ile getir.
    public List<AllEmployeeRecordResponse> findAllEmployeeRecord(String token,Optional<EState> state) {
        List<AllEmployeeResponse> employeeListByOrganizationService =
                getDataFromResponse(organisationManagementManager.getAllEmployees(token,state));

        return employeeListByOrganizationService.stream()
                .map(employeeResponse -> {
                    Optional<AllEmployeeRecordResponse> optionalRecord =
                            employeeRecordRepository.findAllEmployeeRecordResponse(employeeResponse.employeeId());

                    if (optionalRecord.isPresent()) { //Çalışan kaydı HR tarafında yoksa hata fırlatmak yerine onu listeye dahil etmiyoruz.
                        AllEmployeeRecordResponse employeeRecord = optionalRecord.get();
                        return AllEmployeeRecordResponse.builder()
                                .allEmployeeResponse(employeeResponse)
                                .employeeRecordId(employeeRecord.getEmployeeRecordId())
                                .startDate(employeeRecord.getStartDate())
                                .endDate(employeeRecord.getEndDate())
                                .personelFileName(employeeRecord.getPersonelFileName())
                                .personelFileUrl(employeeRecord.getPersonelFileUrl())
                                .build();
                    }
                    return null;
                })
                .filter(Objects::nonNull) //Buradada HR tarafında kaydı olmayan ama OrganizationServicete kaydı bulunan çalışanları filtreliyoruz.
                .toList();
    }

    public EmployeeRecordResponse getEmployeeRecord(String token, Long employeeRecordId) {
        EmployeeRecordResponse response = employeeRecordRepository.findEmployeeRecordResponseById(employeeRecordId)
                .orElseThrow(() -> new HRException(ErrorType.EMPLOYEE_RECORD_NOT_FOUND));
        EmployeeDetailResponse employeeDetailResponse
                = getDataFromResponse(organisationManagementManager.getEmployeeDetails(token, response.getEmployeeId()));
        response.setEmployeeDetailResponse(employeeDetailResponse);

        return response;
    }


    public Boolean updateEmployeeRecord(String token, UpdateEmployeeRecordRequest dto) {
        EmployeeRecord employeeRecord = findById(dto.employeeRecordId());

        if (getSuccessFromResponse(organisationManagementManager.updateEmployee(token, dto.updateEmployeeRequest()))) {
            employeeRecord.setStartDate(dto.startDate());
            employeeRecord.setEndDate(dto.endDate());
            employeeRecord.setUpdateAt(LocalDateTime.now());
            employeeRecordRepository.save(employeeRecord);
            return true;
        }
        throw new HRException(ErrorType.INTERNAL_SERVER_ERROR);

    }

    public Boolean deleteEmployeeRecord(String token, Long employeeRecordId) {
        EmployeeRecord employeeRecord = findById(employeeRecordId);
        if (getSuccessFromResponse(organisationManagementManager.deleteEmployee(token, employeeRecord.getEmployeeId()))) {
            employeeRecord.setState(EState.PASSIVE);
            employeeRecord.setUpdateAt(LocalDateTime.now());
            employeeRecordRepository.save(employeeRecord);
            return true;
        }
        throw new HRException(ErrorType.INTERNAL_SERVER_ERROR);
    }

    public Boolean uploadPersonelFile(String token,Long employeeRecordId, MultipartFile file) {
        System.out.println("Metod çağırıldı file upload");
        EmployeeRecord employeeRecord = findById(employeeRecordId);
        System.out.println("emp ID : "+employeeRecord.getEmployeeId());
        if(!getSuccessFromResponse(organisationManagementManager.checkCompanyId(token,employeeRecord.getEmployeeId())))
            throw new HRException(ErrorType.UNAUTHORIZED);


        employeeRecord.setPerfonelFileUrl(getDataFromResponse(fileManager.uploadFile(file)));
        employeeRecord.setPerfonelFileName(file.getOriginalFilename());
        employeeRecordRepository.save(employeeRecord);

        return true;
    }

    public EmployeeRecord findById(Long employeeRecordId) {
        return employeeRecordRepository.findById(employeeRecordId).orElseThrow(() -> new HRException(ErrorType.EMPLOYEE_RECORD_NOT_FOUND));
    }

    public Long findCompanyIdByEmployeeId(Long employeeId){
        return employeeRecordRepository.findCompanyIdByEmployeeId(employeeId)
                .orElseThrow(() -> new HRException(ErrorType.EMPLOYEE_RECORD_NOT_FOUND));
    }

    public Boolean existsByEmployeeIdAndState(Long employeeId) {
        return employeeRecordRepository.existsByEmployeeIdAndState(employeeId,EState.ACTIVE);
    }



}
