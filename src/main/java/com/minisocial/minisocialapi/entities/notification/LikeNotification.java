package com.minisocial.minisocialapi.entities.notification;

import com.minisocial.minisocialapi.entities.Post;
import com.minisocial.minisocialapi.entities.User;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

@Entity
@DiscriminatorValue("POST_LIKED")
public class LikeNotification extends Notification {
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "liker_id")
    private User liker;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id")
    private Post likedPost;

    public LikeNotification() {}

    public LikeNotification(User recipient, User liker, Post likedPost) {
        super(recipient, "POST_LIKED", "User " + liker.getName() + " liked your post");
        this.liker = liker;
        this.likedPost = likedPost;
    }

    public User getLiker() {
        return liker;
    }
    public void setLiker(User liker) {
        this.liker = liker;
    }
    public Post getLikedPost() {
        return likedPost;
    }
    public void setLikedPost(Post likedPost) {
        this.likedPost = likedPost;
    }
}