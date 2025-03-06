package com.bilgeadam.dto.response;

public record ImportResultWithFile(
		int totalRows,
		int importedRows,
		byte[] errorFile // Hatalı satırları içeren Excel dosyası
) {
}