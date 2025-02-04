package com.bilgeadam.exception;

import com.fasterxml.jackson.databind.JsonNode;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;


@Builder
@Getter
@Setter
public class ErrorMessage {
    int code;
    String message;
    Boolean success;
    List<String> fields;

}
