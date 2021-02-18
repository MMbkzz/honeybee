package com.stackstech.honeybee.server.core.handler;

import com.google.common.collect.Maps;
import com.stackstech.honeybee.common.entity.ResponseObject;
import com.stackstech.honeybee.server.core.enums.StatusCode;
import com.stackstech.honeybee.server.core.exception.AuthenticationException;
import com.stackstech.honeybee.server.core.exception.DataNotFoundException;
import com.stackstech.honeybee.server.core.exception.ServerException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * Server exception handler
 *
 * @author william
 * @since 1.0
 */
@Slf4j
@ControllerAdvice(annotations = RestController.class)
public class ServerExceptionHandler {

    @ResponseBody
    @ExceptionHandler
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    private ResponseObject onDefaultExceptionHandler(Exception e) {
        String code = UUID.randomUUID().toString().toLowerCase();
        log.error(StringUtils.join("Error code [", code, "]: "), e);
        return ResponseObject.build().failed(StatusCode.INTERNAL_ERROR, MessageHandler.of().message("server.internal.error", new Object[]{code}));
    }

    @ResponseBody
    @ResponseStatus(HttpStatus.OK)
    @ExceptionHandler(value = ServerException.class)
    private ResponseObject onServerExceptionHandler(ServerException e) {
        log.error(e.getMessage());
        return ResponseObject.build().failed(e.getMessage());
    }

    @ResponseBody
    @ResponseStatus(HttpStatus.OK)
    @ExceptionHandler(value = DataNotFoundException.class)
    private ResponseObject onDataNotFoundExceptionHandler(DataNotFoundException e) {
        log.warn(e.getMessage());
        return ResponseObject.build().failed(e.getMessage());
    }

    @ResponseBody
    @ResponseStatus(HttpStatus.OK)
    @ExceptionHandler(value = AuthenticationException.class)
    private ResponseObject onAuthenticationExceptionHandler(AuthenticationException e) {
        log.warn(e.getMessage());
        return ResponseObject.build().failed(StatusCode.UNAUTHORIZED, e.getMessage());
    }


    @ResponseBody
    @ResponseStatus(HttpStatus.OK)
    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    private ResponseObject onMethodArgNotValidExceptionHandler(MethodArgumentNotValidException e) {
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
