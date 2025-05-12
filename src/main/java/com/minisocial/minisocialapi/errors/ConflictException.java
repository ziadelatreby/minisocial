package com.minisocial.minisocialapi.errors;

import jakarta.ws.rs.core.Response;

public class ConflictException extends ApiException {
    public ConflictException(String message) {
        super(message, Response.Status.CONFLICT);
    }
}
