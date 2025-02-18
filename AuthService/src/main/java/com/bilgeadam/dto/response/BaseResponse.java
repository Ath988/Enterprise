package com.bilgeadam.dto.response;

import com.bilgeadam.exception.EnterpriseException;
import com.bilgeadam.exception.ErrorType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.springframework.http.ResponseEntity;

@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@Data
public class BaseResponse<T> {
    Boolean success;
    String message;
    Integer code;
    T data;

    public static <T> T getDataFromResponse(ResponseEntity<BaseResponse<T>> response) {
        if (response.getBody() == null || response.getBody().getData() == null) {
            throw new EnterpriseException(ErrorType.INTERNAL_SERVER_ERROR);
        }
        return response.getBody().getData();
    }

    public static Boolean getSuccessFromResponse(ResponseEntity<BaseResponse<Boolean>> response) {
        if (response.getBody() == null || response.getBody().getSuccess() == null) {
            throw new EnterpriseException(ErrorType.INTERNAL_SERVER_ERROR);
        }
        return response.getBody().getSuccess();
    }
}
