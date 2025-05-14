package com.minisocial.minisocialapi.dtos;

import java.io.Serializable;

public class CommentDTO implements Serializable {

    private String content;
    private Long postId;

    public CommentDTO() {
    }
    public CommentDTO(String content, Long postId) {
        this.content = content;
        this.postId = postId;
    }

    public String getContent() {
        return content;
    }

    public Long getPostId() {
        return postId;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setPostId(Long postId) {
        this.postId = postId;
    }

    
}
