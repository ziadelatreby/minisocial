package com.minisocial.minisocialapi.dtos;

import com.minisocial.minisocialapi.entities.User;

import java.io.Serializable;

public class UserProfileDTO implements Serializable {
    private Long id;
    private String name;
    private String email;
    private String bio;
    private String role;
    private int friendsCount;
    
    public UserProfileDTO() {
    }
    
    public UserProfileDTO(User user) {
        this.id = user.getId();
        this.name = user.getName();
        this.email = user.getEmail();
        this.bio = user.getBio();
        this.role = user.getRole();
        // Don't try to access the lazy-loaded friends collection
        this.friendsCount = 0; // We'll set this separately if needed
    }
    
    // Constructor that allows explicitly setting the friends count
    public UserProfileDTO(User user, int friendsCount) {
        this(user);
        this.friendsCount = friendsCount;
    }
    
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    public String getEmail() {
        return email;
    }
    
    public void setEmail(String email) {
        this.email = email;
    }
    
    public String getBio() {
        return bio;
    }
    
    public void setBio(String bio) {
        this.bio = bio;
    }
    
    public String getRole() {
        return role;
    }
    
    public void setRole(String role) {
        this.role = role;
    }
    
    public int getFriendsCount() {
        return friendsCount;
    }
    
    public void setFriendsCount(int friendsCount) {
        this.friendsCount = friendsCount;
    }
}
