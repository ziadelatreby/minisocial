package com.minisocial.minisocialapi.repositories;

import com.minisocial.minisocialapi.entities.Post;

import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

@Stateless
public class PostRepository {
    @PersistenceContext
    private EntityManager em;

    public Post findById(Long id) {
        return em.find(Post.class, id);
    }
}