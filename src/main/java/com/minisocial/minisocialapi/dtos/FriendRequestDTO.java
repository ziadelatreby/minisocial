package com.minisocial.minisocialapi.dtos;

import java.io.Serializable;

public class FriendRequestDTO implements Serializable {
    private Long fromUserId;
    private Long toUserId;
    
    public FriendRequestDTO() {
    }
    
    public FriendRequestDTO(Long fromUserId, Long toUserId) {
        this.fromUserId = fromUserId;
        this.toUserId = toUserId;
    }
    
    public Long getFromUserId() {
        return fromUserId;
    }
    
    public void setFromUserId(Long fromUserId) {
        this.fromUserId = fromUserId;
    }
    
    public Long getToUserId() {
        return toUserId;
    }
    
    public void setToUserId(Long toUserId) {
        this.toUserId = toUserId;
    }
}
