package com.minisocial.minisocialapi.repositories;

import com.minisocial.minisocialapi.entities.Post;
import com.minisocial.minisocialapi.entities.User;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;

import java.util.List;

@Stateless
public class PostRepository {
    
    @PersistenceContext(unitName = "minisocialPU")
    private EntityManager em;
    
    public Post findById(Long id) {
        return em.find(Post.class, id);
    }
    
    public Post save(Post post) {
        if (post.getId() == null) {
            em.persist(post);
            return post;
        } else {
            return em.merge(post);
        }
    }
    
    public void delete(Post post) {
        if (em.contains(post)) {
            em.remove(post);
        } else {
            em.remove(em.merge(post));
        }
    }
    
    public List<Post> getUserFeed(User user) {
        TypedQuery<Post> query = em.createQuery(
            "SELECT p FROM Post p " +
            "WHERE p.user = :user OR p.user IN " +
            "(SELECT f FROM User u JOIN u.friends f WHERE u = :user) " +
            "ORDER BY p.createdAt DESC", Post.class);
        query.setParameter("user", user);
        return query.getResultList();
    }
    
    public boolean isPostLikedByUser(Post post, User user) {
        TypedQuery<Long> query = em.createQuery(
            "SELECT COUNT(u) FROM Post p JOIN p.likes u " +
            "WHERE p = :post AND u = :user", Long.class);
        query.setParameter("post", post);
        query.setParameter("user", user);
        return query.getSingleResult() > 0;
    }
}
