package com.minisocial.minisocialapi.service.notification_service_utiles;

import com.minisocial.minisocialapi.dto.NotificationDTO;
import com.minisocial.minisocialapi.mapper.NotificationMapper;
import com.minisocial.minisocialapi.repository.NotificationRepository;
import com.minisocial.minisocialapi.entities.notification.Notification;

import jakarta.ejb.ActivationConfigProperty;
import jakarta.ejb.MessageDriven;
import jakarta.inject.Inject;
import jakarta.jms.*;
import jakarta.jms.MessageListener;
import jakarta.jms.ObjectMessage;
import jakarta.jms.JMSException;

@MessageDriven(activationConfig = {
    @ActivationConfigProperty(propertyName = "destinationType",   propertyValue = "javax.jms.Queue"),
    @ActivationConfigProperty(propertyName = "destinationLookup", propertyValue = "java:/jms/queue/Notifications"),
    @ActivationConfigProperty(propertyName = "maxSession",        propertyValue = "10"),
    @ActivationConfigProperty(propertyName = "acknowledgeMode",    propertyValue = "Auto-acknowledge")
})

public class NotificationListener implements MessageListener {

    private NotificationMapper notificationMapper;
    private NotificationRepository notificationRepository;

    @Inject
    public NotificationListener(NotificationMapper notificationMapper, NotificationRepository notificationRepository) {
        this.notificationMapper = notificationMapper;
        this.notificationRepository = notificationRepository;
    }

    @Override
    public void onMessage(Message message) {
        if (message instanceof ObjectMessage) {
            try {
                ObjectMessage objectMessage = (ObjectMessage) message;
                if(objectMessage.getObject() instanceof NotificationDTO){
                    NotificationDTO notificationDTO = (NotificationDTO) objectMessage.getObject();
                    Notification notification = notificationMapper.toEntity(notificationDTO);
                    notificationRepository.save(notification);
                }
                else{
                    //logger.error("Received message is not of type NotificationDTO");
                }
                


            } catch (JMSException e) {
                throw new RuntimeException("Failed to process message", e);
            }
        } else {
            throw new IllegalArgumentException("Unexpected message type: " + message.getClass());
        }
    }
}
