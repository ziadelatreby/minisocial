package com.minisocial.minisocialapi.entities;

import com.minisocial.minisocialapi.enums.USER_GROUP_ROLE;
import jakarta.persistence.*;

import java.io.Serializable;
import java.time.LocalDateTime;

@Entity
@Table(name="users_groups", uniqueConstraints = @UniqueConstraint(columnNames = {"user_id", "group_id"}))

public class UserGroup implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "group_id", nullable = false)
    private Group group;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private USER_GROUP_ROLE role;

    @Temporal(TemporalType.TIMESTAMP)
    private LocalDateTime createdAt;


    public UserGroup() {
        this.createdAt = LocalDateTime.now();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Group getGroup() {
        return group;
    }

    public void setGroup(Group group) {
        this.group = group;
    }

    public USER_GROUP_ROLE getRole() {
        return role;
    }

    public void setRole(USER_GROUP_ROLE role) {
        this.role = role;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}
