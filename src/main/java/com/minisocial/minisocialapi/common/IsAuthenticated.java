package com.minisocial.minisocialapi.common;

import com.minisocial.minisocialapi.errors.UnauthorizedException;
import jakarta.annotation.Priority;
import jakarta.ws.rs.Priorities;
import jakarta.ws.rs.container.ContainerRequestContext;
import jakarta.ws.rs.container.ContainerRequestFilter;
import jakarta.ws.rs.ext.Provider;
import io.jsonwebtoken.Claims;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

//  implement a Middleware that is executed with each Api to validate if
//  the user sending this request is authenticated
//  if is authenticated then we store the userId of this context user in the request context object

@Provider
@Priority(Priorities.AUTHENTICATION)
public class IsAuthenticated implements ContainerRequestFilter {


    // add public apis here
    Set<String> WHITE_LISTED_ENDPOINTS = new HashSet<>(Arrays.asList(
            "/users/signin",
            "/users/signup",
            "/hello-world"
    ));

    @Override
    public void filter(ContainerRequestContext requestContext) {
        try {
            String path = requestContext.getUriInfo().getPath();

            System.out.println("GGPATH: " + path);
            // Skip filter for whitelisted paths
            if (WHITE_LISTED_ENDPOINTS.contains(path)) {
                return;
            }

            String authHeader = requestContext.getHeaderString("Authorization"); // auth header
            if (authHeader == null || !authHeader.startsWith("Bearer ")) {
                throw new UnauthorizedException("Authorization header must be provided");
            }

            String token = authHeader.substring("Bearer ".length());
            Claims claims = JwtUtil.decodeToken(token);
            String userId = claims.getSubject(); // we stored the userid as the subject

            if(utils.isNull(userId)) {
                throw new UnauthorizedException("Invalid token");
            }

            // store it in the context object for the other resources usage
            requestContext.setProperty("ctxUserId", Long.parseLong(userId));
        } catch (Exception e) {
            throw new UnauthorizedException(e.getMessage());
        }
    }
}
