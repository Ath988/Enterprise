package com.bilgeadam.dto.response;

import com.bilgeadam.exception.ErrorType;
import com.bilgeadam.exception.HRException;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.ResponseEntity;

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

    public static <T> T getDataFromResponse(ResponseEntity<BaseResponse<T>> response) {
        if (response.getBody() == null || response.getBody().getData() == null) {
            throw new HRException(ErrorType.INTERNAL_SERVER_ERROR);
        }
        return response.getBody().getData();
    }

    public static Boolean getSuccessFromResponse(ResponseEntity<BaseResponse<Boolean>> response) {
        if (response.getBody() == null || response.getBody().getSuccess() == null) {
            throw new HRException(ErrorType.INTERNAL_SERVER_ERROR);
        }
        return response.getBody().getSuccess();
    }
}