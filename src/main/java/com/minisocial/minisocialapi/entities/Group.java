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
}
