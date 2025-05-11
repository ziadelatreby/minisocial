package com.minisocial.minisocialapi.repository;

import com.minisocial.minisocialapi.entities.GroupJoinRequest;

import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

@Stateless
public class GroupJoinRequestRepository {
    @PersistenceContext
    private EntityManager em;

    public GroupJoinRequest findById(Long id) {
        return em.find(GroupJoinRequest.class, id);
    }
}
