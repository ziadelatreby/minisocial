package com.minisocial.minisocialapi.errors;

import jakarta.ws.rs.core.Response;

public class BadRequestException extends ApiException {
    public BadRequestException(String message) {
        super(message, Response.Status.BAD_REQUEST);
    }
}