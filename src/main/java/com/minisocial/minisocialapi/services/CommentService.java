package com.minisocial.minisocialapi.services;

import com.minisocial.minisocialapi.entities.Comment;
import com.minisocial.minisocialapi.entities.Post;
import com.minisocial.minisocialapi.entities.User;
import com.minisocial.minisocialapi.errors.ForbiddenException;
import com.minisocial.minisocialapi.errors.NotFoundException;
import com.minisocial.minisocialapi.repositories.CommentRepository;
import com.minisocial.minisocialapi.repositories.PostRepository;
import com.minisocial.minisocialapi.repositories.UserRepository;
import jakarta.ejb.Stateless;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Stateless
public class CommentService {
    
    @Inject
    private CommentRepository commentRepository;
    
    @Inject
    private PostRepository postRepository;
    
    @Inject
    private UserRepository userRepository;
    
    public Comment findById(Long id) {
        Comment comment = commentRepository.findById(id);
        if (comment == null) {
            throw new NotFoundException("Comment not found with id: " + id);
        }
        return comment;
    }
    
    @Transactional
    public Comment createComment(String content, Long postId, Long userId) {
        Post post = postRepository.findById(postId);
        if (post == null) {
            throw new NotFoundException("Post not found with id: " + postId);
        }
        
        User user = userRepository.findById(userId);
        if (user == null) {
            throw new NotFoundException("User not found with id: " + userId);
        }
        
        Comment comment = new Comment();
        comment.setContent(content);
        comment.setPost(post);
        comment.setUser(user);
        comment.setCreatedAt(LocalDateTime.now());
        
        return commentRepository.save(comment);
    }
    
    @Transactional
    public Comment updateComment(Long commentId, String content, Long userId) {
        Comment comment = findById(commentId);
        User user = userRepository.findById(userId);
        
        if (user == null) {
            throw new NotFoundException("User not found with id: " + userId);
        }
        
        // Check if user is the owner of the comment or an admin
        if (!comment.getUser().getId().equals(userId) && 
            !user.getRole().toString().equals("admin")) {
            throw new ForbiddenException("You don't have permission to update this comment");
        }
        
        comment.setContent(content);
        
        return commentRepository.save(comment);
    }
    
    @Transactional
    public void deleteComment(Long commentId, Long userId) {
        Comment comment = findById(commentId);
        User user = userRepository.findById(userId);
        
        if (user == null) {
            throw new NotFoundException("User not found with id: " + userId);
        }
        
        // Check if user is the owner of the comment or an admin
        if (!comment.getUser().getId().equals(userId) && 
            !user.getRole().toString().equals("admin")) {
            throw new ForbiddenException("You don't have permission to delete this comment");
        }
        
        commentRepository.delete(comment);
    }
    
    public List<Comment> getPostComments(Long postId) {
        Post post = postRepository.findById(postId);
        if (post == null) {
            throw new NotFoundException("Post not found with id: " + postId);
        }
        
        return commentRepository.getPostComments(post);
    }
}
