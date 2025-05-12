package com.minisocial.minisocialapi.repositories;

import com.minisocial.minisocialapi.entities.Group;

import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

@Stateless
public class GroupRepository {
    @PersistenceContext
    private EntityManager em;

    public Group findById(Long id) {
        return em.find(Group.class, id);
    }
}