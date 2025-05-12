package com.minisocial.minisocialapi.dtos;

import com.minisocial.minisocialapi.entities.Post;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.LocalDateTime;

public class PostDTO {
    private Long id;
    
    @NotNull(message = "Content cannot be null")
    @Size(min = 1, max = 1000, message = "Content must be between 1 and 1000 characters")
    private String content;
    
    private String imageUrl;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private Long userId;
    private String userName;
    private Long groupId;
    private String groupName;
    private int likeCount;
    private int commentCount;
    private boolean likedByCurrentUser;
    
    public PostDTO() {
    }
    
    public PostDTO(Post post, boolean likedByCurrentUser) {
        this.id = post.getId();
        this.content = post.getContent();
        this.imageUrl = post.getImageUrl();
        this.createdAt = post.getCreatedAt();
        this.updatedAt = post.getUpdatedAt();
        
        if (post.getUser() != null) {
            this.userId = post.getUser().getId();
            this.userName = post.getUser().getName();
        }
        
        if (post.getGroup() != null) {
            this.groupId = post.getGroup().getId();
            this.groupName = post.getGroup().getName();
        }
        
        this.likeCount = post.getLikes() != null ? post.getLikes().size() : 0;
        this.commentCount = post.getComments() != null ? post.getComments().size() : 0;
        this.likedByCurrentUser = likedByCurrentUser;
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

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
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

    public Long getGroupId() {
        return groupId;
    }

    public void setGroupId(Long groupId) {
        this.groupId = groupId;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public int getLikeCount() {
        return likeCount;
    }

    public void setLikeCount(int likeCount) {
        this.likeCount = likeCount;
    }

    public int getCommentCount() {
        return commentCount;
    }

    public void setCommentCount(int commentCount) {
        this.commentCount = commentCount;
    }

    public boolean isLikedByCurrentUser() {
        return likedByCurrentUser;
    }

    public void setLikedByCurrentUser(boolean likedByCurrentUser) {
        this.likedByCurrentUser = likedByCurrentUser;
    }
}
