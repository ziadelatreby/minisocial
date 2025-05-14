package com.minisocial.minisocialapi.repositories;

import com.minisocial.minisocialapi.entities.Group;
import com.minisocial.minisocialapi.entities.User;
import com.minisocial.minisocialapi.entities.UserGroup;
import com.minisocial.minisocialapi.enums.USER_GROUP_ROLE;

import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

import java.util.List;
import java.util.Set;

@Stateless
public class GroupRepository {
    @PersistenceContext
    private EntityManager em;

    public void flushing() {
        em.flush();
    }

    public void save(Group group) {
        em.persist(group);
    }

    public void saveUserGroup(UserGroup userGroup) {
        em.persist(userGroup);
    }
    public Group findById(Long id) {
        return em.find(Group.class, id);
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

    public boolean isUserAdminInGroup(Long userId, Long groupId) {
        String q = "SELECT COUNT(ug) FROM UserGroup ug WHERE ug.user.id = :userId AND ug.group.id = :groupId AND ug.role = :role";
        Long count = em.createQuery(q, Long.class)
                .setParameter("userId", userId)
                .setParameter("groupId", groupId)
                .setParameter("role", USER_GROUP_ROLE.ADMIN)
                .getSingleResult();
        return count > 0;
    }


    public UserGroup findUserGroup(Long groupId, Long userId) {
        String q = "SELECT ug FROM UserGroup ug WHERE ug.user.id = :userId AND ug.group.id = :groupId";
        return em.createQuery(q, UserGroup.class)
                .setParameter("userId", userId)
                .setParameter("groupId", groupId)
                .getResultStream()
                .findFirst()
                .orElse(null);
    }


    public void updateUserGroup(UserGroup userGroup) {
        em.merge(userGroup);
    }

    public List<User> getJoinRequestsForGroup(Long groupId) {
        return em.createQuery("SELECT u FROM User u JOIN u.requestedGroups g WHERE g.id = :groupId", User.class)
                .setParameter("groupId", groupId)
                .getResultList();
    }

    public void deleteUserGroup(UserGroup userGroup) {
        em.remove(em.contains(userGroup) ? userGroup : em.merge(userGroup));
    }

}
