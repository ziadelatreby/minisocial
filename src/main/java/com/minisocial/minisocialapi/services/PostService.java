package com.minisocial.minisocialapi.services;

import com.minisocial.minisocialapi.repositories.UserRepository;
import com.minisocial.minisocialapi.repositories.GroupRepository;
import com.minisocial.minisocialapi.repositories.PostRepository;
import com.minisocial.minisocialapi.entities.User;
import com.minisocial.minisocialapi.errors.NotFoundException;

import java.util.ArrayList;
import java.util.List;

import com.minisocial.minisocialapi.dtos.PostDTO;
import com.minisocial.minisocialapi.entities.Group;
import com.minisocial.minisocialapi.entities.Post;

import jakarta.ejb.Stateless;
import jakarta.inject.Inject;

@Stateless
public class PostService {

    @Inject
    private UserRepository userRepository;

    @Inject
    private GroupRepository groupRepository;

    @Inject
    private PostRepository postRepository;

    public Post createPost (Long userId, PostDTO postDTO ){
        String content = postDTO.getContent();
        String imageUrl = postDTO.getImageUrl();
        Long groupId = postDTO.getGroupId();

        Post post = null;
        User user = userRepository.findById(userId);

        if(user == null){
            throw new NotFoundException ("User not found");
        }

        post = new Post(content, user);

        if(imageUrl != null && !imageUrl.isEmpty()){
            post.setImageUrl(imageUrl);
        }

        if(groupId != -1 && groupId != null)
        {
            Group group = groupRepository.findById(groupId);
            if(group == null ){
                throw new NotFoundException("Group not found");
            }
            if(!groupRepository.isUserInGroup(userId, groupId)){
                throw new NotFoundException("User is not a member of this group");
            }
            post.setGroup(group);
        }

        postRepository.save(post);

        return post;
    }

    public Post editPost (Long userId, Long postId, PostDTO postDTO){
        Post post = postRepository.findById(postId);
        if(post == null){
            throw new NotFoundException("Post not found");
        }

        //User validation
        if(!post.getUser().getId().equals(userId)){
            throw new NotFoundException("User is not allowed to edit this post");
        }

        //Group validation
        if(postDTO.getGroupId() != -1 && postDTO.getGroupId() != null){
            Group group = groupRepository.findById(postDTO.getGroupId());
            if(group == null){
                throw new NotFoundException("Group not found");
            }
            if(!post.getGroup().getId().equals(postDTO.getGroupId())){
                throw new NotFoundException("Cannot change group of post");
            }
            if(!groupRepository.isUserInGroup(userId, postDTO.getGroupId())){
                throw new NotFoundException("User is not a member of this group");
            }
        }

        //Content
        if(postDTO.getContent() != null && !postDTO.getContent().isEmpty()){
            post.setContent(postDTO.getContent());
        }else{
            throw new NotFoundException("Content is required !");
        }
        post.setContent(postDTO.getContent());


        //Image url
        if(postDTO.getImageUrl() != null && !postDTO.getImageUrl().isEmpty()){
            post.setImageUrl(postDTO.getImageUrl());
        }
        post.setImageUrl(postDTO.getImageUrl());

        postRepository.save(post);

        return post;
    }

    public boolean deletePost(Long userId, Long postId){

        User user = userRepository.findById(userId);
        if(user == null){
            throw new NotFoundException("User not found");
        }
        Post post = postRepository.findById(postId);
        if(post == null){
            throw new NotFoundException("Post not found");
        }
        if(!post.getUser().getId().equals(userId)){
            throw new NotFoundException("unauthorized user to delete this post");
        }

        postRepository.delete(post);

        return true;
    }

    public List<Post> getPostsOfFriends(User user,User friend){
        List<Post> allFrenPosts= postRepository.findByUserId(friend.getId());
        List<Post> resPosts= new ArrayList<Post>();

        for(Post post : allFrenPosts){
            if(post.getGroup() != null){
                //to check if the user is in the same group as the friend's post
                if(!(groupRepository.isUserInGroup(user.getId(), post.getGroup().getId())))
                    continue;
                
            }
            //post will be added to the result list if 
                //it is not in a group or
                //the user is in the same group as the post
            resPosts.add(post);
        }
        
        return resPosts;
    }
}
