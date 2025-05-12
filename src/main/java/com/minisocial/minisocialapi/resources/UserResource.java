package com.minisocial.minisocialapi.resources;

// import com.minisocial.minisocialapi.dtos.UserDTO;
import com.minisocial.minisocialapi.entities.User;
import com.minisocial.minisocialapi.services.UserService;
import jakarta.inject.Inject;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.ws.rs.*;
// import jakarta.ws.rs.container.ContainerRequestContext;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.util.HashMap;
import java.util.Map;

@Path("/users")
public class UserResource {
    @Inject
    private UserService userService;

    private final String ctxUserIdAttributeName = "ctxUserId";

    @POST
    @Path("/signup")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response signup(User user) {
        userService.signup(user);
        return Response.status(Response.Status.CREATED).build();
    }

    @POST
    @Path("/signin")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response signin(User user) {
        String token = userService.signinUser(user);

        Map<String, String> res = new HashMap<>();
        res.put("token", token);



        return Response.status(Response.Status.OK).entity(res).type(MediaType.APPLICATION_JSON).build();
    }

    @PUT
    @Path("/{userId}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response updateProfile(@PathParam("userId") Long userId, UserDTO updateParams, @Context HttpServletRequest ctx) {
        Long ctxUserId = (Long) ctx.getAttribute(ctxUserIdAttributeName);

        UserDTO userDTO =  userService.updateProfile(ctxUserId, userId, updateParams);

        return Response.status(Response.Status.OK).entity(userDTO).type(MediaType.APPLICATION_JSON).build();
    }
}
