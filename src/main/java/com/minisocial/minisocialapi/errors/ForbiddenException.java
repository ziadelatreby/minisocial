package com.minisocial.minisocialapi.errors;

import jakarta.ws.rs.core.Response;

public class ForbiddenException extends ApiException {
    
    public ForbiddenException(String message) {
        super(message, Response.Status.FORBIDDEN);
    }
    
    public ForbiddenException() {
        super("Forbidden", Response.Status.FORBIDDEN);
    }
}
