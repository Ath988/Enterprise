package com.bilgeadam.service;

import com.bilgeadam.entity.Customer;
import com.bilgeadam.exception.CRMServiceException;
import com.bilgeadam.exception.ErrorType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.font.PDType0Font;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class PdfService {
	private final CustomerService customerService;
	private static final String FONT_PATH = "fonts/Roboto-Regular.ttf";
	
	/**
	 * 📌 Müşteri listesini içeren bir PDF oluşturur.
	 */
	public byte[] generateCustomerPdf() {
		List<Customer> customers = customerService.getAllCustomers();
		
		if (customers.isEmpty()) {
			throw new CRMServiceException(ErrorType.CUSTOMER_NOT_FOUND);
		}
		
		try (PDDocument document = new PDDocument();
		     ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
			
			PDPage page = new PDPage(PDRectangle.A4);
			document.addPage(page);
			
			PDPageContentStream contentStream = new PDPageContentStream(document, page);
			
			ClassPathResource fontResource = new ClassPathResource(FONT_PATH);
			InputStream fontStream = fontResource.getInputStream();
			PDType0Font font = PDType0Font.load(document, fontStream);
			
			float margin = 50;
			float pageWidth = page.getMediaBox().getWidth();
			float tableWidth = pageWidth - 2 * margin;
			float yStart = page.getMediaBox().getHeight() - margin;
			float yPosition = yStart;
			float rowHeight = 30;
			float tableMargin = 10;
			float headerFontSize = 16;
			
			// 📌 Başlık
			String title = "Müşteri Listesi";
			float titleWidth = font.getStringWidth(title) / 1000 * headerFontSize;
			float titleX = (pageWidth - titleWidth) / 2; // Ortaya hizala
			
			contentStream.setFont(font, headerFontSize);
			contentStream.beginText();
			contentStream.newLineAtOffset(titleX, yPosition);
			contentStream.showText(title);
			contentStream.endText();
			yPosition -= 20; // Başlık altı boşluk
			
			// 📌 Sütun Başlıkları
			String[] headers = {"Ad Soyad", "E-posta", "Telefon", "Adres"};
			float[] colWidths = {tableWidth * 0.25f, tableWidth * 0.3f, tableWidth * 0.2f, tableWidth * 0.25f};
			
			drawTableRow(contentStream, font, headers, margin, yPosition, colWidths,true, rowHeight);
			yPosition -= rowHeight;
			
			// 📌 Müşteri Verilerini Tabloya Yaz
			for (Customer customer : customers) {
				String[] data = {
						customer.getProfile().getFirstName() + " " + customer.getProfile().getLastName(),
						customer.getProfile().getEmail(),
						customer.getProfile().getPhoneNumber(),
						customer.getProfile().getAddress()
				};
				drawTableRow(contentStream, font, data, margin, yPosition, colWidths, false, rowHeight);
				yPosition -= rowHeight;
				
				// 📌 Satırın Altına Çizgi Çek
				contentStream.setLineWidth(0.5f);
				contentStream.moveTo(margin, yPosition + rowHeight - 2);
				contentStream.lineTo(margin + tableWidth, yPosition + rowHeight - 5);
				contentStream.stroke();
				
				if (yPosition < margin + tableMargin) {
					contentStream.close();
					PDPage newPage = new PDPage(PDRectangle.A4);
					document.addPage(newPage);
					contentStream = new PDPageContentStream(document, newPage);
					yPosition = yStart;
				}
			}
			
			contentStream.close();
			document.save(outputStream);
			return outputStream.toByteArray();
			
		} catch (IOException e) {
			log.error("❌ PDF oluşturulurken hata meydana geldi: {}", e.getMessage());
			throw new CRMServiceException(ErrorType.PDF_GENERATION_FAILED);
		}
	}
	
	private void drawTableRow(PDPageContentStream contentStream, PDType0Font font, String[] data, float x, float y,
	                          float[] colWidths, boolean isHeader, float rowHeight) throws IOException {
		contentStream.setFont(font, isHeader ? 14 : 12);
		float cellX = x;
		for (int i = 0; i < data.length; i++) {
			contentStream.beginText();
			contentStream.newLineAtOffset(cellX + 5, y - (rowHeight / 2 + 5));
			contentStream.showText(data[i]);
			contentStream.endText();
			cellX += colWidths[i];
		}
	}
	
}