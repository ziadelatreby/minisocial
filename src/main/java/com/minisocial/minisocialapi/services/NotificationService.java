package com.minisocial.minisocialapi.service;

import com.minisocial.minisocialapi.dto.NotificationDTO;
import com.minisocial.minisocialapi.entities.notification.Notification;
import com.minisocial.minisocialapi.mapper.NotificationMapper;
import com.minisocial.minisocialapi.service.notification_service_utiles.MessageStringCreator;
import com.minisocial.minisocialapi.service.notification_service_utiles.NotificationSender;

import jakarta.inject.Inject;
import jakarta.ejb.Stateless;

@Stateless
public class NotificationService {

    MessageStringCreator messageStringCreator;

    @Inject
    NotificationSender notificationSender;

    @Inject
    NotificationMapper notificationMapper;

    public void sendNotification(Notification notification) {

        try {
            //get message
            notification.setMessage( MessageStringCreator.createMessage(notification));

            //get notificationDTO
            NotificationDTO notificationDTO = notificationMapper.toDTO(notification);

            //send notification
            notificationSender.send(notificationDTO);
        } catch (Exception e) {
            System.err.println("Failed to send notification: " + e.getMessage());
            e.printStackTrace();
        }
        
    }
}
