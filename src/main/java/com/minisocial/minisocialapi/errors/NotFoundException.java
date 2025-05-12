package com.minisocial.minisocialapi.errors;

import com.minisocial.minisocialapi.errors.ApiException;
import jakarta.ws.rs.core.Response;
public class NotFoundException extends ApiException {
    public NotFoundException(String message) {
        super(message, Response.Status.NOT_FOUND);
    }
}