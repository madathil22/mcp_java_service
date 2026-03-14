package com.example.mcpjavaservice.api;

import com.example.mcpjavaservice.department.DepartmentNotFoundException;
import com.example.mcpjavaservice.employee.EmployeeNotFoundException;
import com.example.mcpjavaservice.ui.response.ErrorResponse;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.Instant;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler({DepartmentNotFoundException.class, EmployeeNotFoundException.class})
    public ResponseEntity<ErrorResponse> handleNotFound(RuntimeException exception, HttpServletRequest request) {
        HttpStatus status = HttpStatus.NOT_FOUND;
        ErrorResponse errorResponse = new ErrorResponse(
            Instant.now(),
            status.value(),
            status.getReasonPhrase(),
            exception.getMessage(),
            request.getRequestURI()
        );
        return ResponseEntity.status(status).body(errorResponse);
    }

    @ExceptionHandler({IllegalArgumentException.class, IllegalStateException.class})
    public ResponseEntity<ErrorResponse> handleBadRequest(RuntimeException exception, HttpServletRequest request) {
        HttpStatus status = exception instanceof IllegalStateException
            ? HttpStatus.SERVICE_UNAVAILABLE
            : HttpStatus.BAD_REQUEST;
        ErrorResponse errorResponse = new ErrorResponse(
            Instant.now(),
            status.value(),
            status.getReasonPhrase(),
            exception.getMessage(),
            request.getRequestURI()
        );
        return ResponseEntity.status(status).body(errorResponse);
    }
}
