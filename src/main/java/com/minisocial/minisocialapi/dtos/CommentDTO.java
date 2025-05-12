package com.minisocial.minisocialapi.dtos;

import com.minisocial.minisocialapi.entities.Comment;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.LocalDateTime;

public class CommentDTO {
    private Long id;
    
    @NotNull(message = "Content cannot be null")
    @Size(min = 1, max = 500, message = "Content must be between 1 and 500 characters")
    private String content;
    
    private LocalDateTime createdAt;
    private Long postId;
    private Long userId;
    private String userName;
    
    public CommentDTO() {
    }
    
    public CommentDTO(Comment comment) {
        this.id = comment.getId();
        this.content = comment.getContent();
        this.createdAt = comment.getCreatedAt();
        
        if (comment.getPost() != null) {
            this.postId = comment.getPost().getId();
        }
        
        if (comment.getUser() != null) {
            this.userId = comment.getUser().getId();
            this.userName = comment.getUser().getName();
        }
    }
    
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public Long getPostId() {
        return postId;
    }

    public void setPostId(Long postId) {
        this.postId = postId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }
}
