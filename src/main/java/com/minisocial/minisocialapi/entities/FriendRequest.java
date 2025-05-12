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


    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getSender() {
        return this.sender;
    }

    public void setSender(User sender) {
        this.sender = sender;
    }

    public User getReceiver() {
        return this.receiver;
    }

    public void setReceiver(User receiver) {
        this.receiver = receiver;
    }

    public LocalDateTime getDate() {
        return this.date;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }


}
