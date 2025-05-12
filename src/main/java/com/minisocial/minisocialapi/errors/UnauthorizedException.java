package com.minisocial.minisocialapi.errors;
import jakarta.ws.rs.core.Response;

public class UnauthorizedException extends ApiException {
    public UnauthorizedException(String message) {
        super(message, Response.Status.UNAUTHORIZED);
    }
}

