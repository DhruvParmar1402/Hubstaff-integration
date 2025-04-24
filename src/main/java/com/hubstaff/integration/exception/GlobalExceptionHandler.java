package com.hubstaff.integration.exception;

import com.hubstaff.integration.util.ResponseHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class GlobalExceptionHandler {

    private final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ResponseHandler<?>> handleValidationExceptions(MethodArgumentNotValidException ex) {
        ResponseHandler<Map<String, String>> response = new ResponseHandler<>();
        response.setStatus(HttpStatus.BAD_REQUEST);
        response.setSuccess(false);

        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach(error -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });

        response.setMessage("Validation failed");
        response.setData(errors);

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }


    @ExceptionHandler(ExternalApiException.class)
    public ResponseEntity<ResponseHandler<Object>> handleExternalApiException(ExternalApiException ex) {
        logger.error("External API Error: {}", ex.getMessage(), ex);
        ResponseHandler<Object> response = new ResponseHandler<>(
                null,
                ex.getMessage(),
                HttpStatus.valueOf(ex.getStatusCode()),
                false
        );
        return new ResponseEntity<>(response,HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ResponseHandler<Object>> handleGeneralException(Exception ex) {
        logger.error("Unexpected Error: {}", ex.getMessage(), ex);
        ResponseHandler<Object> response = new ResponseHandler<>(
                null,
                ex.getMessage(),
                HttpStatus.INTERNAL_SERVER_ERROR,
                false
        );
        return new ResponseEntity<>(response, response.getStatus());
    }
}
