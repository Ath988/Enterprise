package com.bilgeadam.service;

import com.bilgeadam.dto.request.AddCustomerRequestDto;
import com.bilgeadam.dto.request.UpdateCustomerRequestDto;
import com.bilgeadam.entity.Customer;
import com.bilgeadam.exception.CRMServiceException;
import com.bilgeadam.exception.ErrorType;
import com.bilgeadam.mapper.CustomerMapper;
import com.bilgeadam.repository.CustomerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CustomerService {
	private final CustomerRepository customerRepository;
	
	public void addCustomer(AddCustomerRequestDto dto){
		Customer customer = CustomerMapper.INSTANCE.fromAddCustomer(dto);
		customerRepository.save(customer);
	}
	
	public List<Customer> getAllCustomers(){
		
		return customerRepository.findAll();
	}
	
	public Customer getCustomerById(Long id){
		return customerRepository.findById(id).orElseThrow(()-> new CRMServiceException(ErrorType.CUSTOMER_NOT_FOUND));
	}
	
	public void updateCustomer(Long customerId, UpdateCustomerRequestDto dto) {
		Optional<Customer> optionalCustomer = customerRepository.findById(customerId);
		if (optionalCustomer.isPresent()) {
			Customer customer = optionalCustomer.get();
			CustomerMapper.INSTANCE.updateCustomerFromDto(dto, customer);
			customerRepository.save(customer);
		} else {
			throw new CRMServiceException(ErrorType.CUSTOMER_NOT_FOUND);
		}
	}
	
	public void deleteCustomer(Long customerId) {
		Customer customer = customerRepository.findById(customerId)
		                                      .orElseThrow(() -> new CRMServiceException(ErrorType.CUSTOMER_NOT_FOUND));
		customerRepository.delete(customer);
	}
	
	/*
	
	public Customer getCustomerByEmail(String email) {
		return customerRepository.findByEmail(email)
		                         .orElseThrow(() -> new CRMServiceException(ErrorType.CUSTOMER_NOT_FOUND));
	}
	
	public Customer getCustomerByPhone(String phone){
		return customerRepository.findByPhone(phone)
				.orElseThrow(()-> new CRMServiceException(ErrorType.CUSTOMER_NOT_FOUND));
	}
	
	public void updateCustomerByEmail(UpdateCustomerRequestDto dto){
		Optional<Customer> optionalCustomer = customerRepository.findByEmail(dto.email());
		if (optionalCustomer.isPresent()){
			Customer customer = optionalCustomer.get();
			CustomerMapper.INSTANCE.updateCustomerFromDto(dto, customer);
			customerRepository.save(customer);
		}else{
			throw new CRMServiceException(ErrorType.CUSTOMER_NOT_FOUND);
		}
	}
	
	public void updateCustomerByPhone(UpdateCustomerRequestDto dto){
		Optional<Customer> optionalCustomer = customerRepository.findByPhone(dto.phone());
		if (optionalCustomer.isPresent()){
			Customer customer = optionalCustomer.get();
			CustomerMapper.INSTANCE.updateCustomerFromDto(dto, customer);
			customerRepository.save(customer);
		}else {
			throw new CRMServiceException(ErrorType.CUSTOMER_NOT_FOUND);
		}
	}
	
	public void deleteCustomerByEmail(String email) {
		Optional<Customer> optionalCustomer = customerRepository.findByEmail(email);
		if (optionalCustomer.isPresent()) {
			customerRepository.delete(optionalCustomer.get());
		} else {
			throw new CRMServiceException(ErrorType.CUSTOMER_NOT_FOUND);
		}
	}
	
	public void deleteCustomerByPhone(String phone) {
		Optional<Customer> optionalCustomer = customerRepository.findByPhone(phone);
		if (optionalCustomer.isPresent()) {
			customerRepository.delete(optionalCustomer.get());
		} else {
			throw new CRMServiceException(ErrorType.CUSTOMER_NOT_FOUND);
		}
	}
	
	public List<Customer> getCustomersByCompanyName(String companyName) {
		return customerRepository.findByCompanyName(companyName);
	}
	
	public List<Customer> getCustomersByFirstName(String firstName) {
		return customerRepository.findByFirstNameIgnoreCase(firstName);
	}
	
	public Optional<Customer> getCustomerByFullName(String firstName, String lastName) {
		return customerRepository.findByFirstNameAndLastName(firstName, lastName);
	}
	
	public List<Customer> getCustomersByPhonePrefix(String areaCode) {
		return customerRepository.findByPhoneStartingWith(areaCode);
	}
	
	public List<Customer> getLatestCustomers() {
		return customerRepository.findTop10ByOrderByIdDesc();
	}*/
	
	
}