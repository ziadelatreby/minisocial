package com.minisocial.minisocialapi.rest;

import com.minisocial.minisocialapi.service.NotificationService;
import com.minisocial.minisocialapi.repository.UserRepository;
import com.minisocial.minisocialapi.entities.notification.Notification;
import com.minisocial.minisocialapi.entities.User;

import jakarta.annotation.security.RolesAllowed;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.SecurityContext;
import java.util.Set;

@Path("/notifications")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class NotificationResource {

    @Inject
    private NotificationService notificationService;

    @Inject
    private UserRepository userRepository;


    @Context
    private SecurityContext securityContext;

    @GET
    @RolesAllowed("USER")
    public Set<Notification> getNotification() {

        Set<Notification> notifications = null;
        //get user id from security context
        long userId = Long.parseLong(securityContext.getUserPrincipal().getName());

        //get user via user repo
        User user = userRepository.findById(userId);

        //get notificaitons set from user
        notifications = user.getNotifications();

        //return notifications
        return notifications;
    }

}

