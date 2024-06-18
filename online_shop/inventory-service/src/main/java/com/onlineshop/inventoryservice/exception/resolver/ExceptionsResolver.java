package com.onlineshop.inventoryservice.exception.resolver;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.client.HttpClientErrorException;

import lombok.Data;

@ControllerAdvice
public class ExceptionsResolver {

    @ExceptionHandler(HttpClientErrorException.class)
    public ResponseEntity<ErrorResponse> handleHttpClientErrorException(HttpClientErrorException ex) {
        ErrorResponse errorResponse = new ErrorResponse(ex.getStatusText());
        return new ResponseEntity<>(errorResponse, ex.getStatusCode());
    }

    @Data
    private static class ErrorResponse {
        private final String message;

        public ErrorResponse(String message) {
            this.message = message;
        }
    }

}
