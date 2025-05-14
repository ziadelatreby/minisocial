package com.minisocial.minisocialapi.repositories;

import com.minisocial.minisocialapi.entities.Comment;

import java.util.List;
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

    public List<Comment> findByPostId(Long postId) {
        return em.createQuery("SELECT c FROM Comment c WHERE c.post.id = :postId", Comment.class)
                .setParameter("postId", postId)
                .getResultList();
    }

    public void saveComment(Comment comment) {
            em.persist(comment);
        }
}