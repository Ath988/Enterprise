package com.bilgeadam.repository;

import com.bilgeadam.entity.Customer;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CustomerRepository extends JpaRepository<Customer, Long> {
	Optional<Customer> findByProfileEmail(String email);
	Optional<Customer> findByProfilePhoneNumber(String phoneNumber);
	/*Optional<Customer> findByEmail(String email);
	Optional<Customer> findByPhone(String phone);
	
	// 1. Belirli bir şirket adına göre müşteri listesi getiren sorgu
	List<Customer> findByCompanyName(String companyName);
	
	// 2. İsme göre müşteri arayan sorgu (büyük/küçük harf duyarlılığı olmadan)
	List<Customer> findByFirstNameIgnoreCase(String firstName);
	
	// 3. Hem isim hem soyisimle müşteri arayan sorgu
	Optional<Customer> findByFirstNameAndLastName(String firstName, String lastName);
	
	// 4. Belirli bir alan koduyla başlayan telefon numaralarına sahip müşterileri getiren sorgu
	List<Customer> findByPhoneStartingWith(String areaCode);
	
	// 5. En son eklenen müşterileri sıralı getiren sorgu
	List<Customer> findTop10ByOrderByIdDesc();*/
}