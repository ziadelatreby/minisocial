package com.minisocial.minisocialapi.mapper;

import com.minisocial.minisocialapi.dto.NotificationDTO;
import com.minisocial.minisocialapi.entities.notification.*;
import com.minisocial.minisocialapi.entities.User;
import com.minisocial.minisocialapi.repository.*;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

@ApplicationScoped
public class NotificationMapper {
    
    public NotificationDTO toDTO(Notification notification) {
        NotificationDTO dto = new NotificationDTO();
        dto.setId(notification.getId());
        dto.setRecipientId(notification.getRecipient().getId());
        dto.setType(notification.getEventType());
        dto.setMessage(notification.getMessage());
        dto.setIsRead(notification.getIsRead());
        dto.setTimestamp(notification.getTimestamp());

        switch (notification.getEventType()) {
            case "FRIEND_REQUEST": mapFriendRequest((FriendRequestNotification) notification, dto); break;
            case "GROUP_JOINED" : mapGroupJoin((GroupJoinNotification) notification, dto); break;
            case "GROUP_LEFT" : mapGroupLeave((GroupLeaveNotification) notification, dto); break;
            case "POST_COMMENTED" : mapComment((CommentNotification) notification, dto); break;
            case "POST_LIKED" : mapLike((LikeNotification) notification, dto); break;
            default: throw new IllegalArgumentException("Unknown notification type");
        }

        return dto;
    }

    private void mapFriendRequest(FriendRequestNotification notification, NotificationDTO dto) {
        dto.addData("requesterId", notification.getRequester().getId());
        dto.addData("requestId", notification.getFriendRequest().getId());
    }

    private void mapGroupJoin(GroupJoinNotification notification, NotificationDTO dto) {
        dto.addData("groupId", notification.getGroup().getId());
        dto.addData("joinedUserId", notification.getJoinedUser().getId());
    }
    private void mapGroupLeave(GroupLeaveNotification notification, NotificationDTO dto) {
        dto.addData("groupId", notification.getGroup().getId());
        dto.addData("leftUserId", notification.getLeftUser().getId());
        dto.addData("wasKicked", notification.isWasKicked());
    }
    private void mapComment(CommentNotification notification, NotificationDTO dto) {
        dto.addData("commentId", notification.getComment().getId());
        dto.addData("postId", notification.getCommentedPost().getId());
        dto.addData("commenterId", notification.getCommenter().getId());
    }
    private void mapLike(LikeNotification notification, NotificationDTO dto) {
        dto.addData("postId", notification.getLikedPost().getId());
        dto.addData("likerId", notification.getLiker().getId());
    }

    @Inject
    private UserRepository userRepo;
    @Inject
    private FriendRequestRepository friendRequestRepo;
    @Inject
    private GroupRepository groupRepo;
    @Inject
    private PostRepository postRepo;
    @Inject
    private CommentRepository commentRepo;




    // --- TO ENTITY ---
    public Notification toEntity(
        NotificationDTO dto
    ) {

        User recipient = userRepo.findById(dto.getRecipientId());
        Notification returnedNoti;


        switch (dto.getType()) {
            case "FRIEND_REQUEST" :
                returnedNoti=dtoToFRN(dto, recipient, userRepo, friendRequestRepo);
            break;
            case "GROUP_JOINED" :
                returnedNoti=dtoToGJN(dto, recipient, userRepo, groupRepo);
            break;
            case "GROUP_LEFT":
                returnedNoti=dtoToGLN(dto, recipient, userRepo, groupRepo);
            break;
            case "POST_COMMENTED" :
                returnedNoti=dtoToCN(dto, recipient, userRepo, postRepo, commentRepo);
            break;
            case  "POST_LIKED":
                returnedNoti=dtoToLN(dto, recipient, userRepo, postRepo);
            break;
            default : 
            returnedNoti=  null;
            throw new IllegalArgumentException("Unknown notification type");
        }

        return returnedNoti;
    }

    private FriendRequestNotification dtoToFRN(NotificationDTO dto,User recipient, UserRepository userRepo, FriendRequestRepository friendRequestRepo) {

                FriendRequestNotification entity = new FriendRequestNotification();
                mapBaseFields(dto, entity, recipient);
                entity.setRequester(userRepo.findById((Long) dto.getData("requesterId")));
                entity.setFriendRequest(friendRequestRepo.findById((Long) dto.getData("requestId")));
                return entity;
    }

    private GroupJoinNotification dtoToGJN(NotificationDTO dto,User recipient, UserRepository userRepo, GroupRepository groupRepo) {
                GroupJoinNotification entity = new GroupJoinNotification();
                mapBaseFields(dto, entity, recipient);
                entity.setGroup(groupRepo.findById((Long) dto.getData("groupId")));
                entity.setJoinedUser(userRepo.findById((Long) dto.getData("joinedUserId")));
                return entity;
    }

    private GroupLeaveNotification dtoToGLN(NotificationDTO dto,User recipient, UserRepository userRepo, GroupRepository groupRepo) {
                GroupLeaveNotification entity = new GroupLeaveNotification();
                mapBaseFields(dto, entity, recipient);
                entity.setGroup(groupRepo.findById((Long) dto.getData("groupId")));
                entity.setLeftUser(userRepo.findById((Long) dto.getData("leftUserId")));
                entity.setWasKicked((Boolean) dto.getData("wasKicked"));
                return entity;
    }
    private CommentNotification dtoToCN(NotificationDTO dto,User recipient, UserRepository userRepo, PostRepository postRepo, CommentRepository commentRepo) {
                CommentNotification entity = new CommentNotification();
                mapBaseFields(dto, entity, recipient);
                entity.setComment(commentRepo.findById((Long) dto.getData("commentId")));
                entity.setCommentedPost(postRepo.findById((Long) dto.getData("postId")));
                entity.setCommenter(userRepo.findById((Long) dto.getData("commenterId")));
                return entity;
    }
    private LikeNotification dtoToLN(NotificationDTO dto,User recipient, UserRepository userRepo, PostRepository postRepo) {
                LikeNotification entity = new LikeNotification();
                mapBaseFields(dto, entity, recipient);
                entity.setLikedPost(postRepo.findById((Long) dto.getData("postId")));
                entity.setLiker(userRepo.findById((Long) dto.getData("likerId")));
                return entity;
    }

    private void mapBaseFields(NotificationDTO dto, Notification entity, User recipient) {
        entity.setRecipient(recipient);
        entity.setEventType(dto.getType());
        entity.setMessage(dto.getMessage());
        entity.setIsRead(dto.getIsRead());
        entity.setTimestamp(dto.getTimestamp());
    }
}