package com.minisocial.minisocialapi.entities;

import jakarta.persistence.*;

import java.io.Serializable;
import java.util.Set;

@Table(name="groups")
@Entity
public class Group implements Serializable {

    @Id
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String description;


    @OneToMany(mappedBy = "group")
    private Set<UserGroup> userGroups;


    @OneToMany(mappedBy = "group")
    private Set<Post> posts;

    // need a manual logic to prevent duplication
    @ManyToMany(mappedBy = "requestedGroups")
    private Set<User> joinRequesters;


    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return this.description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Set<UserGroup> getUserGroups() {
        return this.userGroups;
    }

    public void setUserGroups(Set<UserGroup> userGroups) {
        this.userGroups = userGroups;
    }

    public Set<Post> getPosts() {
        return this.posts;
    }

    public void setPosts(Set<Post> posts) {
        this.posts = posts;
    }

    public Set<User> getJoinRequesters() {
        return this.joinRequesters;
    }

    public void setJoinRequesters(Set<User> joinRequesters) {
        this.joinRequesters = joinRequesters;
    }


}
