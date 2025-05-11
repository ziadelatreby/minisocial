package com.minisocial.minisocialapi.service.notification_service_utiles;

import com.minisocial.minisocialapi.entities.Comment;
import com.minisocial.minisocialapi.entities.FriendRequest;
import com.minisocial.minisocialapi.entities.Group;
import com.minisocial.minisocialapi.entities.GroupJoinRequest;
import com.minisocial.minisocialapi.entities.Post;
import com.minisocial.minisocialapi.entities.User;
import com.minisocial.minisocialapi.entities.notification.*;

import jakarta.ejb.Stateless;

@Stateless
public class NotificationFactory {

    private NotificationFactory() {
        // Private constructor to prevent instantiation
    }

    public static LikeNotification createLikeNotification(User recipient, User liker, Post likedPost) {
        return new LikeNotification(recipient, liker, likedPost);
    }

    public static CommentNotification createCommentNotification(User recipient, User commenter, Post post, Comment comment) {
        return new CommentNotification(recipient, commenter, post, comment);
    }

    public static FriendRequestNotification createFriendRequestNotification(User recipient, User requester, FriendRequest friendRequest) {
        return new FriendRequestNotification(recipient, requester, friendRequest);
    }

    public static GroupJoinNotification createGroupJoinNotification(User recipient, User joinedUser, Group group, GroupJoinRequest request) {
        return new GroupJoinNotification(recipient, joinedUser, group, request);
    }

    public static GroupLeaveNotification createGroupLeaveNotification(User recipient, User leftUser, Group group, boolean wasKicked) {
        return new GroupLeaveNotification(recipient, leftUser, group, wasKicked);
    }
}