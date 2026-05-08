package com.shopflow.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(ShopFlowException.class)
    public ResponseEntity<ErrorResponse> handleShopFlowException(ShopFlowException ex) {
        log.warn("ShopFlow exception: {}", ex.getMessage());
        return ResponseEntity.status(ex.getStatus())
                .body(ErrorResponse.of(ex.getMessage(), ex.getStatus().value()));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidationException(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach(error -> {
            String fieldName = ((FieldError) error).getField();
            String message = error.getDefaultMessage();
            errors.put(fieldName, message);
        });
        ErrorResponse response = ErrorResponse.builder()
                .message("Erreur de validation")
                .status(HttpStatus.BAD_REQUEST.value())
                .timestamp(LocalDateTime.now())
                .validationErrors(errors)
                .build();
        return ResponseEntity.badRequest().body(response);
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ErrorResponse> handleAccessDeniedException(AccessDeniedException ex) {
        return ResponseEntity.status(HttpStatus.FORBIDDEN)
                .body(ErrorResponse.of("Accès refusé", HttpStatus.FORBIDDEN.value()));
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<ErrorResponse> handleBadCredentials(BadCredentialsException ex) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(ErrorResponse.of("Email ou mot de passe incorrect", HttpStatus.UNAUTHORIZED.value()));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGenericException(Exception ex) {
        log.error("Unexpected error: ", ex);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ErrorResponse.of("Erreur interne du serveur", HttpStatus.INTERNAL_SERVER_ERROR.value()));
    }

    public record ErrorResponse(
            String message,
            int status,
            LocalDateTime timestamp,
            Map<String, String> validationErrors
    ) {
        public static ErrorResponse of(String message, int status) {
            return new ErrorResponse(message, status, LocalDateTime.now(), null);
        }

        public static Builder builder() {
            return new Builder();
        }

        public static class Builder {
            private String message;
            private int status;
            private LocalDateTime timestamp;
            private Map<String, String> validationErrors;

            public Builder message(String message) { this.message = message; return this; }
            public Builder status(int status) { this.status = status; return this; }
            public Builder timestamp(LocalDateTime timestamp) { this.timestamp = timestamp; return this; }
            public Builder validationErrors(Map<String, String> errors) { this.validationErrors = errors; return this; }
            public ErrorResponse build() { return new ErrorResponse(message, status, timestamp, validationErrors); }
        }
    }
}
