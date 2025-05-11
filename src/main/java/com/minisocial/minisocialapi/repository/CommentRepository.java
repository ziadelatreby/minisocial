package com.minisocial.minisocialapi.repository;

import com.minisocial.minisocialapi.entities.Comment;

import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

@Stateless
public class CommentRepository {
    @PersistenceContext
    private EntityManager em;

    public Comment findById(Long id) {
        return em.find(Comment.class, id);
    }
}