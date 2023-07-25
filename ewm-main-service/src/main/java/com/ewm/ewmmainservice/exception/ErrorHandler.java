package com.ewm.ewmmainservice.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Map;

@RestControllerAdvice
@Slf4j
public class ErrorHandler {

    @ExceptionHandler(NotFoundedException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public Map<String, String> objectNotFoundedException(final NotFoundedException e) {
        log.debug("Получен статус 404 Not found {}", e.getMessage(), e);
        return Map.of("exception", e.getMessage());
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public Map<String, String> objectNotFoundedException(final Exception e) {
        log.debug("Получен статус 500 Not found {}", e.getMessage(), e);
        return Map.of("exception", e.getMessage());
    }

    @ExceptionHandler(Throwable.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public Map<String, String> objectNotFoundedException(final Throwable e) {
        log.debug("Получен статус 500 Not found {}", e.getMessage(), e);
        return Map.of("exception", e.getMessage());
    }

    /*
    @ExceptionHandler(InvalidDateException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Map<String, String> dateException(final Exception e) {
        log.debug("Получен статус 400 Bad Request {}", e.getMessage(), e);
        return Map.of("exception", e.getMessage());
    }*/

    @ExceptionHandler(IllegalArgumentException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Map<String, String> statusException(final Exception e) {
        log.debug("Получен статус 400 Bad Request {}", e.getMessage(), e);
        return Map.of("error", e.getMessage());
    }

    @ExceptionHandler(BadRequestException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Map<String, String> badRequest(final BadRequestException e) {
        log.debug("Получен статус 400 Bad Request {}", e.getMessage(), e);
        return Map.of("error", e.getMessage());
    }

    @ExceptionHandler(ConflictException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public Map<String, String> conflict(final ConflictException e) {
        log.debug("Получен статус 409 Conflict {}", e.getMessage(), e);
        return Map.of("error", e.getMessage());
    }

}
