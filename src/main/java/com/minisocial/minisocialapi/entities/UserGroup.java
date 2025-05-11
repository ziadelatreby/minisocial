package com.minisocial.minisocialapi.entities;

import com.minisocial.minisocialapi.enums.UserGroupRoleEnum;
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
    private UserGroupRoleEnum role;

    @Temporal(TemporalType.TIMESTAMP)
    private LocalDateTime createdAt;


    public UserGroup() {
        this.createdAt = LocalDateTime.now();
    }
}
