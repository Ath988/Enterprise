package com.bilgeadam.controller;

import com.bilgeadam.dto.request.AddCustomerRequestDto;
import com.bilgeadam.dto.request.UpdateCustomerRequestDto;
import com.bilgeadam.dto.response.BaseResponse;
import com.bilgeadam.entity.Customer;
import com.bilgeadam.service.CustomerService;
import com.bilgeadam.service.ExcelService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;

import static com.bilgeadam.constant.RestApis.*;

@RestController
@RequestMapping(CUSTOMER)
@RequiredArgsConstructor

public class CustomerController {
	private final CustomerService customerService;
	private final ExcelService excelService;
	
	@PostMapping(ADD_CUSTOMER)
	public ResponseEntity<BaseResponse<Boolean>> addCustomer(@RequestBody @Valid AddCustomerRequestDto dto){
		customerService.addCustomer(dto);
		return ResponseEntity.ok(BaseResponse.<Boolean>builder()
		                                     .code(200)
		                                     .success(true)
		                                     .data(true)
		                                     .message("Yeni müşteri eklendi.")
		                                     .build()
		);
	}
	
	@GetMapping(GET_ALL_CUSTOMERS)
	public ResponseEntity<BaseResponse<List<Customer>>> getAllCustomers() {
		List<Customer> customers = customerService.getAllCustomers();
		return ResponseEntity.ok(BaseResponse.<List<Customer>>builder()
		                                     .code(200)
		                                     .success(true)
		                                     .data(customers)
		                                     .message("Tüm müşteriler listelendi.")
		                                     .build()
		);
	}
	
	@GetMapping(GET_CUSTOMER_ID + "/{customerId}")
	public ResponseEntity<BaseResponse<Customer>> getCustomerById(@PathVariable Long customerId) {
		Customer customer = customerService.getCustomerById(customerId);
		return ResponseEntity.ok(BaseResponse.<Customer>builder()
		                                     .code(200)
		                                     .success(true)
		                                     .data(customer)
		                                     .message("Müsşteri bilgisi getirildi")
		                                     .build()
		);
	}
	
	@PutMapping(UPDATE_CUSTOMER + "/{customerId}")
	public ResponseEntity<BaseResponse<Boolean>> updateCustomer(@PathVariable Long customerId, @RequestBody @Valid UpdateCustomerRequestDto dto) {
		customerService.updateCustomer(customerId, dto);
		return ResponseEntity.ok(BaseResponse.<Boolean>builder()
		                                     .code(200)
		                                     .success(true)
		                                     .data(true)
		                                     .message("Müşteri bilgileri güncellendi.")
		                                     .build()
		);
	}
	
	@DeleteMapping(DELETE_CUSTOMER + "/{customerId}")
	public ResponseEntity<BaseResponse<Boolean>> deleteCustomer(@PathVariable Long customerId) {
		customerService.deleteCustomer(customerId);
		return ResponseEntity.ok(BaseResponse.<Boolean>builder()
		                                     .code(200)
		                                     .success(true)
		                                     .data(true)
		                                     .message("Müsteri başarıyla silindi")
		                                     .build()
		);
	}
	
	@DeleteMapping(DELETE_CUSTOMERS)
	public ResponseEntity<BaseResponse<Boolean>> deleteCustomers(@RequestBody List<Long> customerIds) {
		customerService.deleteCustomers(customerIds);
		return ResponseEntity.ok(BaseResponse.<Boolean>builder()
		                                     .code(200)
		                                     .success(true)
		                                     .data(true)
		                                     .message("Seçili müşteriler başarıyla silindi.")
		                                     .build());
	}
	
	@PostMapping(value = IMPORT_EXCEL, consumes = "multipart/form-data")
	public ResponseEntity<BaseResponse<Boolean>> importExcel(
			@RequestParam("file") MultipartFile file) {
		excelService.importCustomersFromExcel(file);
		return ResponseEntity.ok(BaseResponse.<Boolean>builder()
		                                     .code(200)
		                                     .success(true)
		                                     .data(true)
		                                     .message("Excel'den müşteriler başarıyla eklendi.")
		                                     .build()
		);
	}
	
	/*@GetMapping(GETCUSTOMERBYEMAIL)
	public ResponseEntity<BaseResponse<Customer>> getCustomerByEmail(@RequestParam String email) {
		Customer customer = customerService.getCustomerByEmail(email);
		return ResponseEntity.ok(BaseResponse.<Customer>builder()
		                                     .code(200)
		                                     .success(true)
		                                     .data(customer)
		                                     .message("Müşteri bilgisi getirildi.")
		                                     .build()
		);
	}
	
	@GetMapping(GETCUSTOMERBYPHONE)
	public ResponseEntity<BaseResponse<Customer>> getCustomerByPhone(@RequestParam String phone) {
		Customer customer = customerService.getCustomerByPhone(phone);
		return ResponseEntity.ok(BaseResponse.<Customer>builder()
		                                     .code(200)
		                                     .success(true)
		                                     .data(customer)
		                                     .message("Müşteri bilgisi getirildi.")
		                                     .build()
		);
	}
	
	@GetMapping(GETCUSTOMERSBYCOMPANY)
	public ResponseEntity<BaseResponse<List<Customer>>> getCustomersByCompany(@RequestParam String companyName) {
		return ResponseEntity.ok(BaseResponse.<List<Customer>>builder()
		                                     .code(200)
		                                     .success(true)
		                                     .data(customerService.getCustomersByCompanyName(companyName))
		                                     .message("Şirkete ait müşteriler listelendi.")
		                                     .build()
		);
	}
	
	@GetMapping(GETCUSTOMERSBYFIRSTNAME)
	public ResponseEntity<BaseResponse<List<Customer>>> getCustomersByFirstName(@RequestParam String firstName) {
		return ResponseEntity.ok(BaseResponse.<List<Customer>>builder()
		                                     .code(200)
		                                     .success(true)
		                                     .data(customerService.getCustomersByFirstName(firstName))
		                                     .message("İsme göre müşteriler listelendi.")
		                                     .build()
		);
	}
	
	@GetMapping(GETCUSTOMERBYFULLNAME)
	public ResponseEntity<BaseResponse<Optional<Customer>>> getCustomerByFullName(@RequestParam String firstName, @RequestParam String lastName) {
		return ResponseEntity.ok(BaseResponse.<Optional<Customer>>builder()
		                                     .code(200)
		                                     .success(true)
		                                     .data(customerService.getCustomerByFullName(firstName, lastName))
		                                     .message("Ad ve soyada göre müşteri getirildi.")
		                                     .build()
		);
	}
	
	@GetMapping(GETCUSTOMERSBYPHONEPREFIX)
	public ResponseEntity<BaseResponse<List<Customer>>> getCustomersByPhonePrefix(@RequestParam String areaCode) {
		return ResponseEntity.ok(BaseResponse.<List<Customer>>builder()
		                                     .code(200)
		                                     .success(true)
		                                     .data(customerService.getCustomersByPhonePrefix(areaCode))
		                                     .message("Belirtilen telefon numarası ön ekine göre müşteriler listelendi.")
		                                     .build()
		);
	}
	
	@GetMapping(GETLATESTCUSTOMERS)
	public ResponseEntity<BaseResponse<List<Customer>>> getLatestCustomers() {
		return ResponseEntity.ok(BaseResponse.<List<Customer>>builder()
		                                     .code(200)
		                                     .success(true)
		                                     .data(customerService.getLatestCustomers())
		                                     .message("Son eklenen müşteriler listelendi.")
		                                     .build()
		);
	}
	
	@PutMapping(UPDATECUSTOMERBYEMAIL)
	public ResponseEntity<BaseResponse<Boolean>> updateCustomerByEmail(@RequestBody @Valid UpdateCustomerRequestDto dto) {
		customerService.updateCustomerByEmail(dto);
		return ResponseEntity.ok(BaseResponse.<Boolean>builder()
		                                     .code(200)
		                                     .success(true)
		                                     .data(true)
		                                     .message("Müşteri bilgileri güncellendi.")
		                                     .build()
		);
	}
	
	@PutMapping(UPDATECUSTOMERBYPHONE)
	public ResponseEntity<BaseResponse<Boolean>> updateCustomerByPhone(@RequestBody @Valid UpdateCustomerRequestDto dto) {
		customerService.updateCustomerByPhone(dto);
		return ResponseEntity.ok(BaseResponse.<Boolean>builder()
		                                     .code(200)
		                                     .success(true)
		                                     .data(true)
		                                     .message("Müşteri bilgileri güncellendi.")
		                                     .build()
		);
	}
	
	@DeleteMapping(DELETECUSTOMERBYEMAIL)
	public ResponseEntity<BaseResponse<Boolean>> deleteCustomerByEmail(@RequestParam String email) {
		customerService.deleteCustomerByEmail(email);
		return ResponseEntity.ok(BaseResponse.<Boolean>builder()
		                                     .code(200)
		                                     .success(true)
		                                     .data(true)
		                                     .message("Müşteri başarıyla silindi.")
		                                     .build()
		);
	}
	
	@DeleteMapping(DELETECUSTOMERBYPHONE)
	public ResponseEntity<BaseResponse<Boolean>> deleteCustomerByPhone(@RequestParam String phone) {
		customerService.deleteCustomerByPhone(phone);
		return ResponseEntity.ok(BaseResponse.<Boolean>builder()
		                                     .code(200)
		                                     .success(true)
		                                     .data(true)
		                                     .message("Müşteri başarıyla silindi.")
		                                     .build()
		);
	}*/
}