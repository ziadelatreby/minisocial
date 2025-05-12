package com.minisocial.minisocialapi.resources;

import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;

import com.minisocial.minisocialapi.dtos.PostDTO;
import com.minisocial.minisocialapi.entities.Post;
import com.minisocial.minisocialapi.errors.NotFoundException;
import com.minisocial.minisocialapi.mapper.PostMapper;
import com.minisocial.minisocialapi.repositories.PostRepository;
import com.minisocial.minisocialapi.repositories.UserRepository;
import com.minisocial.minisocialapi.services.PostService;

import jakarta.ejb.Stateless;
import jakarta.inject.Inject;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.PATCH;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("/posts")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Stateless
public class PostResource {
    
@Inject
private PostService postService;
@Inject
private PostMapper postMapper;
@Inject
private UserRepository userRepository;
@Inject
private PostRepository postRepository;

private final String ctxUserIdAttributeName = "ctxUserId";


@POST
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public Response createPost(@Context HttpServletRequest ctx, PostDTO postDTO){
    long userId = (long) ctx.getAttribute(ctxUserIdAttributeName);

    if(userRepository.findById(userId) == null){
        return Response.status(Response.Status.UNAUTHORIZED).build();
    }

    Post post = postMapper.toEntity(postDTO);
    if(post == null){
        return Response.status(Response.Status.BAD_REQUEST).build();
    }

    Post createdPost = postService.createPost(userId , postDTO);
    if(createdPost == null){
        return Response.status(Response.Status.BAD_REQUEST).build();
    }

    return Response.created(URI.create("/posts/" + createdPost.getId())).entity(postMapper.toDTO(createdPost)).build();
}

@GET
public Response getPosts(@Context HttpServletRequest ctx){
    long userId = (long) ctx.getAttribute(ctxUserIdAttributeName);

    if(userRepository.findById(userId) == null){
        return Response.status(Response.Status.UNAUTHORIZED).build();
    }

    List<Post> posts = postRepository.findByUserId(userId);
    return Response.ok(posts.stream().map(postMapper::toDTO).collect(Collectors.toList())).build();

}

@PATCH
@Path("/{postId}")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public Response editPost(@PathParam("postId") Long postId,
                         @Context HttpServletRequest ctx,
                         PostDTO postDTO){

    long userId = (long) ctx.getAttribute(ctxUserIdAttributeName);

    if(userRepository.findById(userId) == null){
        return Response.status(Response.Status.UNAUTHORIZED).build();
    }

    Post editedPost = postService.editPost(userId, postId, postDTO);
    if(editedPost == null){
        return Response.status(Response.Status.BAD_REQUEST).build();
    }

    return Response.ok(postMapper.toDTO(editedPost)).build();
}

@DELETE
@Path("/{postId}")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public Response deletePost(@PathParam("postId") Long postId,
                          @Context HttpServletRequest ctx){
    long userId = (long) ctx.getAttribute(ctxUserIdAttributeName);

    if(userRepository.findById(userId) == null){
        return Response.status(Response.Status.UNAUTHORIZED).build();
    }

    try {
        boolean deleted = postService.deletePost(userId, postId);
        if(!deleted){
            return Response.status(Response.Status.BAD_REQUEST).build();
        }
    } catch (NotFoundException e) {
        if(e.getMessage().equals("unauthorized user to delete this post")){
            return Response.status(Response.Status.FORBIDDEN).build();
        }
        if(e.getMessage().equals("Post not found")){
            return Response.status(Response.Status.NOT_FOUND).build();
        }
    }

    return Response.noContent().build();
}
}
