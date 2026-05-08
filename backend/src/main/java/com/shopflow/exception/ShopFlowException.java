package com.shopflow.exception;

import org.springframework.http.HttpStatus;

public class ShopFlowException extends RuntimeException {

    private final HttpStatus status;

    public ShopFlowException(String message, HttpStatus status) {
        super(message);
        this.status = status;
    }

    public HttpStatus getStatus() {
        return status;
    }

    public static ShopFlowException notFound(String message) {
        return new ShopFlowException(message, HttpStatus.NOT_FOUND);
    }

    public static ShopFlowException badRequest(String message) {
        return new ShopFlowException(message, HttpStatus.BAD_REQUEST);
    }

    public static ShopFlowException forbidden(String message) {
        return new ShopFlowException(message, HttpStatus.FORBIDDEN);
    }

    public static ShopFlowException conflict(String message) {
        return new ShopFlowException(message, HttpStatus.CONFLICT);
    }

    public static ShopFlowException unauthorized(String message) {
        return new ShopFlowException(message, HttpStatus.UNAUTHORIZED);
    }
}
