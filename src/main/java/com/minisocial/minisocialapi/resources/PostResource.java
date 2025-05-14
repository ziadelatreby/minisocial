package com.minisocial.minisocialapi.resources;

import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;


import com.minisocial.minisocialapi.dtos.PostDTO;
import com.minisocial.minisocialapi.entities.Post;
import com.minisocial.minisocialapi.entities.User;
import com.minisocial.minisocialapi.enums.GROUP_TYPE;
import com.minisocial.minisocialapi.entities.Group;
import com.minisocial.minisocialapi.errors.NotFoundException;
import com.minisocial.minisocialapi.mapper.PostMapper;
import com.minisocial.minisocialapi.repositories.GroupRepository;
import com.minisocial.minisocialapi.repositories.PostRepository;
import com.minisocial.minisocialapi.repositories.UserRepository;
import com.minisocial.minisocialapi.services.PostService;
import com.minisocial.minisocialapi.services.CommentService;
import com.minisocial.minisocialapi.dtos.CommentDTO;
import com.minisocial.minisocialapi.entities.Comment;
import com.minisocial.minisocialapi.mapper.CommentMapper;

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
@Inject
private GroupRepository groupRepository;
@Inject
private CommentService commentService;
@Inject
private CommentMapper commentMapper;


private final String ctxUserIdAttributeName = "ctxUserId";

//creating post
@POST
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public Response createPost(@Context HttpServletRequest ctx, PostDTO postDTO){
    long userId = Long.parseLong((String) ctx.getAttribute(ctxUserIdAttributeName));

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


//EDITING POST (update , delete)
@PATCH
@Path("/{postId}")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public Response editPost(@PathParam("postId") Long postId,
                         @Context HttpServletRequest ctx,
                         PostDTO postDTO){

    long userId = Long.parseLong((String) ctx.getAttribute(ctxUserIdAttributeName));

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
    long userId = Long.parseLong((String) ctx.getAttribute(ctxUserIdAttributeName));

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

//FEED POSTS
//get posts of a group
@GET
@Path("/{groupId}/groups")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public Response getGroupPosts(@PathParam("groupId") Long groupId, @Context HttpServletRequest ctx){
    long userId = Long.parseLong((String) ctx.getAttribute(ctxUserIdAttributeName));

    if(userRepository.findById(userId) == null){
        return Response.status(Response.Status.UNAUTHORIZED).build();
    }

    Group group = groupRepository.findById(groupId);
    if(group == null){
        return Response.status(Response.Status.NOT_FOUND).build();
    }

    //check if closed group and user is not in group
    if(group.getType() == GROUP_TYPE.CLOSED && !groupRepository.isUserInGroup(userId, groupId))
            return Response.status(Response.Status.FORBIDDEN).build();

    List<Post> posts = postRepository.findByGroupId(groupId);

    List<PostDTO> postDTOs = posts.stream().map(postMapper::toDTO).collect(Collectors.toList());
    
    return Response.ok(postDTOs).build();
    
}
//get posts of a user
@GET
public Response getPosts(@Context HttpServletRequest ctx){
    long userId = Long.parseLong((String) ctx.getAttribute(ctxUserIdAttributeName));

    if(userRepository.findById(userId) == null){
        return Response.status(Response.Status.UNAUTHORIZED).build();
    }

    List<Post> posts = postRepository.findByUserId(userId);
    return Response.ok(posts.stream().map(postMapper::toDTO).collect(Collectors.toList())).build();

}
//get posts of a user's friends
@GET
@Path("/{friendId}/friends")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public Response getFriendsPosts(@PathParam("friendId") Long friendId, @Context HttpServletRequest ctx){

    //check user
    long userId = Long.parseLong((String) ctx.getAttribute(ctxUserIdAttributeName));
    User user = userRepository.findById(userId);
    if( userRepository.findById(userId) == null){
        return Response.status(Response.Status.UNAUTHORIZED).entity("User not found").build();
    }
    //check friend
    User friend = userRepository.findById(friendId);
    if(friend == null){
        return Response.status(Response.Status.NOT_FOUND).entity("Friend not found").build();
    }

    //check are they friends
    if(!userRepository.areFriends(user,friend)){
        return Response.status(Response.Status.FORBIDDEN).entity("User and friend are not friends").build();
    }

    //get posts of the friend
        //check if the friend and user are in the same group of a post
        //return the posts that are in the same group

    List<Post> posts = postService.getPostsOfFriends(user,friend);
    List<PostDTO> postDTOs = posts.stream().map(postMapper::toDTO).collect(Collectors.toList());
    return Response.status(Response.Status.OK).entity(postDTOs).build();
}


//get comments of a post
@GET
@Path("/{postId}/comments")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public Response getComments(@PathParam("postId") Long postId, @Context HttpServletRequest ctx){
    long userId = Long.parseLong((String) ctx.getAttribute(ctxUserIdAttributeName));

    //check user
    User user = userRepository.findById(userId);
    if(user == null){
        return Response.status(Response.Status.UNAUTHORIZED).build();
    }
    //check post
    Post post = postRepository.findById(postId);
    if(post == null){
        return Response.status(Response.Status.NOT_FOUND).entity("Post not found").build();
    }

    //check post's group and if user is in that group
    Group postGroup = post.getGroup();
    if(post.getGroup() != null && !groupRepository.isUserInGroup(userId,postGroup.getId() )){
        return Response.status(Response.Status.FORBIDDEN).entity("User is not in the group "+postGroup.getName()).build();
    }


    List<Comment> comments = commentService.getCommentsByPostId(postId);
    List<CommentDTO> commentDTOs = comments.stream().map(commentMapper::toDTO).collect(Collectors.toList());

    return Response.ok(commentDTOs).entity(commentDTOs).build();
}


}
