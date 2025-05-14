package com.minisocial.minisocialapi.services.notification_service_utiles;

import com.minisocial.minisocialapi.dtos.NotificationDTO;
import com.minisocial.minisocialapi.mapper.NotificationMapper;
import com.minisocial.minisocialapi.repositories.NotificationRepository;
import com.minisocial.minisocialapi.entities.notification.Notification;

import jakarta.ejb.ActivationConfigProperty;
import jakarta.ejb.MessageDriven;
import jakarta.inject.Inject;
import jakarta.jms.*;
import jakarta.jms.MessageListener;
import jakarta.jms.ObjectMessage;
import jakarta.jms.JMSException;

@MessageDriven(activationConfig = {
    @ActivationConfigProperty(propertyName = "destinationType",   propertyValue = "jakarta.jms.Queue"),
    @ActivationConfigProperty(propertyName = "destinationLookup", propertyValue = "java:/jms/queue/Notifications"),
    @ActivationConfigProperty(propertyName = "maxSession",        propertyValue = "10"),
    @ActivationConfigProperty(propertyName = "acknowledgeMode",    propertyValue = "Auto-acknowledge")
})

public class NotificationListener implements MessageListener {

    @Inject
    private NotificationMapper notificationMapper;
    @Inject
    private NotificationRepository notificationRepository;


    @Override
    public void onMessage(Message message) {
        if (message instanceof ObjectMessage) {
            try {
                ObjectMessage objectMessage = (ObjectMessage) message;
                if(objectMessage.getObject() instanceof NotificationDTO){
                    NotificationDTO notificationDTO = (NotificationDTO) objectMessage.getObject();

                    //de
                    System.out.println("\n\n\tabdelrahman NotificationDTO: " + notificationDTO.getMessage()+"\n\n");

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
