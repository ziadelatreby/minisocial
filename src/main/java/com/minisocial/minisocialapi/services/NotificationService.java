package com.minisocial.minisocialapi.services;

import java.util.List;

import com.minisocial.minisocialapi.dtos.NotificationDTO;
import com.minisocial.minisocialapi.entities.User;
import com.minisocial.minisocialapi.entities.notification.Notification;
import com.minisocial.minisocialapi.errors.NotFoundException;
import com.minisocial.minisocialapi.mapper.NotificationMapper;
import com.minisocial.minisocialapi.services.notification_service_utiles.MessageStringCreator;
import com.minisocial.minisocialapi.services.notification_service_utiles.NotificationSender;
import com.minisocial.minisocialapi.repositories.NotificationRepository;
import com.minisocial.minisocialapi.repositories.UserRepository;

import jakarta.inject.Inject;
import jakarta.ejb.Stateless;

@Stateless
public class NotificationService {

    MessageStringCreator messageStringCreator;

    @Inject
    NotificationSender notificationSender;

    @Inject
    NotificationMapper notificationMapper;

    @Inject
    NotificationRepository notificationRepository;

    @Inject
    UserRepository userRepository;

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

    public void readNotification(Long notificationId){
        Notification notification = notificationRepository.findById(notificationId);
        notification.setIsRead(true);
        notificationRepository.update(notification);
    }

    public void readAllNotifications(Long userId){
        User user = userRepository.findById(userId);
        if(user == null){
            throw new NotFoundException("User not found");
        }
        List<Notification> notifications = notificationRepository.findByRecipient(user);
        for(Notification notification : notifications){
            notification.setIsRead(true);
            notificationRepository.update(notification);
        }
    }
}
