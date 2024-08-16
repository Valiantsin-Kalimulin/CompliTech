/*
 * (c) 2024 Valiantsin Kalimulin. All Right Reserved. All information contained herein is, and remains the
 * property of Valiantsin Kalimulin and/or its suppliers and is protected by international intellectual
 * property law. Dissemination of this information or reproduction of this material is strictly forbidden,
 * unless prior written permission is obtained from Valiantsin Kalimulin
 */

package com.complitech.complitech_test_task.exception;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.core.NestedRuntimeException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;

@ControllerAdvice
public class ExceptionHandlerAdvice {

    @ExceptionHandler(ValidationException.class)
    public ResponseEntity<String> handleValidationException(HttpServletRequest request, ValidationException e) {
        return ResponseEntity.status(BAD_REQUEST).body(request.getRequestURI() + " " + e.getMessage());
    }

    @ExceptionHandler({Exception.class, NestedRuntimeException.class})
    public ResponseEntity<String> handleException(HttpServletRequest request, Exception O_o) {
        return ResponseEntity.status(INTERNAL_SERVER_ERROR).body(request.getRequestURI() + " " + O_o.getMessage());
    }
}
