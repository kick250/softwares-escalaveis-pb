package com.erp.server.infra;

import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<?> emptyBodyHandler() {
        return ResponseEntity.badRequest().body("Corpo da requisição vazio ou inválido.");
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> invalidRequestHandler(MethodArgumentNotValidException exception) {
        Map<String, String> errors = new HashMap<>();

        exception.getBindingResult().getFieldErrors().forEach(error ->
                errors.put(error.getField(), error.getDefaultMessage())
        );

        return ResponseEntity.badRequest().body(errors);
    }

    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ResponseEntity<Map<String, String>> handleMissingParams(MissingServletRequestParameterException ex) {
        String name = ex.getParameterName();
        return ResponseEntity
                .badRequest()
                .body(Map.of("error", "O parâmetro '" + name + "' é obrigatório."));
    }

    @ExceptionHandler(NoResourceFoundException.class)
    public ResponseEntity<String> Handler(Exception exception) {
        return ResponseEntity.status(404).body("Essa rota não existe.");
    }

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<String> unknownMethodHandler(Exception ignored) {
        return ResponseEntity.status(404).body("Esse metodo não existe.");
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<String> TypeMismatchHandler(Exception exception) {
        return ResponseEntity.badRequest().body("Algum parametro está inválido.");
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> unknownErrorHandler(Exception exception) {
        return ResponseEntity.status(500).body("Ocorreu um erro desconhecido. Tente mais tarde.");
    }
}