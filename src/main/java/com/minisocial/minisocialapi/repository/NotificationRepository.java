package com.minisocial.minisocialapi.repository;

import java.util.List;

import com.minisocial.minisocialapi.entities.notification.Notification;
import com.minisocial.minisocialapi.entities.User;

import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;

@Stateless
public class NotificationRepository {

    //inject persistence context
    @PersistenceContext
    private EntityManager em;

    @Transactional  //either fully committed or fully rolled back
    public Notification save(Notification notification) {
        if (notification.getId() == null) {
            //if notification is new, persist it (aka : insert)
            em.persist(notification);
            return notification;
        } else {
            //if notification already exists, merge it (aka : update)
            return em.merge(notification);
        }
    }

    public Notification findById(Long id) {
        return em.find(Notification.class, id);
    }

    public List<Notification> findAll() {
        return em.createQuery("SELECT n FROM Notification n", Notification.class).getResultList();
    }

    @Transactional
    public void deleteById(Long id) {
        Notification n = em.find(Notification.class, id);
        if (n != null) em.remove(n);
    }

    public List<Notification> findByRecipient(User recipient) {
        return em.createQuery("SELECT n FROM Notification n WHERE n.recipient = :recipient", Notification.class)
                 .setParameter("recipient", recipient)      //specify the parameter
                 .getResultList();
    }

    public List<Notification> findByRecipientAndIsRead(User recipient, boolean isRead) {
        return em.createQuery("SELECT n FROM Notification n WHERE n.recipient = :recipient AND n.isRead = :isRead", Notification.class)
                 .setParameter("recipient", recipient)
                 .setParameter("isRead", isRead)
                 .getResultList();
    }

    public List<Notification> findByEventType(String eventType) {
        if(!eventType.equals("FRIEND_REQUEST")||
           !eventType.equals("POST_LIKED")||
           !eventType.equals("POST_COMMENTED")||
           !eventType.equals("GROUP_JOINED")||
           !eventType.equals("GROUP_LEFT")) {
            throw new IllegalArgumentException("Invalid event type: " + eventType);
        }
        return em.createQuery("SELECT n FROM Notification n WHERE n.eventType = :eventType", Notification.class)
                 .setParameter("eventType", eventType)
                 .getResultList();
    }
}