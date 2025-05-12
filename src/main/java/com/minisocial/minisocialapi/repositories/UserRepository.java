package com.minisocial.minisocialapi.repositories;

import com.minisocial.minisocialapi.entities.User;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

@Stateless
public class UserRepository {
    @PersistenceContext
    private EntityManager em;

    public User findByEmail(String email) {
        return em.createQuery("SELECT u FROM User u WHERE u.email = :email", User.class)
                .setParameter("email", email)
                .getResultStream()
                .findFirst()
                .orElse(null);
    }

    public void save(User user) {
        em.persist(user);
    }
}
