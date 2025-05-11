package com.minisocial.minisocialapi.errors;

import jakarta.ws.rs.ClientErrorException;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;

import java.util.HashMap;
import java.util.Map;

@Provider
public class GlobalExceptionHandler implements ExceptionMapper<Throwable> {

    @Override
    public Response toResponse(Throwable exception) {
        if (exception instanceof ClientErrorException) {
            ClientErrorException apiEx = (ClientErrorException) exception;
            int status = apiEx.getResponse().getStatus();
            Map<String, String> error = new HashMap<>();
            error.put("error", apiEx.getMessage());
            error.put("status", String.valueOf(status));
            return Response.status(status).entity(error).type(MediaType.APPLICATION_JSON).build();
        }

        Map<String, String> error = new HashMap<>();
        if(exception.getMessage() != null) {
            error.put("error", exception.getMessage());
        } else {
            error.put("error", "Internal server error");
        }
        error.put("status", "500");
        return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(error).type(MediaType.APPLICATION_JSON).build();
    }
}
