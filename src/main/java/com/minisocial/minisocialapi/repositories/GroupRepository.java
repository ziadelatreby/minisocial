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

    public Group findByName(String name) {
        return em.createQuery("SELECT g FROM Group g WHERE g.name = :name", Group.class)
                .setParameter("name", name)
                .getSingleResult();
    }

    //check if user is member of a group
    public boolean isUserMemberOfGroup(Long userId, Long groupId) {
        return (em.createQuery("SELECT COUNT(m) FROM GroupMember m WHERE m.user.id = :userId AND m.group.id = :groupId", Long.class)
                .setParameter("userId", userId)
                .setParameter("groupId", groupId)
                .getSingleResult()) > 0;
    }
}
