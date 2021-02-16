package com.stackstech.honeybee.server.core.handler;

import com.google.common.collect.Maps;
import com.stackstech.honeybee.common.entity.ResponseObject;
import com.stackstech.honeybee.server.core.enums.StatusCode;
import com.stackstech.honeybee.server.core.exception.DataNotFoundException;
import com.stackstech.honeybee.server.core.exception.ServerException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@ControllerAdvice(annotations = RestController.class)
public class ServerExceptionHandler {

    @ResponseBody
    @ExceptionHandler
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    private ResponseObject onDefaultExceptionHandler(HttpServletRequest request, Exception e) {
        log.error("server error", e);
        return ResponseObject.build().failed(StatusCode.INTERNAL_ERROR, e.getMessage());
    }

    @ResponseBody
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(value = ServerException.class)
    private ResponseObject onServerExceptionHandler(HttpServletRequest request, ServerException e) {
        log.error(e.getMessage());
        return ResponseObject.build().failed(e.getMessage());
    }

    @ResponseBody
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(value = DataNotFoundException.class)
    private ResponseObject onDataNotFoundExceptionHandler(HttpServletRequest request, DataNotFoundException e) {
        log.warn(e.getMessage());
        return ResponseObject.build().failed(StatusCode.NOT_FOUND, e.getMessage());
    }

    @ResponseBody
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    private ResponseObject onMethodArgumentNotValidExceptionHandler(HttpServletRequest request, MethodArgumentNotValidException e) {
        log.warn(e.getMessage());
        BindingResult bindingResult = e.getBindingResult();

        if (bindingResult.hasErrors()) {
            List<FieldError> errors = bindingResult.getFieldErrors();
            Map<String, Object> messages = errors.stream().collect(
                    Collectors.toMap(FieldError::getField, DefaultMessageSourceResolvable::getDefaultMessage, (a, b) -> b, Maps::newLinkedHashMap)
            );
            return ResponseObject.build().failed(StatusCode.BAD_REQUEST, messages, bindingResult.getFieldErrorCount());
        }
        return ResponseObject.build().failed(StatusCode.BAD_REQUEST, "request method argument not valid error");
    }
}