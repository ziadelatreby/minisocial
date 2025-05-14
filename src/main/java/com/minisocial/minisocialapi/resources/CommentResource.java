package com.minisocial.minisocialapi.resources;

import com.minisocial.minisocialapi.dtos.CommentDTO;
import com.minisocial.minisocialapi.entities.Comment;
import com.minisocial.minisocialapi.errors.BadRequestException;
import com.minisocial.minisocialapi.errors.NotFoundException;
import com.minisocial.minisocialapi.mapper.CommentMapper;
import com.minisocial.minisocialapi.services.CommentService;

import jakarta.inject.Inject;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import jakarta.servlet.http.HttpServletRequest;

@Path("/comments")
public class CommentResource {

    @Inject
    private CommentService commentService;



    

    @POST
    @Path("/{postId}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response createComment(@PathParam("postId") Long postId, @Context HttpServletRequest request, CommentDTO commentDTO) {
        String userIdStr = request.getAttribute("ctxUserId").toString();
        Long userId = Long.parseLong(userIdStr);

        Comment comment = null;

        try {
           comment= commentService.createComment(postId, commentDTO, userId);
        } catch (BadRequestException e) {
            return Response.status(Response.Status.BAD_REQUEST).entity(e.getMessage()).build();
        } catch (NotFoundException e) {
            return Response.status(Response.Status.NOT_FOUND).entity(e.getMessage()).build();
        } 

        if(comment == null) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("Failed to create comment").build();
        }

        return Response.status(Response.Status.CREATED).entity(comment).build();
    }
}
