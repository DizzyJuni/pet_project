package com.example.shop.exception;

import com.example.shop.domian.Product;
import com.example.shop.dto.ErrorResponseDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.LocalDateTime;

@ControllerAdvice
public class GlobalExceptionHandler {

    private final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponseDTO> handlerGenericException(Exception e) {
        log.error("Handler exception: {}", String.valueOf(e));
        var error = new ErrorResponseDTO("Internal server error.",
                e.getMessage(),
                LocalDateTime.now());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(error);
    }

    @ExceptionHandler(ProductNotFoundException.class)
    public ResponseEntity<ErrorResponseDTO> handlerProductNotFoundException(ProductNotFoundException e) {
        log.error("Handler product not found exception: {}", String.valueOf(e));
        var error = new ErrorResponseDTO("Product not found.",
                e.getMessage(),
                LocalDateTime.now());
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(error);
    }

    @ExceptionHandler(CategoryNotFoundException.class)
    public ResponseEntity<ErrorResponseDTO> handlerCategoryNotFoundException(CategoryNotFoundException e) {
        log.error("Handler category not found exception: {}", String.valueOf(e));
        var error = new ErrorResponseDTO("Category not found",
                e.getMessage(),
                LocalDateTime.now());
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(error);
    }
}
