package com.bilgeadam.dto.response;

import org.apache.poi.ss.usermodel.Row;

import java.util.List;

public record RowDataWithError(
		Row row,
		List<String> errorMessages
) {
}