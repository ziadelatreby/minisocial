package com.minisocial.minisocialapi.services;

import com.minisocial.minisocialapi.entities.Post;
import com.minisocial.minisocialapi.entities.User;
import com.minisocial.minisocialapi.errors.ForbiddenException;
import com.minisocial.minisocialapi.errors.NotFoundException;
import com.minisocial.minisocialapi.repositories.PostRepository;
import com.minisocial.minisocialapi.repositories.UserRepository;
import jakarta.ejb.Stateless;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;

@Stateless
public class PostService {
    
    @Inject
    private PostRepository postRepository;
    
    @Inject
    private UserRepository userRepository;
    
    public Post findById(Long id) {
        Post post = postRepository.findById(id);
        if (post == null) {
            throw new NotFoundException("Post not found with id: " + id);
        }
        return post;
    }
    
    @Transactional
    public Post createPost(String content, String imageUrl, Long userId) {
        User user = userRepository.findById(userId);
        if (user == null) {
            throw new NotFoundException("User not found with id: " + userId);
        }
        
        Post post = new Post();
        post.setContent(content);
        post.setImageUrl(imageUrl);
        post.setUser(user);
        post.setCreatedAt(LocalDateTime.now());
        post.setLikes(new HashSet<>());
        post.setComments(new HashSet<>());
        
        return postRepository.save(post);
    }
    
    @Transactional
    public Post updatePost(Long postId, String content, String imageUrl, Long userId) {
        Post post = findById(postId);
        User user = userRepository.findById(userId);
        
        if (user == null) {
            throw new NotFoundException("User not found with id: " + userId);
        }
        
        if (!post.getUser().getId().equals(userId) && 
            !user.getRole().toString().equals("admin")) {
            throw new ForbiddenException("You don't have permission to update this post");
        }
        
        post.setContent(content);
        post.setImageUrl(imageUrl);
        post.setUpdatedAt(LocalDateTime.now());
        
        return postRepository.save(post);
    }
    
    @Transactional
    public void deletePost(Long postId, Long userId) {
        Post post = findById(postId);
        User user = userRepository.findById(userId);
        
        if (user == null) {
            throw new NotFoundException("User not found with id: " + userId);
        }
        
        if (!post.getUser().getId().equals(userId) && 
            !user.getRole().toString().equals("admin")) {
            throw new ForbiddenException("You don't have permission to delete this post");
        }
        
        postRepository.delete(post);
    }
    
    @Transactional
    public Post likePost(Long postId, Long userId) {
        Post post = findById(postId);
        User user = userRepository.findById(userId);
        
        if (user == null) {
            throw new NotFoundException("User not found with id: " + userId);
        }
        
        if (postRepository.isPostLikedByUser(post, user)) {
            post.getLikes().remove(user);
        } else {
            post.getLikes().add(user);
        }
        
        return postRepository.save(post);
    }
    
    public List<Post> getUserFeed(Long userId) {
        User user = userRepository.findById(userId);
        
        if (user == null) {
            throw new NotFoundException("User not found with id: " + userId);
        }
        
        return postRepository.getUserFeed(user);
    }
    
    public boolean isPostLikedByUser(Post post, Long userId) {
        User user = userRepository.findById(userId);
        
        if (user == null) {
            throw new NotFoundException("User not found with id: " + userId);
        }
        
        return postRepository.isPostLikedByUser(post, user);
    }
}
