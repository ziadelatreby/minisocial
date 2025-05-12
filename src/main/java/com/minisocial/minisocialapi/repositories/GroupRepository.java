package com.minisocial.minisocialapi.repositories;

import com.minisocial.minisocialapi.entities.Group;
import com.minisocial.minisocialapi.entities.UserGroup;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

@Stateless
public class GroupRepository {
    @PersistenceContext
    private EntityManager em;

    public void save(Group group) {
        em.persist(group);
    }

    public void saveUserGroup(UserGroup userGroup) {
        em.persist(userGroup);
    }

    public Group findByName(String name) {
        return em.createQuery("SELECT g FROM Group g WHERE g.name = :name", Group.class)
                .setParameter("name", name)
                .getResultStream()
                .findFirst()
                .orElse(null);
    }

    public boolean isUserInGroup(Long userId, Long groupId) {
        String jpql = "SELECT COUNT(ug) FROM UserGroup ug WHERE ug.user.id = :userId AND ug.group.id = :groupId";
        Long count = em.createQuery(jpql, Long.class)
                .setParameter("userId", userId)
                .setParameter("groupId", groupId)
                .getSingleResult();
        return count > 0;
    }

    public Group findById(Long id) {
        return em.find(Group.class, id);
    }


}
