package com.minisocial.minisocialapi.resources;

import com.minisocial.minisocialapi.services.NotificationService;
import com.minisocial.minisocialapi.repositories.NotificationRepository;
import com.minisocial.minisocialapi.repositories.UserRepository;
import com.minisocial.minisocialapi.entities.notification.Notification;
import com.minisocial.minisocialapi.entities.User;

import jakarta.annotation.security.RolesAllowed;
import jakarta.inject.Inject;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.SecurityContext;

import java.util.List;
import java.util.Set;
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


    // @Context
    // private SecurityContext securityContext;

    private final String ctxUserIdAttributeName = "ctxUserId";

    @GET
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response getNotification( @Context HttpServletRequest ctx) {

        //get user id from security context
        Long userId = (Long) ctx.getAttribute(ctxUserIdAttributeName);

        User user = userRepository.findById(userId);

        List<Notification> notifications = notificationRepository.findByRecipient(user);
        List<String> notificationMessages = getNotificationMessages(notifications);

        //return notifications
        return  Response.status(Response.Status.OK)
                .entity(notificationMessages)
                .type(MediaType.APPLICATION_JSON)
                .build();
    }
    private List<String> getNotificationMessages(List<Notification> notifications) {
        return notifications.stream().map(Notification::getMessage).collect(Collectors.toList());
    }

}

