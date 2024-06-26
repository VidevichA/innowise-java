package com.onlineshop.orderservice.exception.resolver;

import org.slf4j.Logger;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.client.HttpClientErrorException;

import lombok.Data;

@RestControllerAdvice
public class ExceptionsResolver {

    @ExceptionHandler(HttpClientErrorException.class)
    public ResponseEntity<ErrorResponse> handleHttpClientErrorException(HttpClientErrorException ex) {
        Logger logger = org.slf4j.LoggerFactory.getLogger(ExceptionsResolver.class);
        logger.error(ex.getResponseBodyAsString());
        ErrorResponse errorResponse = new ErrorResponse(ex.getResponseBodyAsString());
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
