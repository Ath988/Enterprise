package com.bilgeadam.service;

import com.bilgeadam.dto.response.ImportResultWithFile;
import com.bilgeadam.dto.response.ParseResult;
import com.bilgeadam.dto.response.RowDataWithError;
import com.bilgeadam.entity.Customer;
import com.bilgeadam.entity.Profile;
import com.bilgeadam.exception.CRMServiceException;
import com.bilgeadam.exception.ErrorType;
import com.bilgeadam.repository.CustomerRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayOutputStream;
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
	private static final List<String> REQUIRED_HEADERS = List.of("Ad", "Soyad", "EPosta", "Telefon", "Adres", "Şirket Id");
	
	/**
	 * Excel dosyasının geçerli olup olmadığını kontrol eder.
	 */
	public boolean hasExcelFormat(MultipartFile file) {
		return EXCEL_TYPE.equals(file.getContentType());
	}
	
	/**
	 * 📌 Excel dosyasını okuyarak müşteri listesine çevirir ve veritabanına kaydeder
	 */
	public ImportResultWithFile importCustomersFromExcel(MultipartFile file) {
		List<Customer> validCustomers = new ArrayList<>();
		List<RowDataWithError> errorRows = new ArrayList<>();
		
		try (InputStream inputStream = file.getInputStream();
		     Workbook workbook = new XSSFWorkbook(inputStream)) {
			
			Sheet sheet = workbook.getSheetAt(0); // İlk sayfayı al
			Iterator<Row> rowIterator = sheet.iterator();
			
			// 📌 İlk satır başlık olduğu için atla
			if (rowIterator.hasNext()) {
				Row headerRow = rowIterator.next();
				if (!isHeaderValid(headerRow)) {
					log.warn("Excel başlıkları uygun formatta değil. Kullanıcıya şablon excel döndürülecek.");
					byte[] templateFile = generateTemplateExcel();
					return new ImportResultWithFile(0, 0, templateFile);
				}
			}
			
			int rowNumber = 1;
			while (rowIterator.hasNext()) {
				Row row = rowIterator.next();
				ParseResult result = parseRowToCustomer(row);
				
				if (result.customer() != null) {
					validCustomers.add(result.customer());
				} else {
					List<String> errors = new ArrayList<>();
					errors.add(result.errorMessage());
					errorRows.add(new RowDataWithError(row, errors));
				}
				rowNumber++;
			}
			
			// ❗️ Hatalı satırlar varsa ve hiç başarılı satır yoksa, direkt hata dosyasını döndür.
			if (validCustomers.isEmpty() && !errorRows.isEmpty()) {
				log.warn("Tüm satırlar hatalı, hata dosyası oluşturuluyor...");
				byte[] errorFile = generateErrorExcel(errorRows);
				return new ImportResultWithFile(rowNumber - 1, 0, errorFile);
			}
			
			// 📌 Veritabanına kaydet
			customerService.importCustomers(validCustomers);
			
			byte[] errorFile = generateErrorExcel(errorRows);
			
			return new ImportResultWithFile(rowNumber - 1, validCustomers.size(), errorFile);
			
		} catch (IOException e) {
			log.error("❌ Excel okuma hatası: {}", e.getMessage());
			throw new CRMServiceException(ErrorType.EXCEL_READ_ERROR);
		}
	}
	
	/**
	 * Excel satırını Customer nesnesine çevirir.
	 */
	private ParseResult parseRowToCustomer(Row row) {
		DataFormatter formatter = new DataFormatter();
		
		int columnCount = Math.min(row.getPhysicalNumberOfCells(), 6);
		
		String firstName = columnCount > 0 ? formatter.formatCellValue(row.getCell(0)).trim() : "";
		String lastName = columnCount > 1 ? formatter.formatCellValue(row.getCell(1)).trim() : "";
		String email = columnCount > 2 ? formatter.formatCellValue(row.getCell(2)).trim() : "";
		String phoneNumber = columnCount > 3 ? formatter.formatCellValue(row.getCell(3)).trim() : "";
		String address = columnCount > 4 ? formatter.formatCellValue(row.getCell(4)).trim() : "";
		String companyIdStr = columnCount > 5 ? formatter.formatCellValue(row.getCell(5)).trim() : "";
		
		List<String> errors = new ArrayList<>();
		
		// Zorunlu alan kontrolü
		if (firstName.isEmpty() || lastName.isEmpty() || email.isEmpty() || phoneNumber.isEmpty() || address.isEmpty() || companyIdStr.isEmpty()) {
			errors.add("Zorunlu alanlardan biri veya birkaçı eksik.");
		}
		
		// Ad ve Soyad harf kontrolü
		if (!firstName.matches("^[a-zA-ZçÇğĞıİöÖşŞüÜ\\s]+$")) {
			errors.add("Ad sadece harf ve boşluk içerebilir.");
		}
		if (!lastName.matches("^[a-zA-ZçÇğĞıİöÖşŞüÜ\\s]+$")) {
			errors.add("Soyad sadece harf ve boşluk içerebilir.");
		}
		
		// E-posta format kontrolü
		if (!email.contains("@") || !email.matches("^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$")) {
			errors.add("Geçersiz e-posta formatı. Örneğin: ornek@domain.com");
		} else if (customerService.isEmailExists(email)) {
			errors.add("Bu e-posta adresi sistemde zaten kayıtlı.");
		}
		
		// Telefon numarası kontrolü
		phoneNumber = phoneNumber.replaceAll("[^0-9]", ""); // Sadece rakamları al
		
		if (phoneNumber.startsWith("0") && phoneNumber.length() == 11) {
			phoneNumber = phoneNumber.substring(1); // Başındaki 0'ı kaldır
		}
		
		if (phoneNumber.length() != 10) {
			errors.add("Telefon numarası 10 haneli olmalıdır. (Örnek: 5556669988)");
		} else if (customerService.isPhoneNumberExists(phoneNumber)) {
			errors.add("Bu telefon numarası sistemde zaten kayıtlı.");
		}
		
		// Adres format kontrolü (İlçe/İl formatı zorunlu)
		if (!address.matches("^[a-zA-ZçÇğĞıİöÖşŞüÜ\\s]+/[a-zA-ZçÇğĞıİöÖşŞüÜ\\s]+$")) {
			errors.add("Adres 'İlçe/İl' formatında olmalıdır. Örnek: Beşiktaş/İstanbul.");
		}
		
		// Hatalar varsa müşteri oluşturma ve hata mesajlarını dön
		if (!errors.isEmpty()) {
			return new ParseResult(null, String.join(", ", errors));
		}
		
		try {
			Long companyId = Long.parseLong(companyIdStr);
			Customer customer = Customer.builder()
			                            .companyId(companyId)
			                            .profile(new Profile(firstName, lastName, email, phoneNumber, address))
			                            .build();
			return new ParseResult(customer, null); // Hata yoksa errorMessage null olur
		} catch (NumberFormatException e) {
			return new ParseResult(null, "Geçersiz şirket ID. Sayısal bir değer olmalıdır.");
		}
	}
	
	/**
	 * 📌 Müşteri bilgilerini Excel formatında dışa aktarır (indirme işlemi).
	 */
	
	public byte[] exportCustomersToExcel(){
		List<Customer> customers = customerService.getAllCustomers();
		
		if (customers.isEmpty()) {
			throw new CRMServiceException(ErrorType.CUSTOMER_NOT_FOUND);
		}
		
		try (Workbook workbook = new XSSFWorkbook()){
			Sheet sheet = workbook.createSheet("Müşteriler");
			
			// ✅ Başlık satırını oluştur
			Row headerRow = sheet.createRow(0);
			
			for (int i = 0; i < REQUIRED_HEADERS.size(); i++) {
				Cell cell = headerRow.createCell(i);
				cell.setCellValue(REQUIRED_HEADERS.get(i));
				cell.setCellStyle(createHeaderStyle(workbook));
			}
			
			// ✅ Müşteri verilerini yaz
			int rowNum = 1;
			for (Customer customer : customers) {
				Row row = sheet.createRow(rowNum++);
				row.createCell(0).setCellValue(customer.getProfile().getFirstName());
				row.createCell(1).setCellValue(customer.getProfile().getLastName());
				row.createCell(2).setCellValue(customer.getProfile().getEmail());
				row.createCell(3).setCellValue(customer.getProfile().getPhoneNumber());
				row.createCell(4).setCellValue(customer.getProfile().getAddress());
				row.createCell(5).setCellValue(customer.getCompanyId());
			}
			
			// ✅ Otomatik sütun genişliği ayarla
			for (int i = 0; i < REQUIRED_HEADERS.size(); i++) {
				sheet.autoSizeColumn(i);
			}
			
			// ✅ Excel dosyasını byte dizisine çevir
			ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
			workbook.write(outputStream);
			
			return outputStream.toByteArray();
			
		}
		catch (IOException e) {
			log.error("❌ Excel dosyası oluşturulurken hata oluştu: {}", e.getMessage());
			throw new CRMServiceException(ErrorType.EXCEL_READ_ERROR);
		}
	}
	
	/**
	 * 📌 Boş bir müşteri şablonu (template) üretir.
	 */
	private byte[] exportTemplateExcel() {
		try (Workbook workbook = new XSSFWorkbook()) {
			Sheet sheet = workbook.createSheet("Template");
			Row headerRow = sheet.createRow(0);
			
			for (int i = 0; i < REQUIRED_HEADERS.size(); i++) {
				Cell cell = headerRow.createCell(i);
				cell.setCellValue(REQUIRED_HEADERS.get(i));
				cell.setCellStyle(createHeaderStyle(workbook));
			}
			
			ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
			workbook.write(outputStream);
			return outputStream.toByteArray();
		} catch (IOException e) {
			throw new CRMServiceException(ErrorType.EXCEL_READ_ERROR);
		}
	}
	
	/**
	 * 📌 Başlık hücresi için stil oluşturur.
	 */
	private CellStyle createHeaderStyle(Workbook workbook) {
		CellStyle headerStyle = workbook.createCellStyle();
		Font font = workbook.createFont();
		font.setBold(true);
		headerStyle.setFont(font);
		return headerStyle;
	}
	
	private byte[] generateErrorExcel(List<RowDataWithError> errorRows) throws IOException {
		if (errorRows.isEmpty()) return new byte[0];
		
		try (Workbook workbook = new XSSFWorkbook()) {
			Sheet sheet = workbook.createSheet("Hatalı Satırlar");
			
			// Başlıkları ekle
			Row headerRow = sheet.createRow(0);
			for (int i = 0; i < REQUIRED_HEADERS.size(); i++) {
				headerRow.createCell(i).setCellValue(REQUIRED_HEADERS.get(i));
			}
			headerRow.createCell(REQUIRED_HEADERS.size()).setCellValue("Hata Mesajı");
			
			// Hatalı satırları ekle
			int rowNum = 1;
			DataFormatter formatter = new DataFormatter();
			
			for (RowDataWithError rowData : errorRows) {
				Row errorRow = sheet.createRow(rowNum++);
				for (int i = 0; i < REQUIRED_HEADERS.size(); i++) {
					errorRow.createCell(i).setCellValue(formatter.formatCellValue(rowData.row().getCell(i)));
				}
				String combinedErrors = String.join(", ", rowData.errorMessages());
				errorRow.createCell(REQUIRED_HEADERS.size()).setCellValue(combinedErrors);
			}
			
			ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
			workbook.write(outputStream);
			return outputStream.toByteArray();
		}
	}
	
	
	private void createHeaderRow(Sheet sheet) {
		Row headerRow = sheet.createRow(0);
		String[] headers = {"Ad", "Soyad", "EPosta", "Telefon", "Adres", "Şirket Id", "Hata Mesajı"};
		
		for (int i = 0; i < headers.length; i++) {
			headerRow.createCell(i).setCellValue(headers[i]);
		}
	}
	
	private void copyRow(Row sourceRow, Row targetRow, DataFormatter formatter) {
		for (int i = 0; i < 6; i++) { // İlk 6 hücreyi (Ad, Soyad, Email, Telefon, Adres, ŞirketID) kopyala
			String cellValue = formatter.formatCellValue(sourceRow.getCell(i));
			targetRow.createCell(i).setCellValue(cellValue);
		}
	}
	
	private boolean isHeaderValid(Row headerRow) {
		if (headerRow == null) {
			return false;
		}
		
		int actualSize = headerRow.getPhysicalNumberOfCells();
		int requiredSize = REQUIRED_HEADERS.size();
		
		// Eğer Hata Mesajı sütunu varsa, başlık sayısı bir fazla olabilir
		if (actualSize < requiredSize || actualSize > requiredSize + 1) {
			return false;
		}
		
		// Sadece ilk 6 başlığı kontrol et
		for (int i = 0; i < requiredSize; i++) {
			String cellValue = headerRow.getCell(i, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK).getStringCellValue().trim();
			if (!REQUIRED_HEADERS.get(i).equalsIgnoreCase(cellValue)) {
				return false;
			}
		}
		return true;
	}
	
	
	private byte[] generateTemplateExcel() {
		return exportTemplateExcel();  // Sadece başlıklarla boş excel üretir.
	}
	
	
}