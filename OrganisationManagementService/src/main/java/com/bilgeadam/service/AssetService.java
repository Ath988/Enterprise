package com.bilgeadam.service;

import com.bilgeadam.dto.request.AddAssetRequestDto;
import com.bilgeadam.dto.request.UpdateAssetRequestDto;
import com.bilgeadam.dto.response.AllEmployeeResponse;
import com.bilgeadam.dto.response.AssetResponseDto;
import com.bilgeadam.entity.Asset;
import com.bilgeadam.entity.Employee;
import com.bilgeadam.entity.enums.EState;
import com.bilgeadam.exception.ErrorType;
import com.bilgeadam.exception.OrganisationManagementException;
import com.bilgeadam.repository.AssetRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AssetService {

    private final AssetRepository assetRepository;
    private final EmployeeService employeeService;


    public Boolean createAsset(AddAssetRequestDto dto, String token) {
        Employee employee = employeeService.getEmployeeByToken(token);

        Asset asset = Asset.builder()
                .description(dto.description())
                .companyId(employee.getCompanyId())
                .employeeId(dto.employeeId())
                .type(dto.type())
                .givenDate(dto.givenDate())
                .build();
        assetRepository.save(asset);
        return true;
    }

    public List<AllEmployeeResponse> getAllEmployees(String token, Optional<EState> state) {
        return employeeService.findAllEmployees(token, state);
    }

    public List<AssetResponseDto> getAllAssets(String token){
        employeeService.getEmployeeByToken(token);
        List<Asset> assetList = assetRepository.findAllByState(EState.ACTIVE);

        List<AssetResponseDto> assetResponseDtoList = new ArrayList<>();
        for (Asset asset : assetList) {

            Employee employee = employeeService.findById(asset.getEmployeeId());

            AssetResponseDto dto = AssetResponseDto.builder()
                    .id(asset.getId())
                    .employeeId(employee.getId())
                    .firstName(employee.getFirstName())
                    .lastName(employee.getLastName())
                    .type(asset.getType())
                    .description(asset.getDescription())
                    .status(asset.getStatus())
                    .givenDate(asset.getGivenDate())
                    .build();
            assetResponseDtoList.add(dto);
        }
        return assetResponseDtoList;
    }

    public Boolean updateAsset(UpdateAssetRequestDto dto, String token){
        employeeService.getEmployeeByToken(token);

        Optional<Asset> optionalAsset = assetRepository.findById(dto.assetId());
        if(optionalAsset.isEmpty()){
            throw new OrganisationManagementException(ErrorType.ASSET_NOT_FOUND);
        }
        Asset asset = optionalAsset.get();
        asset.setDescription(dto.description());
        asset.setGivenDate(dto.givenDate());
        asset.setEmployeeId(dto.employeeId());
        asset.setType(dto.type());
        assetRepository.save(asset);
        return true;
    }

    public Boolean deleteAsset(String token, Long assetId) {
        employeeService.getEmployeeByToken(token);

        Optional<Asset> optionalAsset = assetRepository.findById(assetId);
        if(optionalAsset.isEmpty()){
            throw new OrganisationManagementException(ErrorType.ASSET_NOT_FOUND);
        }
        Asset asset = optionalAsset.get();
        asset.setState(EState.PASSIVE);
        assetRepository.save(asset);
        return true;
    }
}
