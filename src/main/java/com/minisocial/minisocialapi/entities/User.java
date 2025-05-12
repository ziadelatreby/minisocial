package com.minisocial.minisocialapi.entities;

import com.minisocial.minisocialapi.dtos.UserDTO;
import com.minisocial.minisocialapi.enums.USER_ROLE;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Locale;
import java.util.Set;

@Entity
@Table(name = "users")
@NamedQueries({
    @NamedQuery(name = "User.findByEmail", query = "SELECT u FROM User u WHERE u.email = :email")
})
public class User implements Serializable {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @NotNull
    @Column(nullable = false)
    private String name;
    
    @NotNull
    @Column(nullable = false, unique = true)
    private String email;
    
    @NotNull
    @Column(nullable = false)
    private String password;
    
    @Column(length = 1000)
    private String bio;
    
    @NotNull
    @Column(nullable = false)
    private String role;


    @OneToMany(mappedBy = "user")
    private Set<Post> posts;
    
    @OneToMany(mappedBy = "sender")
    private Set<FriendRequest> sentRequests;
    
    @OneToMany(mappedBy = "receiver")
    private Set<FriendRequest> receivedRequests;
    
    @ManyToMany
    @JoinTable(
        name = "user_friends",
        joinColumns = @JoinColumn(name = "user_id"),
        inverseJoinColumns = @JoinColumn(name = "friend_id")
    )
    private Set<User> friends;

    @OneToMany(mappedBy = "user")
    private Set<UserGroup> userGroups;


    @ManyToMany
    @JoinTable(name = "group_join_requests", joinColumns = @JoinColumn(name = "user_id"), inverseJoinColumns = @JoinColumn(name = "group_id"))
    private Set<Group> requestedGroups;


    public User() {
    }
    
    public User(String name, String email, String password, String bio, String role) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.bio = bio;
        this.role = role;
    }
    
    // Getters and Setters
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

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
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

    public Set<Post> getPosts() {
        return posts;
    }

    public void setPosts(Set<Post> posts) {
        this.posts = posts;
    }

    public Set<FriendRequest> getSentRequests() {
        return sentRequests;
    }

    public void setSentRequests(Set<FriendRequest> sentRequests) {
        this.sentRequests = sentRequests;
    }

    public Set<FriendRequest> getReceivedRequests() {
        return receivedRequests;
    }

    public void setReceivedRequests(Set<FriendRequest> receivedRequests) {
        this.receivedRequests = receivedRequests;
    }

    public Set<User> getFriends() {
        return friends;
    }

    public void setFriends(Set<User> friends) {
        this.friends = friends;
    }

    public Set<UserGroup> getGroups() {
        return userGroups;
    }

    public void setGroups(Set<UserGroup> groups) {
        this.userGroups = groups;
    }

    // helprs

    public boolean hasValidRole() {
        return Arrays.stream(USER_ROLE.values()).anyMatch(r -> r.name().toLowerCase(Locale.ROOT).equals(role));
    }

    public UserDTO toDTO() {
        return new UserDTO(id, name, email, role, bio);
    }
    public Set<Group> getRequestedGroups() {
        return requestedGroups;
    }

    public void setRequestedGroups(Set<Group> requestedGroups) {
        this.requestedGroups = requestedGroups;
    }

}
