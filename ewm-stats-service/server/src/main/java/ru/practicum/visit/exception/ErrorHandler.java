package ru.practicum.visit.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.validation.ValidationException;
import java.util.Map;

@RestControllerAdvice
@Slf4j
public class ErrorHandler {

    @ExceptionHandler(ValidationException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public Map<String, String> objectNotFoundedException(final ValidationException e) {
        log.debug("Получен статус 404 Not found {}", e.getMessage(), e);
        return Map.of("exception", e.getMessage());
    }


    @ExceptionHandler(BadRequestException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Map<String, String> badRequest(final BadRequestException e) {
        log.debug("Получен статус 400 Bad Request {}", e.getMessage(), e);
        return Map.of("error", e.getMessage());
    }


}

