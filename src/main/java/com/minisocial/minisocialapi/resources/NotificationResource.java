package com.minisocial.minisocialapi.resources;

import com.minisocial.minisocialapi.services.NotificationService;
import com.minisocial.minisocialapi.repositories.NotificationRepository;
import com.minisocial.minisocialapi.repositories.UserRepository;
import com.minisocial.minisocialapi.entities.notification.Notification;
import com.minisocial.minisocialapi.entities.User;
import com.minisocial.minisocialapi.dtos.NotificationDTO;
import com.minisocial.minisocialapi.mapper.NotificationMapper;


import jakarta.inject.Inject;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.SecurityContext;

import java.util.List;
import java.util.stream.Collectors;

@Path("/notifications")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class NotificationResource {

    @Inject
    private NotificationService notificationService;

    @Inject
    private UserRepository userRepository;

    @Inject
    private NotificationRepository notificationRepository;

    @Inject
    private NotificationMapper notificationMapper;

    // @Context
    // private SecurityContext securityContext;

    private final String ctxUserIdAttributeName = "ctxUserId";

    @GET
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response getNotification( @Context HttpServletRequest ctx) {

        //get user id from security context
        Long userId = Long.parseLong((String) ctx.getAttribute(ctxUserIdAttributeName));

        User user = userRepository.findById(userId);

        if(user == null){
            return Response.status(Response.Status.NOT_FOUND).build();
        }



        List<Notification> notifications = notificationRepository.findByRecipient(user);
        List<NotificationDTO> notificationDTOs = notifications.stream().map(notificationMapper::toDTO).collect(Collectors.toList());

        notificationService.readAllNotifications(userId);

        //return notifications
        return  Response.status(Response.Status.OK)
                .entity(notificationDTOs)
                .type(MediaType.APPLICATION_JSON)
                .build();
    }

    @GET
    @Path("/unread")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getUnreadNotifications(@Context HttpServletRequest ctx) {
        Long userId = Long.parseLong((String) ctx.getAttribute(ctxUserIdAttributeName));

        User user = userRepository.findById(userId);
        if(user == null){
            return Response.status(Response.Status.NOT_FOUND).build();
        }

        List<Notification> notifications = notificationRepository.findByRecipientAndIsRead(user, false);

        for(Notification unreadNoti: notifications){
            notificationService.readNotification(unreadNoti.getId());
        }

        return Response.status(Response.Status.OK).entity(notifications).type(MediaType.APPLICATION_JSON).build();
    }
}

