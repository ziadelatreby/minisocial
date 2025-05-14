package com.minisocial.minisocialapi.mapper;

import com.minisocial.minisocialapi.dtos.CommentDTO;
import com.minisocial.minisocialapi.entities.Comment;
import com.minisocial.minisocialapi.entities.Post;
import com.minisocial.minisocialapi.entities.User;
import com.minisocial.minisocialapi.errors.BadRequestException;
import com.minisocial.minisocialapi.errors.NotFoundException;
import com.minisocial.minisocialapi.repositories.PostRepository;
import com.minisocial.minisocialapi.repositories.UserRepository;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

@ApplicationScoped
public class CommentMapper {

    @Inject
    private PostRepository postRepository;

    @Inject
    private UserRepository userRepository;

    public CommentDTO toDTO(Comment comment) {
        return new CommentDTO(comment.getContent(), comment.getPost().getId());
    }

    public Comment toEntity(CommentDTO commentDTO, Post post , User user) {

        if(commentDTO.getContent() == null || commentDTO.getContent().isEmpty()) {
            throw new BadRequestException("Content is required");
        }

        Comment comment = new Comment();
        comment.setContent(commentDTO.getContent());
        comment.setPost(post);
        comment.setUser(user);

        return comment;
    }
}
