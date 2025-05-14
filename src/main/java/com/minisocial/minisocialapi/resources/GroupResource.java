package com.minisocial.minisocialapi.resources;

import com.minisocial.minisocialapi.dtos.UserDTO;
import com.minisocial.minisocialapi.entities.Group;
import com.minisocial.minisocialapi.entities.User;
import com.minisocial.minisocialapi.enums.GROUP_TYPE;
import com.minisocial.minisocialapi.services.GroupService;
import jakarta.inject.Inject;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Path( "/groups")
public class GroupResource {
    @Inject
    private GroupService groupService;

    private final String ctxUserIdAttributeName = "ctxUserId";


    // @POST
    // @Path("/")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response createGroup(Group group, @Context HttpServletRequest ctx) {
        Long ctxUserId = Long.parseLong((String) ctx.getAttribute(ctxUserIdAttributeName));
        groupService.createGroup(group, ctxUserId);
        return Response.status(Response.Status.CREATED).build();
    }

    @POST
    @Path("/{id}/join")
    @Produces(MediaType.APPLICATION_JSON)
    public Response joinGroup(@PathParam("id") Long groupId, @Context HttpServletRequest ctx) {
        Long ctxUserId = Long.parseLong((String) ctx.getAttribute(ctxUserIdAttributeName));
        GROUP_TYPE type = groupService.joinGroup(groupId, ctxUserId);
        String message;
        if (type == GROUP_TYPE.OPEN) {
            message = "Member added to the group.";
        } else {
            message = "The request has been sent.";
        }
        Map<String, String> res = new HashMap<>();
        res.put("message", message);

        return Response.status(Response.Status.CREATED).entity(res).build();
    }

    @PUT
    @Path("/{groupId}/requests/{userId}/accept")
    @Produces(MediaType.APPLICATION_JSON)
    public Response acceptJoinRequest(@PathParam("groupId") Long groupId, @PathParam("userId") Long targetUserId, @Context HttpServletRequest ctx) {
        Long ctxUserId = Long.parseLong((String) ctx.getAttribute(ctxUserIdAttributeName));
        try {
            groupService.acceptJoinRequest(groupId, targetUserId, ctxUserId);
        } catch (BadRequestException e) {
            return Response.status(Response.Status.BAD_REQUEST).entity(e.getMessage()).build();
        } catch (NotFoundException e) {
            return Response.status(Response.Status.NOT_FOUND).entity(e.getMessage()).build();
        } catch (NotAuthorizedException e) {
            return Response.status(Response.Status.FORBIDDEN).entity(e.getMessage()).build();
        }

        return Response.ok().build();
    }

    @PUT
    @Path("/{groupId}/requests/{userId}/reject")
    @Produces(MediaType.APPLICATION_JSON)
    public Response rejectJoinRequest(@PathParam("groupId") Long groupId, @PathParam("userId") Long targetUserId, @Context HttpServletRequest ctx) {
        Long ctxUserId = Long.parseLong((String) ctx.getAttribute(ctxUserIdAttributeName));
        groupService.rejectJoinRequest(groupId, targetUserId, ctxUserId);
        return Response.ok().build();
    }


    @DELETE
    @Path("/{groupId}/members/{targetUserId}")
    public Response kickMember(@PathParam("groupId") Long groupId, @PathParam("targetUserId") Long targetUserId, @Context HttpServletRequest ctx) {
        Long ctxUserId = Long.parseLong((String) ctx.getAttribute(ctxUserIdAttributeName));
        groupService.kickMember(groupId, targetUserId, ctxUserId);
        return Response.noContent().build();
    }

    @PUT
    @Path("/{groupId}/members/{targetUserId}/promote")
    public Response promoteMember(@PathParam("groupId") Long groupId, @PathParam("targetUserId") Long targetUserId, @Context HttpServletRequest ctx) {
        Long ctxUserId = Long.parseLong((String) ctx.getAttribute(ctxUserIdAttributeName));
        groupService.promoteMemberToAdmin(groupId, targetUserId, ctxUserId);
        return Response.ok().build();
    }

    @GET
    @Path("/{groupId}/requests")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getJoinRequests(@PathParam("groupId") Long groupId, @Context HttpServletRequest ctx) {
        Long ctxUserId = Long.parseLong((String) ctx.getAttribute(ctxUserIdAttributeName));
        List<UserDTO> res = groupService.getGroupJoinRequests(groupId, ctxUserId);
        return Response.status(Response.Status.OK).entity(res).type(MediaType.APPLICATION_JSON).build();
    }
}
