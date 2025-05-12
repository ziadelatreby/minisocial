package com.minisocial.minisocialapi.resources;

import com.minisocial.minisocialapi.dtos.CommentDTO;
import com.minisocial.minisocialapi.dtos.PostDTO;
import com.minisocial.minisocialapi.entities.Comment;
import com.minisocial.minisocialapi.entities.Post;
import com.minisocial.minisocialapi.services.CommentService;
import com.minisocial.minisocialapi.services.PostService;
import jakarta.annotation.security.RolesAllowed;
import jakarta.ejb.EJB;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.SecurityContext;

import java.security.Principal;
import java.util.List;
import java.util.stream.Collectors;

@Path("/posts")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class PostResource {
    
    @EJB
    private PostService postService;
    
    @EJB
    private CommentService commentService;
    
    @Context
    private SecurityContext securityContext;
    
    private Long getCurrentUserId() {
        Principal principal = securityContext.getUserPrincipal();
        if (principal == null) {
            throw new NotAuthorizedException("Not authenticated");
        }
        return Long.parseLong(principal.getName());
    }
    
    @POST
    @RolesAllowed({"user", "admin"})
    public Response createPost(@Valid PostDTO postDTO) {
        Post post = postService.createPost(
            postDTO.getContent(), 
            postDTO.getImageUrl(), 
            getCurrentUserId()
        );
        
        return Response.status(Response.Status.CREATED)
            .entity(new PostDTO(post, false))
            .build();
    }
    
    @GET
    @Path("/{postId}")
    @RolesAllowed({"user", "admin"})
    public Response getPost(@PathParam("postId") Long postId) {
        Post post = postService.findById(postId);
        boolean liked = postService.isPostLikedByUser(post, getCurrentUserId());
        return Response.ok(new PostDTO(post, liked)).build();
    }
    
    @PUT
    @Path("/{postId}")
    @RolesAllowed({"user", "admin"})
    public Response updatePost(@PathParam("postId") Long postId, @Valid PostDTO postDTO) {
        Post post = postService.updatePost(
            postId, 
            postDTO.getContent(), 
            postDTO.getImageUrl(), 
            getCurrentUserId()
        );
        
        boolean liked = postService.isPostLikedByUser(post, getCurrentUserId());
        return Response.ok(new PostDTO(post, liked)).build();
    }
    
    @DELETE
    @Path("/{postId}")
    @RolesAllowed({"user", "admin"})
    public Response deletePost(@PathParam("postId") Long postId) {
        postService.deletePost(postId, getCurrentUserId());
        return Response.noContent().build();
    }
    
    @PATCH
    @Path("/{postId}/likes")
    @RolesAllowed({"user", "admin"})
    public Response likePost(@PathParam("postId") Long postId) {
        Post post = postService.likePost(postId, getCurrentUserId());
        return Response.ok(new PostDTO(post, true)).build();
    }
    
    @GET
    @Path("/feed/{userId}")
    @RolesAllowed({"user", "admin"})
    public Response getUserFeed(@PathParam("userId") Long userId) {
        List<Post> posts = postService.getUserFeed(userId);
        Long currentUserId = getCurrentUserId();
        
        List<PostDTO> feed = posts.stream()
            .map(post -> new PostDTO(post, postService.isPostLikedByUser(post, currentUserId)))
            .collect(Collectors.toList());
        
        return Response.ok(feed).build();
    }
    
    @POST
    @Path("/{postId}/comments")
    @RolesAllowed({"user", "admin"})
    public Response addComment(@PathParam("postId") Long postId, @Valid CommentDTO commentDTO) {
        Comment comment = commentService.createComment(
            commentDTO.getContent(), 
            postId, 
            getCurrentUserId()
        );
        
        return Response.status(Response.Status.CREATED)
            .entity(new CommentDTO(comment))
            .build();
    }
    
    @GET
    @Path("/{postId}/comments")
    @RolesAllowed({"user", "admin"})
    public Response getPostComments(@PathParam("postId") Long postId) {
        List<Comment> comments = commentService.getPostComments(postId);
        List<CommentDTO> commentDTOs = comments.stream()
            .map(CommentDTO::new)
            .collect(Collectors.toList());
        
        return Response.ok(commentDTOs).build();
    }
    
    @PUT
    @Path("/comments/{commentId}")
    @RolesAllowed({"user", "admin"})
    public Response updateComment(
        @PathParam("commentId") Long commentId, 
        @Valid CommentDTO commentDTO
    ) {
        Comment comment = commentService.updateComment(
            commentId, 
            commentDTO.getContent(), 
            getCurrentUserId()
        );
        
        return Response.ok(new CommentDTO(comment)).build();
    }
    
    @DELETE
    @Path("/comments/{commentId}")
    @RolesAllowed({"user", "admin"})
    public Response deleteComment(@PathParam("commentId") Long commentId) {
        commentService.deleteComment(commentId, getCurrentUserId());
        return Response.noContent().build();
    }
}
