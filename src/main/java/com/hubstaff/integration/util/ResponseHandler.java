package com.hubstaff.integration.util;

import lombok.*;
import org.springframework.http.HttpStatus;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class ResponseHandler<T>{
    private T data;
    private String message;
    private HttpStatus status;
    private boolean success;
}
