package com.edts.concerts.utils.helpers;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

@Component
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ResponseApi<T> {
    private int code;
    private String message;
    private String status;
    private T data;

    public static <T> ResponseApi<T> createResponse(HttpStatus httpStatus, String message, T data) {
        return new ResponseApi<>(httpStatus.value(), message, httpStatus.getReasonPhrase(), data);
    }

    public static <T> ResponseApi<T> OK(String message, T data) {
        return createResponse(HttpStatus.OK, message, data);
    }

    public static <T> ResponseApi<T> HttpStatus(HttpStatus httpStatus, String message, T data) {
        return createResponse(httpStatus, message, data);
    }

    public static <T> ResponseApi<T> CREATED(String message, T data) {
        return createResponse(HttpStatus.CREATED, message, data);
    }

    public static <T> ResponseApi<T> INTERNAL_SERVER_ERROR(String message, T data) {
        return createResponse(HttpStatus.INTERNAL_SERVER_ERROR, message, data);
    }

    public static <T> ResponseApi<T> BAD_REQUEST(String message, T data) {
        return createResponse(HttpStatus.BAD_REQUEST, message, data);
    }

    public static <T> ResponseApi<T> NOT_FOUND(String message, T data) {
        return createResponse(HttpStatus.NOT_FOUND, message, data);
    }
}
