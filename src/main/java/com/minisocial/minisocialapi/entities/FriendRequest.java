package com.minisocial.minisocialapi.entities;

import jakarta.persistence.*;

import java.io.Serializable;
import java.time.LocalDateTime;

// The bridge table between user and user for modeling the friend request relationship
// this approach explicitly creates a bridge table entity and makes a many-to-one relationship with each side
// the table can be inserted to the db as the bridge table, containing extra relationship columns

@Table(name="friend_requests", uniqueConstraints = @UniqueConstraint(columnNames = {"receiver", "sender"}))
@Entity
public class FriendRequest implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "receiver", nullable = false)
    private User sender;

    @ManyToOne
    @JoinColumn(name = "sender", nullable = false)
    private User receiver;

    @Column(nullable = false)
    private LocalDateTime date;


    public FriendRequest() {
        this.date = LocalDateTime.now();
    }
}
