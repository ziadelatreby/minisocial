package com.minisocial.minisocialapi.repositories;

import java.util.List;

import com.minisocial.minisocialapi.entities.Post;

import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;

@Stateless
public class PostRepository {
    @PersistenceContext
    private EntityManager em;

    public Post findById(Long id) {
        return em.find(Post.class, id);
    }

    @Transactional
    public Post save(Post post) {
        if(post.getId() == null){
            em.persist(post);
        }else{
            em.merge(post);
        }
        return post;
    }

    public List<Post> findByUserId(Long userId){
        return em.createQuery("SELECT p FROM Post p WHERE p.user.id = :userId", Post.class)
        .setParameter("userId", userId)
        .getResultList();
    }

    public List<Post> findByGroupId(Long groupId){
        return em.createQuery("SELECT p FROM Post p WHERE p.group.id = :groupId", Post.class)
        .setParameter("groupId", groupId)
        .getResultList();
    }
}