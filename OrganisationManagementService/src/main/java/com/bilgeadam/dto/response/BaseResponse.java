package com.bilgeadam.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class BaseResponse<T> {
    @Builder.Default
    Boolean success = true;
    String message;
    @Builder.Default
    Integer code = 200;
    T data;
}