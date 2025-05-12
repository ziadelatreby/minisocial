package com.minisocial.minisocialapi.resources;

import com.minisocial.minisocialapi.dtos.FriendRequestActionDTO;
import com.minisocial.minisocialapi.dtos.FriendRequestDTO;
import com.minisocial.minisocialapi.dtos.FriendRequestResponseDTO;
import com.minisocial.minisocialapi.dtos.UserProfileDTO;
import com.minisocial.minisocialapi.entities.FriendRequest;
import com.minisocial.minisocialapi.entities.User;
import com.minisocial.minisocialapi.repositories.UserRepository;
import com.minisocial.minisocialapi.services.FriendRequestService;
import jakarta.inject.Inject;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.util.List;
import java.util.stream.Collectors;

@Path("/connections")
public class ConnectionResource {

    @Inject
    private FriendRequestService friendRequestService;
    
    @Inject
    private UserRepository userRepository;

    @POST
    @Path("/friend-requests")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response sendFriendRequest(FriendRequestDTO friendRequestDTO, @Context HttpServletRequest request) {
        String userIdStr = request.getAttribute("ctxUserId").toString();
        Long userId = Long.parseLong(userIdStr);
        
        friendRequestService.sendFriendRequest(friendRequestDTO, userId);
        
        return Response.status(Response.Status.CREATED)
                .entity("Friend request sent successfully")
                .build();
    }

    @PATCH
    @Path("/friend-requests/{from-user-id}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response handleFriendRequest(
            @PathParam("from-user-id") Long fromUserId,
            FriendRequestActionDTO actionDTO,
            @Context HttpServletRequest request) {
        
        // Get authenticated user ID from request context
        String userIdStr = request.getAttribute("ctxUserId").toString();
        Long userId = Long.parseLong(userIdStr);
        
        friendRequestService.handleFriendRequest(fromUserId, actionDTO, userId);
        
        String action = actionDTO.getAction().toLowerCase();
        String message = "Friend request " + (action.equals("accept") ? "accepted" : "rejected");
        
        return Response.status(Response.Status.OK)
                .entity(message)
                .build();
    }

    @GET
    @Path("/user/{user-id}/friends")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getUserFriends(
            @PathParam("user-id") Long userId,
            @QueryParam("name") String nameFilter,
            @QueryParam("email") String emailFilter,
            @Context HttpServletRequest request) {
        
        // We don't need to check authentication for viewing friends list,
        // as it's public information in this implementation
        
        List<User> friends = friendRequestService.getUserFriends(userId, nameFilter, emailFilter);
        
        // Convert to DTOs to avoid lazy loading issues, with explicit friend count
        List<UserProfileDTO> profileDTOs = friends.stream()
                .map(friend -> new UserProfileDTO(friend, userRepository.countFriends(friend.getId())))
                .collect(Collectors.toList());
        
        return Response.status(Response.Status.OK)
                .entity(profileDTOs)
                .build();
    }

    @GET
    @Path("/user/{user-id}/friend-requests")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getUserPendingFriendRequests(
            @PathParam("user-id") Long userId,
            @Context HttpServletRequest request) {
        
        // Get authenticated user ID from request context
        String userIdStr = request.getAttribute("ctxUserId").toString();
        Long authenticatedUserId = Long.parseLong(userIdStr);
        
        // Check if the user is requesting their own friend requests
        if (!userId.equals(authenticatedUserId)) {
            return Response.status(Response.Status.FORBIDDEN)
                    .entity("You can only view your own friend requests")
                    .build();
        }
        
        List<FriendRequest> pendingRequests = friendRequestService.getPendingFriendRequests(userId);
        
        // Convert to DTOs to avoid circular references and provide better formatting
        List<FriendRequestResponseDTO> responseList = pendingRequests.stream()
                .map(FriendRequestResponseDTO::new)
                .collect(Collectors.toList());
        
        return Response.status(Response.Status.OK)
                .entity(responseList)
                .build();
    }

    @GET
    @Path("/user/{user-id}/profile")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getUserProfile(
            @PathParam("user-id") Long userId,
            @Context HttpServletRequest request) {
        
        // No authentication check needed for viewing public profiles
        
        // Fetch the user from repository
        User user = userRepository.findById(userId);
        if (user == null) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity("User not found")
                    .build();
        }
        
        // Create and return the user profile DTO with explicit friend count
        UserProfileDTO profileDTO = new UserProfileDTO(user, userRepository.countFriends(user.getId()));
        
        return Response.status(Response.Status.OK)
                .entity(profileDTO)
                .build();
    }
}
