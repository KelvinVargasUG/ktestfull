package com.sasf.ktestfull.Config.GlobalExceptionHandler;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

import com.sasf.ktestfull.Dto.ApiGenericResponse;
import com.sasf.ktestfull.Exception.AccessDeniedException;
import com.sasf.ktestfull.Exception.DuplicateResourceException;
import com.sasf.ktestfull.Exception.ResourceNotFoundException;
import com.sasf.ktestfull.Util.ResponseUtil;

/**
 * Global exception handler for the application.
 * Handles exceptions and returns appropriate responses.
 */
@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ApiGenericResponse<Object>> handleResourceNotFoundException(
            ResourceNotFoundException ex, WebRequest request) {

        return ResponseUtil.error(ex.getMessage(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ApiGenericResponse<Object>> handleAccessDeniedException(
            AccessDeniedException ex, WebRequest request) {

        return ResponseUtil.error(ex.getMessage(), HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ApiGenericResponse<Object>> handleIllegalArgumentException(
            IllegalArgumentException ex, WebRequest request) {

        return ResponseUtil.error(ex.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(DuplicateResourceException.class)
    public ResponseEntity<ApiGenericResponse<Object>> handleDuplicateResourceException(
            DuplicateResourceException ex, WebRequest request) {

        return ResponseUtil.error(ex.getMessage(), HttpStatus.CONFLICT);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiGenericResponse<Object>> handleGlobalException(
            Exception ex, WebRequest request) {
        return ResponseUtil.error(ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    }
}