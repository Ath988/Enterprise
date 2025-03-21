package com.bilgeadam.controller;

import com.bilgeadam.dto.request.UpdateServiceDto;
import com.bilgeadam.dto.response.BaseResponse;
import com.bilgeadam.entity.ServiceEntity;
import com.bilgeadam.service.ServiceEntityService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.bilgeadam.constant.RestApis.*;

@RestController
@RequiredArgsConstructor
@RequestMapping(SERVICE)
@CrossOrigin("*")
public class ServiceEntityController {
	private final ServiceEntityService serviceEntityService;
	
	@GetMapping(GET_ALL_SERVICES)
	public ResponseEntity<BaseResponse<List<ServiceEntity>>> getAllServices(){
		return ResponseEntity.ok(BaseResponse.<List<ServiceEntity>>builder()
				                         .data(serviceEntityService.getAllServices())
				                         .success(true)
				                         .code(200)
				                         .message("hizmetler başarıyla getirildi")
		                                     .build());
	}
	
	@PreAuthorize("hasRole('SYSTEM_ADMIN')")
    @PutMapping(UPDATE_SERVICE)
	public ResponseEntity<BaseResponse<ServiceEntity>> updateService(@RequestBody UpdateServiceDto dto){
		ServiceEntity updated = serviceEntityService.updateServicePlan(dto);
		return ResponseEntity.ok(BaseResponse.<ServiceEntity>builder()
				                         .data(updated)
				                         .message("başarıyla guncellendi")
				                         .code(200)
				                         .success(true)
		                                     .build());
    }
}