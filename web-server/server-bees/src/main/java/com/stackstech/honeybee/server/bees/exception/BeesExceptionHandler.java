package com.stackstech.honeybee.server.bees.exception;

import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

import javax.servlet.http.HttpServletRequest;

@ControllerAdvice
public class BeesExceptionHandler {

    @SuppressWarnings("rawtypes")
    @ExceptionHandler(BeesException.ServiceException.class)
    public ResponseEntity handleGriffinExceptionOfServer(
            HttpServletRequest request,
            BeesException.ServiceException e) {
        String message = e.getMessage();
        Throwable cause = e.getCause();
        BeesExceptionResponse body = new BeesExceptionResponse(
                HttpStatus.INTERNAL_SERVER_ERROR,
                message, request.getRequestURI(), cause.getClass().getName());
        return new ResponseEntity<>(body, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @SuppressWarnings("rawtypes")
    @ExceptionHandler(BeesException.class)
    public ResponseEntity handleGriffinExceptionOfClient(
            HttpServletRequest request, BeesException e) {
        ResponseStatus responseStatus = AnnotationUtils.findAnnotation(
                e.getClass(), ResponseStatus.class);
        HttpStatus status = responseStatus.code();
        String code = e.getMessage();
        BeesExceptionMessage message = BeesExceptionMessage
                .valueOf(Integer.parseInt(code));
        BeesExceptionResponse body = new BeesExceptionResponse(
                status, message, request.getRequestURI());
        return new ResponseEntity<>(body, status);
    }
}
