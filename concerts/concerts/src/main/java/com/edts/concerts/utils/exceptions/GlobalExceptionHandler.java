package com.edts.concerts.utils.exceptions;

import com.edts.concerts.utils.helpers.ResponseApi;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.server.MethodNotAllowedException;
import org.springframework.web.servlet.NoHandlerFoundException;

@ControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    private HttpStatus determineHttpStatus(Exception ex) {
        if (ex instanceof CustomsException && ex.getMessage().contains("found")) {
            return HttpStatus.NOT_FOUND;
        }
        return HttpStatus.INTERNAL_SERVER_ERROR;
    }

    @ExceptionHandler(CustomsException.class)
    public ResponseEntity<ResponseApi<Void>> handleCustomsException(CustomsException ex) {
        HttpStatus httpStatus = determineHttpStatus(ex);
        ResponseApi<Void> errorResponse = ResponseApi.HttpStatus(httpStatus,ex.getMessage(), null);;
        return ResponseEntity.status(httpStatus).body(errorResponse);
    }

    @ExceptionHandler(MappingErrorException.class)
    public ResponseEntity<ResponseApi<Void>> handleMappingErrorException(MappingErrorException ex) {
        log.error("Mapping error occurred", ex);

        ResponseApi<Void> errorResponse = ResponseApi.INTERNAL_SERVER_ERROR("an error occurred", null);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ResponseApi<Void>> handleValidationException(MethodArgumentTypeMismatchException ex) {
        if (ex.getName().equals("id") && ex.getValue() == null) {
            String errorMessage = "Missing required parameter: id";
            log.error("Validation error: {}", errorMessage, ex);

            ResponseApi<Void> errorResponse = ResponseApi.BAD_REQUEST(errorMessage, null);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
        }

        String errorMessage = (ex.getCause() instanceof IllegalArgumentException)
                ? "Invalid parameter format: " + ex.getMessage()
                : "Invalid parameter value: " + ex.getMessage();

        ResponseApi<Void> errorResponse = ResponseApi.BAD_REQUEST(errorMessage, null);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }

    @ExceptionHandler(NoHandlerFoundException.class)
    public ResponseEntity<ResponseApi<Void>> handleNoHandlerFoundException(NoHandlerFoundException ex) {
        String errorMessage = "Endpoint not found: " + ex.getRequestURL();
        log.error("Endpoint not found: {}", ex.getRequestURL(), ex);

        ResponseApi<Void> errorResponse = ResponseApi.NOT_FOUND(errorMessage, null);
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
    }

    @ExceptionHandler(DataAccessException.class)
    public ResponseEntity<ResponseApi<Void>> handleDataAccessException(DataAccessException ex) {
        log.error("Database error", ex);

        ResponseApi<Void> errorResponse = ResponseApi.INTERNAL_SERVER_ERROR("database error", null);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
    }

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<ResponseApi<Void>> handleMethodNotAllowed(HttpRequestMethodNotSupportedException ex) {
        log.error("Method Not Allowed", ex);

        ResponseApi<Void> errorResponse = ResponseApi.INTERNAL_SERVER_ERROR("Method Not Allowed", null);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
    }

    @ExceptionHandler(InterruptedException.class)
    public ResponseEntity<ResponseApi<Void>> handleMethodNotAllowed(InterruptedException ex) {
        log.error("InterruptedException", ex);

        ResponseApi<Void> errorResponse = ResponseApi.INTERNAL_SERVER_ERROR(ex.getMessage(), null);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
    }

    @ExceptionHandler(ArithmeticException.class)
    public ResponseEntity<ResponseApi<Void>> handleDivisionByZeroException(ArithmeticException ex) {
        log.error("ArithmeticException", ex);

        ResponseApi<Void> errorResponse = ResponseApi.INTERNAL_SERVER_ERROR(ex.getMessage(), null);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ResponseApi<Void>> handleIllegalArgumentException(IllegalArgumentException ex) {
        log.error("IllegalArgumentException", ex);

        ResponseApi<Void> errorResponse = ResponseApi.INTERNAL_SERVER_ERROR(ex.getMessage(), null);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
    }
}
