package com.minisocial.minisocialapi.repositories;

import com.minisocial.minisocialapi.entities.User;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

import java.util.List;

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

    public User findById(Long id) {
        try {
            return em.createQuery("SELECT u FROM User u LEFT JOIN FETCH u.friends WHERE u.id = :id", User.class)
                    .setParameter("id", id)
                    .getSingleResult();
        } catch (jakarta.persistence.NoResultException e) {
            return null;
        }
    }
    
    public void update(User user) {
        em.merge(user);
    }
    
    public boolean areFriends(User user1, User user2) {
        Long count = em.createQuery(
                "SELECT COUNT(u) FROM User u JOIN u.friends f WHERE u = :user1 AND f = :user2", 
                Long.class)
                .setParameter("user1", user1)
                .setParameter("user2", user2)
                .getSingleResult();
        return count > 0;
    }
    
    @jakarta.transaction.Transactional
    public void addFriendship(Long userId1, Long userId2) {
        // Direct approach using a native query to add bidirectional friendship
        // Using the correct table name from the entity mapping
        String tableName = "user_friends"; // From @JoinTable annotation in User.java
        
        try {
            // Add first direction of friendship
            em.createNativeQuery("INSERT INTO " + tableName + " (user_id, friend_id) VALUES (?1, ?2)")
               .setParameter(1, userId1)
               .setParameter(2, userId2)
               .executeUpdate();
            
            // Add reverse direction of friendship
            em.createNativeQuery("INSERT INTO " + tableName + " (user_id, friend_id) VALUES (?1, ?2)")
               .setParameter(1, userId2)
               .setParameter(2, userId1)
               .executeUpdate();
        } catch (Exception e) {
            // Log and handle potential errors like duplicate entries
            System.out.println("Error adding friendship: " + e.getMessage());
        }
    }
    
    /**
     * Get a count of friends for a specific user
     */
    public int countFriends(Long userId) {
        // Direct count using native query to avoid loading entire collection
        Number count = (Number) em.createNativeQuery(
            "SELECT COUNT(*) FROM user_friends WHERE user_id = ?1")
            .setParameter(1, userId)
            .getSingleResult();
        return count.intValue();
    }
    
    /**
     * Find friends of a user with optional filtering
     */
    public List<User> findFriends(User user, String nameFilter, String emailFilter) {
        // Use a direct native query to avoid issues with JPA entity mapping
        String sql = "SELECT u.* FROM users u JOIN user_friends uf ON u.id = uf.friend_id WHERE uf.user_id = ?1";
        
        // Apply filters if provided
        if (nameFilter != null && !nameFilter.isEmpty()) {
            sql += " AND LOWER(u.name) LIKE LOWER(?2)";
        }
        if (emailFilter != null && !emailFilter.isEmpty()) {
            sql += " AND LOWER(u.email) LIKE LOWER(?" + (nameFilter != null && !nameFilter.isEmpty() ? "3" : "2") + ")";
        }
        
        // Create the query
        jakarta.persistence.Query query = em.createNativeQuery(sql, User.class);
        query.setParameter(1, user.getId());
        
        // Set filter parameters if needed
        int paramIndex = 2;
        if (nameFilter != null && !nameFilter.isEmpty()) {
            query.setParameter(paramIndex++, "%" + nameFilter + "%");
        }
        if (emailFilter != null && !emailFilter.isEmpty()) {
            query.setParameter(paramIndex, "%" + emailFilter + "%");
        }
        
        @SuppressWarnings("unchecked")
        List<User> result = query.getResultList();
        return result;
    }
}
