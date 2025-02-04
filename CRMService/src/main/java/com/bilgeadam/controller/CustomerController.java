package com.bilgeadam.controller;

import com.bilgeadam.dto.request.AddCustomerRequestDto;
import com.bilgeadam.dto.request.UpdateCustomerRequestDto;
import com.bilgeadam.dto.response.BaseResponse;
import com.bilgeadam.entity.Customer;
import com.bilgeadam.service.CustomerService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

import static com.bilgeadam.constant.RestApis.*;

@RestController
@RequestMapping(CUSTOMER)
@RequiredArgsConstructor

public class CustomerController {
	private final CustomerService customerService;
	
	@PostMapping(ADDCUSTOMER)
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
	
	@GetMapping(GETALLCUSTOMERS)
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
	
	@GetMapping(GETCUSTOMERBYEMAIL)
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
	}
}