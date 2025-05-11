package com.minisocial.minisocialapi.repository;

import com.minisocial.minisocialapi.entities.FriendRequest;

import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

@Stateless
public class FriendRequestRepository {
    @PersistenceContext
    private EntityManager em;

    public FriendRequest findById(Long id) {
        return em.find(FriendRequest.class, id);
    }
}