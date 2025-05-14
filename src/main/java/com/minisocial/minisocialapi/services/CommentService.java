package com.minisocial.minisocialapi.services;

import com.minisocial.minisocialapi.dtos.CommentDTO;
import com.minisocial.minisocialapi.entities.Comment;
import com.minisocial.minisocialapi.entities.User;
import com.minisocial.minisocialapi.entities.notification.Notification;
import com.minisocial.minisocialapi.entities.Post;
import com.minisocial.minisocialapi.mapper.CommentMapper;
import com.minisocial.minisocialapi.repositories.CommentRepository;
import com.minisocial.minisocialapi.repositories.UserRepository;
import com.minisocial.minisocialapi.services.notification_service_utiles.NotificationFactory;
import com.minisocial.minisocialapi.repositories.GroupRepository;
import com.minisocial.minisocialapi.repositories.PostRepository;
import com.minisocial.minisocialapi.errors.*;

import java.util.List;
import jakarta.ejb.Stateless;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

@Stateless
public class CommentService {

    @Inject
    private CommentRepository commentRepository;

    @Inject
    private CommentMapper commentMapper;

    @Inject
    private UserRepository userRepository;

    @Inject
    private PostRepository postRepository;

    @Inject
    private GroupRepository groupRepository;

    @Inject
    private NotificationService notificationService;

    @Transactional
    public Comment createComment(Long postId, CommentDTO commentDTO, Long userId) {

        User user = userRepository.findById(userId);
        if(user == null) {
            throw new NotFoundException("User not found");
        }

        Post post = postRepository.findById(postId);
        if(post == null) {
            throw new NotFoundException("Post not found");
        }
        if(post.getGroup() != null && !groupRepository.isUserInGroup(userId, post.getGroup().getId())) {
            throw new ForbiddenException("User is not a member of the group");
        }

        Comment comment = commentMapper.toEntity(commentDTO,post,user);
        post.addComment(comment);
        commentRepository.saveComment(comment);
     
        if (!post.getUser().getId().equals(userId)) {
            User postOwner = post.getUser();
            Notification notification = NotificationFactory.createCommentNotification(postOwner, user, post, comment);
            notificationService.sendNotification(notification);
        }

        return comment;
    }

    public List<Comment> getCommentsByPostId(Long postId) {
        Post post = postRepository.findById(postId);
        if(post == null) {
            throw new NotFoundException("Post not found");
        }
        List<Comment> comments = commentRepository.findByPostId(postId);

        return comments;
    }
}
