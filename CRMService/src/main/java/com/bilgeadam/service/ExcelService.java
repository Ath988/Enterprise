package com.bilgeadam.service;

import com.bilgeadam.entity.Customer;
import com.bilgeadam.entity.Profile;
import com.bilgeadam.exception.CRMServiceException;
import com.bilgeadam.exception.ErrorType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class ExcelService {
	
	private final CustomerService customerService;
	private static final String EXCEL_TYPE = "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet";
	
	/**
	 * Excel dosyasının geçerli olup olmadığını kontrol eder.
	 */
	public boolean hasExcelFormat(MultipartFile file) {
		return EXCEL_TYPE.equals(file.getContentType());
	}
	
	/**
	 * 📌 Excel dosyasını okuyarak müşteri listesine çevirir ve veritabanına kaydeder
	 */
	public void importCustomersFromExcel(MultipartFile file) {
		List<Customer> customers = new ArrayList<>();
		
		try (InputStream inputStream = file.getInputStream();
		     Workbook workbook = new XSSFWorkbook(inputStream)) {
			
			Sheet sheet = workbook.getSheetAt(0); // İlk sayfayı al
			Iterator<Row> rowIterator = sheet.iterator();
			
			// 📌 İlk satır başlık olduğu için atla
			if (rowIterator.hasNext()) {
				rowIterator.next();
			}
			
			while (rowIterator.hasNext()) {
				Row row = rowIterator.next();
				Customer customer = parseRowToCustomer(row);
				if (customer != null) {
					customers.add(customer);
				}
			}
			
			// 📌 Veritabanına kaydet
			customerService.importCustomers(customers);
			
		} catch (IOException e) {
			log.error("❌ Excel okuma hatası: {}", e.getMessage());
			throw new CRMServiceException(ErrorType.EXCEL_READ_ERROR);
		}
	}
	
	/**
	 * Excel satırını Customer nesnesine çevirir.
	 */
	private Customer parseRowToCustomer(Row row) {
		try {
			DataFormatter formatter = new DataFormatter();
			
			String firstName = formatter.formatCellValue(row.getCell(0)).trim();
			String lastName = formatter.formatCellValue(row.getCell(1)).trim();
			String email = formatter.formatCellValue(row.getCell(2)).trim();
			String phoneNumber = formatter.formatCellValue(row.getCell(3)).trim();
			String address = formatter.formatCellValue(row.getCell(4)).trim();
			String companyId = formatter.formatCellValue(row.getCell(5)).trim();
			
			if (firstName.isEmpty() || lastName.isEmpty() || email.isEmpty()) {
				log.warn("⚠️ Boş zorunlu alan! Satır atlandı: {}", row.getRowNum());
				return null;
			}
			
			return Customer.builder()
			               .companyId(Long.parseLong(companyId))  // Şirket ID'yi Long türüne çevir
			               .profile(new Profile(firstName, lastName, email, phoneNumber, address)) // Profile nesnesini oluştur
			               .build();
			
		} catch (Exception e) {
			log.error("❌ Satır okunamadı, hata: {} - Satır No: {}", e.getMessage(), row.getRowNum());
			return null;
		}
	}
}