package com.minisocial.minisocialapi.dtos;

public class PostDTO {
    private String content;
    private String imageUrl;
    private Long groupId;

    public PostDTO() {
        this.groupId = -1L;
    }
    public PostDTO(String content, String imageUrl, Long groupId) {
        this.content = content;
        this.imageUrl = imageUrl;
        if(groupId != null)
            this.groupId = groupId;
        else
            this.groupId = -1L;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }  

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public Long getGroupId() {
        return groupId;
    }

    public void setGroupId(Long groupId) {
        if(groupId != null)
            this.groupId = groupId;
        else
            this.groupId = -1L;
    }
}