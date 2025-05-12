package com.minisocial.minisocialapi.repositories;

import com.minisocial.minisocialapi.entities.Comment;
import com.minisocial.minisocialapi.entities.Post;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;

import java.util.List;

@Stateless
public class CommentRepository {
    
    @PersistenceContext(unitName = "minisocialPU")
    private EntityManager em;
    
    public Comment findById(Long id) {
        return em.find(Comment.class, id);
    }
    
    public Comment save(Comment comment) {
        if (comment.getId() == null) {
            em.persist(comment);
            return comment;
        } else {
            return em.merge(comment);
        }
    }
    
    public void delete(Comment comment) {
        if (em.contains(comment)) {
            em.remove(comment);
        } else {
            em.remove(em.merge(comment));
        }
    }
    
    public List<Comment> getPostComments(Post post) {
        TypedQuery<Comment> query = em.createQuery(
            "SELECT c FROM Comment c WHERE c.post = :post ORDER BY c.createdAt DESC", Comment.class);
        query.setParameter("post", post);
        return query.getResultList();
    }
}
