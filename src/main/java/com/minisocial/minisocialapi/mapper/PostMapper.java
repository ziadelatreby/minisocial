package com.minisocial.minisocialapi.mapper;

import com.minisocial.minisocialapi.dtos.PostDTO;
import com.minisocial.minisocialapi.entities.Post;
import com.minisocial.minisocialapi.errors.NotFoundException;
import com.minisocial.minisocialapi.repositories.GroupRepository;
import com.minisocial.minisocialapi.entities.Group;

import jakarta.ejb.Stateless;
import jakarta.inject.Inject;

@Stateless
public class PostMapper {
    
    @Inject
    private GroupRepository groupRepository;

    public PostDTO toDTO(Post post){
        PostDTO dto = new PostDTO();
        dto.setContent(post.getContent());
        dto.setImageUrl(post.getImageUrl());

        if(post.getGroup() != null){    
            dto.setGroupId(post.getGroup().getId());
        }
        else{
            dto.setGroupId(-1L);
        }

        return dto;
    }

    public Post toEntity(PostDTO dto){
        Post post = new Post();

        if(dto.getGroupId() != null&&dto.getGroupId() != -1){
            Group group = groupRepository.findById(dto.getGroupId());
            if(group == null){
                throw new NotFoundException("Group not found");
            }
            post.setGroup(group);
        }
        post.setContent(dto.getContent());
        post.setImageUrl(dto.getImageUrl());

        return post;
    }
}
