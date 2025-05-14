package com.minisocial.minisocialapi.entities.notification;

import com.minisocial.minisocialapi.entities.FriendRequest;
import com.minisocial.minisocialapi.entities.User;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;

@Entity
@DiscriminatorValue("FRIEND_REQUEST")
public class FriendRequestNotification extends Notification {
    
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "friend_request_id")
    private FriendRequest friendRequest;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "requester_id")
    private User requester;

    public FriendRequestNotification() {}
    public FriendRequestNotification(User recipient, User requester, FriendRequest friendRequest) {
        super(recipient, "FRIEND_REQUEST", "User " + requester.getName() + " sent you a friend request");
        this.requester = requester;
        this.friendRequest = friendRequest;
    }

    public FriendRequest getFriendRequest() {
        return friendRequest;
    }   
    public void setFriendRequest(FriendRequest friendRequest) {
        this.friendRequest = friendRequest;
    }
    public User getRequester() {
        return requester;
    }
    public void setRequester(User requester) {
        this.requester = requester;
    }
}