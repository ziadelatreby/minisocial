package com.minisocial.minisocialapi;

import jakarta.ws.rs.BadRequestException;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;

@Path("/hello-world")
public class HelloResource {
    @GET
    @Produces("text/plain")
    public String hello() {
        boolean testError = true;
        if(testError) {
            throw new BadRequestException( "Invalid request");
        }
        return "Hello, World!";
    }
}