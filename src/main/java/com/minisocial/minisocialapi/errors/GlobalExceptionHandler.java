package com.minisocial.minisocialapi.errors;

import jakarta.ejb.EJBException;
import jakarta.ws.rs.ClientErrorException;
import jakarta.ws.rs.WebApplicationException;
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
        try {
            // for manual throws
            if (exception instanceof ApiException) {
                ApiException apiEx = (ApiException) exception;
                Map<String, String> error = new HashMap<>();
                error.put("error", apiEx.getMessage());
                error.put("status", String.valueOf(apiEx.getStatus().getStatusCode()));
                return Response.status(apiEx.getStatus())
                        .entity(error)
                        .type(MediaType.APPLICATION_JSON)
                        .build();
            }

            // for invalid routes for instance
            if (exception instanceof WebApplicationException) {
                WebApplicationException webEx = (WebApplicationException) exception;
                System.out.println("ClientErrorException");
                int status = webEx.getResponse().getStatus();
                Map<String, String> error = new HashMap<>();
                error.put("error", webEx.getMessage());
                error.put("status", String.valueOf(status));
                return Response.status(status).entity(error).type(MediaType.APPLICATION_JSON).build();
            }

            // else it is a bug, return 500 internal server error
            Map<String, String> error = new HashMap<>();
            if(exception.getMessage() != null) {
                error.put("error", exception.getMessage());
            } else {
                error.put("error", "Internal server error");
            }
            error.put("status", "500");
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(error).type(MediaType.APPLICATION_JSON).build();
        } catch (Exception e) {
            e.printStackTrace();
            Map<String, String> error = new HashMap<>();
            error.put("error", "Internal server error");
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(error).type(MediaType.APPLICATION_JSON).build();
        }
    }
}
