package com.minisocial.minisocialapi.resources;

import jakarta.inject.Inject;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.Response;
import jakarta.servlet.http.HttpServletRequest;

@Path("/likes")
public class LikeResource {

    // @Inject
    // private LikeService likeService;

    @POST
    @Path("/{postId}")
    public Response likePost(@PathParam("postId") Long postId, @Context HttpServletRequest request) {
        Long userId = (Long) request.getAttribute("userId");
       // likeService.likePost(postId, userId);
        return Response.ok().build();
    }
}