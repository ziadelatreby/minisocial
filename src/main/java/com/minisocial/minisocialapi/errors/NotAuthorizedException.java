package com.minisocial.minisocialapi.errors;

import jakarta.ws.rs.core.Response;

public class NotAuthorizedException extends ApiException{
    public NotAuthorizedException(String message) {
        super(message, Response.Status.CONFLICT);
    }
}
