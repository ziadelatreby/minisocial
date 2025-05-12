package com.minisocial.minisocialapi.service.notification_service_utiles;

import jakarta.annotation.Resource;
import jakarta.ejb.Stateless;
import jakarta.jms.ConnectionFactory;
import jakarta.jms.DeliveryMode;
import jakarta.jms.JMSContext;
import jakarta.jms.ObjectMessage;
import jakarta.jms.Queue;


import com.minisocial.minisocialapi.dto.NotificationDTO;

@Stateless
public class NotificationSender {
    @Resource(lookup = "java:/JmsXA")
    private ConnectionFactory connectionFactory;

    @Resource(lookup = "java:/jms/queue/Notifications")
    private Queue notificationsQueue;

    public void send(NotificationDTO evt) {
        try (JMSContext ctx = connectionFactory.createContext(JMSContext.AUTO_ACKNOWLEDGE)) {

            ObjectMessage msg = ctx.createObjectMessage(evt);

            ctx.createProducer()
               .setDeliveryMode(DeliveryMode.PERSISTENT)    //to tell jms to persist the message (aka: make sure got delivered)
               .send(notificationsQueue, msg);
        }
    }
}