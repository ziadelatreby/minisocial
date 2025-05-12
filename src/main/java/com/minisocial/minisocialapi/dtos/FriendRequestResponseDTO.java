package com.minisocial.minisocialapi.dtos;

import com.minisocial.minisocialapi.entities.FriendRequest;
import com.minisocial.minisocialapi.entities.User;

import java.io.Serializable;

public class FriendRequestResponseDTO implements Serializable {
    private Long id;
    private UserProfileDTO sender;
    private String status;
    private String createdAt;
    private String updatedAt;
    
    public FriendRequestResponseDTO() {
    }
    
    public FriendRequestResponseDTO(FriendRequest friendRequest) {
        this.id = friendRequest.getId();
        // Only extract necessary properties from sender to avoid loading friends collection
        User sender = friendRequest.getSender();
        UserProfileDTO senderDTO = new UserProfileDTO();
        senderDTO.setId(sender.getId());
        senderDTO.setName(sender.getName());
        senderDTO.setEmail(sender.getEmail());
        senderDTO.setBio(sender.getBio());
        this.sender = senderDTO;
        this.status = friendRequest.getStatus();
        // Convert LocalDateTime to String to avoid serialization issues
        this.createdAt = friendRequest.getCreatedAt() != null ? friendRequest.getCreatedAt().toString() : null;
        this.updatedAt = friendRequest.getUpdatedAt() != null ? friendRequest.getUpdatedAt().toString() : null;
    }
    
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public UserProfileDTO getSender() {
        return sender;
    }
    
    public void setSender(UserProfileDTO sender) {
        this.sender = sender;
    }
    
    public String getStatus() {
        return status;
    }
    
    public void setStatus(String status) {
        this.status = status;
    }
    
    public String getCreatedAt() {
        return createdAt;
    }
    
    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }
    
    public String getUpdatedAt() {
        return updatedAt;
    }
    
    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }
}
