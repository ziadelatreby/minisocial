package com.minisocial.minisocialapi.entities.notification;

import com.minisocial.minisocialapi.entities.User;
import com.minisocial.minisocialapi.entities.Post;
import com.minisocial.minisocialapi.entities.Comment;
import jakarta.persistence.*;

@Entity
@DiscriminatorValue("POST_COMMENTED")
public class CommentNotification extends Notification {
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "commenter_id")
    private User commenter;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id")
    private Post commentedPost;
    
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "comment_id")
    private Comment comment;

    public CommentNotification() {}
    public CommentNotification(User recipient, User commenter, Post commentedPost, Comment comment) {
        super(recipient, "POST_COMMENTED", "User " + commenter.getName() + " commented on your post");
        this.commenter = commenter;
        this.commentedPost = commentedPost;
        this.comment = comment;
    }

    public User getCommenter() {
        return commenter;
    }
    public void setCommenter(User commenter) {
        this.commenter = commenter;
    }
    public Post getCommentedPost() {
        return commentedPost;
    }
    public void setCommentedPost(Post commentedPost) {
        this.commentedPost = commentedPost;
    }
    public Comment getComment() {
        return comment;
    }
    public void setComment(Comment comment) {
        this.comment = comment;
    }
}