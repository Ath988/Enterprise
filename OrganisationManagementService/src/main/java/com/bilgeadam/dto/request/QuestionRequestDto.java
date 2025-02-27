package com.bilgeadam.dto.request;

import com.bilgeadam.entity.enums.ECategory;

import java.util.List;

public record QuestionRequestDto (

         String text,
         ECategory eCategory

){
}
