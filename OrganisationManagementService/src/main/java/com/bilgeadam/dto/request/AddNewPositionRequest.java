package com.bilgeadam.dto.request;

public record AddNewPositionRequest(

        Long departmentId,
        String title,
        String description
) {
}
