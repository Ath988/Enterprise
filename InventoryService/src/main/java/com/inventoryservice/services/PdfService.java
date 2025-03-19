package com.inventoryservice.services;

import com.inventoryservice.dto.request.PageRequestDTO;
import com.inventoryservice.dto.request.StockMovementUpdateRequestDTO;
import com.inventoryservice.entities.StockMovement;
import com.inventoryservice.exceptions.ErrorType;
import com.inventoryservice.exceptions.InventoryServiceException;
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
public class PdfService
{
	private final StockMovementService stockMovementService;
	private static final String FONT_PATH = "fonts/Roboto-Regular.ttf";
	
	/**
	 * 📌 Müşteri listesini içeren bir PDF oluşturur.
	 */
	public byte[] generateCustomerPdf() {
		List<StockMovement> stockMovements = stockMovementService.findAllByStatusAndAuthIdOrderByProduct_NameAsc();
		
		if (stockMovements.isEmpty()) {
			throw new InventoryServiceException(ErrorType.STOCK_MOVEMENT_NOT_FOUND);
		}
		
		try (PDDocument document = new PDDocument();
		     ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {

			PDPage page = new PDPage(new PDRectangle(PDRectangle.A4.getHeight(), PDRectangle.A4.getWidth()));
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
			String title = "Stok Hareketi Listesi";
			float titleWidth = font.getStringWidth(title) / 1000 * headerFontSize;
			float titleX = (pageWidth - titleWidth) / 2; // Ortaya hizala
			
			contentStream.setFont(font, headerFontSize);
			contentStream.beginText();
			contentStream.newLineAtOffset(titleX, yPosition);
			contentStream.showText(title);
			contentStream.endText();
			yPosition -= 20; // Başlık altı boşluk
			
			// 📌 Sütun Başlıkları
			String[] headers = {"Ürün Adı", "Açıklama", "Stok Adeti","Birim Fiyat", "Toplam Tutar", "Tür"};
			float[] colWidths = {tableWidth * 0.2f, tableWidth * 0.2f, tableWidth * 0.15f, tableWidth * 0.15f, tableWidth * 0.15f, tableWidth * 0.15f};
			
			drawTableRow(contentStream, font, headers, margin, yPosition, colWidths,true, rowHeight);
			yPosition -= rowHeight;

			for (StockMovement stockMovement : stockMovements) {
				String[] data = {
						stockMovement.getProduct().getName(),
						stockMovement.getDescription(),
						String.valueOf(stockMovement.getQuantity()),
						String.valueOf(stockMovement.getProduct().getPrice()),
						String.valueOf(stockMovement.getTotal()),
						stockMovement.getType().toString()
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
					PDPage newPage = new PDPage(new PDRectangle(PDRectangle.A4.getHeight(), PDRectangle.A4.getWidth()));
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
			throw new InventoryServiceException(ErrorType.STOCK_MOVEMENT_NOT_FOUND);
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