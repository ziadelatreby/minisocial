package com.minisocial.minisocialapi.repositories;

import com.minisocial.minisocialapi.entities.FriendRequest;
import com.minisocial.minisocialapi.entities.User;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.PersistenceContext;

import java.util.List;

@Stateless
public class FriendRequestRepository {
    @PersistenceContext
    private EntityManager em;

    public void save(FriendRequest friendRequest) {
        em.persist(friendRequest);
    }

    public void update(FriendRequest friendRequest) {
        em.merge(friendRequest);
    }

    public FriendRequest findById(Long id) {
        return em.find(FriendRequest.class, id);
    }

    public FriendRequest findBySenderAndReceiver(User sender, User receiver) {
        try {
            return em.createQuery(
                    "SELECT fr FROM FriendRequest fr WHERE fr.sender = :sender AND fr.receiver = :receiver", 
                    FriendRequest.class)
                    .setParameter("sender", sender)
                    .setParameter("receiver", receiver)
                    .getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }

    public List<FriendRequest> findPendingRequestsByReceiver(User receiver) {
        return em.createQuery(
                "SELECT fr FROM FriendRequest fr JOIN FETCH fr.sender WHERE fr.receiver = :receiver AND fr.status = :status", 
                FriendRequest.class)
                .setParameter("receiver", receiver)
                .setParameter("status", "pending")
                .getResultList();
    }

    public List<FriendRequest> findPendingRequestsBySender(User sender) {
        return em.createQuery(
                "SELECT fr FROM FriendRequest fr WHERE fr.sender = :sender AND fr.status = :status", 
                FriendRequest.class)
                .setParameter("sender", sender)
                .setParameter("status", "pending")
                .getResultList();
    }
}
