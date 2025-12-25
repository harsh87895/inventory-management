package com.inventory.exception;

import com.inventory.dto.response.ErrorResponse;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Path;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.context.request.WebRequest;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class GlobalExceptionHandlerTest {

    private GlobalExceptionHandler exceptionHandler;
    private WebRequest webRequest;

    @BeforeEach
    void setUp() {
        exceptionHandler = new GlobalExceptionHandler();
        webRequest = mock(WebRequest.class);
        when(webRequest.getDescription(false)).then(invocation -> "uri=/api/v1/test");
    }

    @Test
    void handleResourceNotFoundException() {
        ResourceNotFoundException ex = new ResourceNotFoundException("Resource not found");
        ResponseEntity<ErrorResponse> response = exceptionHandler.handleResourceNotFoundException(ex, webRequest);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        ErrorResponse errorResponse = response.getBody();
        assertNotNull(errorResponse);
        assertEquals(HttpStatus.NOT_FOUND.value(), errorResponse.getStatus());
        assertEquals("Not Found", errorResponse.getError());
        assertEquals("Resource not found", errorResponse.getMessage());
        assertEquals("/api/v1/test", errorResponse.getPath());
        assertNotNull(errorResponse.getTimestamp());
    }

    @Test
    void handleEntityNotFoundException() {
        EntityNotFoundException ex = new EntityNotFoundException("Entity not found");
        ResponseEntity<ErrorResponse> response = exceptionHandler.handleEntityNotFoundException(ex, webRequest);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        ErrorResponse errorResponse = response.getBody();
        assertNotNull(errorResponse);
        assertEquals(HttpStatus.NOT_FOUND.value(), errorResponse.getStatus());
        assertEquals("Not Found", errorResponse.getError());
        assertEquals("Entity not found", errorResponse.getMessage());
        assertEquals("/api/v1/test", errorResponse.getPath());
        assertNotNull(errorResponse.getTimestamp());
    }

    @Test
    void handleMethodArgumentNotValidException() {
        MethodArgumentNotValidException ex = mock(MethodArgumentNotValidException.class);
        BindingResult bindingResult = mock(BindingResult.class);
        when(ex.getBindingResult()).thenReturn(bindingResult);
        
        FieldError fieldError = new FieldError("object", "field", "Field error message");
        when(bindingResult.getAllErrors()).thenReturn(List.of(fieldError));

        ResponseEntity<ErrorResponse> response = exceptionHandler.handleValidationExceptions(ex, webRequest);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        ErrorResponse errorResponse = response.getBody();
        assertNotNull(errorResponse);
        assertEquals(HttpStatus.BAD_REQUEST.value(), errorResponse.getStatus());
        assertEquals("Validation Failed", errorResponse.getError());
        assertEquals("Invalid input parameters", errorResponse.getMessage());
        assertEquals("/api/v1/test", errorResponse.getPath());
        assertNotNull(errorResponse.getTimestamp());
        assertNotNull(errorResponse.getValidationErrors());
        assertEquals("Field error message", errorResponse.getValidationErrors().get("field"));
    }

    @Test
    void handleDataIntegrityViolation() {
        DataIntegrityViolationException ex = mock(DataIntegrityViolationException.class);
        when(ex.getMostSpecificCause()).thenReturn(new RuntimeException("constraint violation message"));

        ResponseEntity<ErrorResponse> response = exceptionHandler.handleDataIntegrityViolation(ex, webRequest);

        assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
        ErrorResponse errorResponse = response.getBody();
        assertNotNull(errorResponse);
        assertEquals(HttpStatus.CONFLICT.value(), errorResponse.getStatus());
        assertEquals("Conflict", errorResponse.getError());
        assertEquals("A record with the same unique identifier already exists", errorResponse.getMessage());
        assertEquals("/api/v1/test", errorResponse.getPath());
        assertNotNull(errorResponse.getTimestamp());
    }

    @Test
    void handleConstraintViolation() {
        Set<ConstraintViolation<?>> violations = new HashSet<>();
        ConstraintViolation<?> violation = mock(ConstraintViolation.class);
        Path path = mock(Path.class);
        when(violation.getPropertyPath()).thenReturn(path);
        when(path.toString()).thenReturn("field");
        when(violation.getMessage()).thenReturn("Constraint error message");
        violations.add(violation);

        ConstraintViolationException ex = new ConstraintViolationException("Constraint violation", violations);

        ResponseEntity<ErrorResponse> response = exceptionHandler.handleConstraintViolation(ex, webRequest);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        ErrorResponse errorResponse = response.getBody();
        assertNotNull(errorResponse);
        assertEquals(HttpStatus.BAD_REQUEST.value(), errorResponse.getStatus());
        assertEquals("Validation Failed", errorResponse.getError());
        assertEquals("Constraint violation", errorResponse.getMessage());
        assertEquals("/api/v1/test", errorResponse.getPath());
        assertNotNull(errorResponse.getTimestamp());
        assertNotNull(errorResponse.getValidationErrors());
        assertEquals("Constraint error message", errorResponse.getValidationErrors().get("field"));
    }

    @Test
    void handleAllUncaughtException() {
        Exception ex = new RuntimeException("Unexpected error");
        ResponseEntity<ErrorResponse> response = exceptionHandler.handleAllUncaughtException(ex, webRequest);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        ErrorResponse errorResponse = response.getBody();
        assertNotNull(errorResponse);
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR.value(), errorResponse.getStatus());
        assertEquals("Internal Server Error", errorResponse.getError());
        assertEquals("An unexpected error occurred", errorResponse.getMessage());
        assertEquals("/api/v1/test", errorResponse.getPath());
        assertNotNull(errorResponse.getTimestamp());
    }
} 