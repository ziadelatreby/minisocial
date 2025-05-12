package com.minisocial.minisocialapi.service.notification_service_utiles;

import com.minisocial.minisocialapi.entities.notification.*;


public class MessageStringCreator {

    private MessageStringCreator() {
        // Private constructor to prevent instantiation
    }

    public static String createMessage(Notification notification){
        String message = null;
        switch (notification.getEventType()) {
            case "POST_LIKED":
                message=createLikeMessage((LikeNotification) notification);
            break;
            case "POST_COMMENTED":
                message=createCommentMessage((CommentNotification) notification);
            break;
            case "FRIEND_REQUEST":
                message=createFriendRequestMessage((FriendRequestNotification) notification);
            break;
            case "GROUP_JOINED":
                message=createGroupJoinedMessage((GroupJoinNotification) notification);
            break;
            case "GROUP_LEFT":
                message=createGroupLeavedMessage((GroupLeaveNotification) notification);
            break;
            default:
                break;
        }

        return message;

    }

    public static String createLikeMessage(LikeNotification notification) {
        String likerName = notification.getLiker().getName();
        long postId = notification.getLikedPost().getId();
        return "User " + likerName + " liked your post (postId: " + postId + ")";
    }

    public static String createCommentMessage(CommentNotification notification) {
        String commenterName = notification.getCommenter().getName();
        long postId = notification.getCommentedPost().getId();
        long commentId = notification.getComment().getId();
        return "User " + commenterName + " commented on your post (postId: " + postId + ", commentId: " + commentId
                + ")";
    }

    public static String createFriendRequestMessage(FriendRequestNotification notification) {
        String requesterName = notification.getRequester().getName();
        long requestId = notification.getFriendRequest().getId();
        return "User " + requesterName + " sent you a friend request (requestId: " + requestId + ")";
    }

    public static String createGroupJoinedMessage(GroupJoinNotification notification) {

        String requesterName = notification.getJoinedUser().getName();
        String recipientName = notification.getRecipient().getName();
        String groupName = notification.getGroup().getName();
        
        //sent to the user
        if(requesterName.equals(recipientName))
            return "You joined the group "+groupName;
        else
        //sent to the group owner
            return "User " + requesterName + "  joined to the group "+groupName;
    }


    public static String createGroupLeavedMessage(GroupLeaveNotification notification) {

        String requesterName = notification.getLeftUser().getName();
        String recipientName = notification.getRecipient().getName();
        String groupName = notification.getGroup().getName();
        boolean wasKicked = notification.isWasKicked();
        
        String message ;
        //sent to the user
        if(requesterName.equals(recipientName))
        {
            if(wasKicked)
                message= "You got kicked out of the group "+groupName;
            else
                message = "You left the group "+groupName;
        }
        else
        {
            if(wasKicked)
                message= "User " + requesterName + " got kicked out of the group "+groupName;
            else
                message = "User " + requesterName + " left the group "+groupName;
        }


        
        return message;
    }

}
