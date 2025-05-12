package com.minisocial.minisocialapi.dtos;

import java.io.Serializable;

public class FriendRequestActionDTO implements Serializable {
    private String action;
    
    public FriendRequestActionDTO() {
    }
    
    public FriendRequestActionDTO(String action) {
        this.action = action;
    }
    
    public String getAction() {
        return action;
    }
    
    public void setAction(String action) {
        this.action = action;
    }
    
    public boolean isValidAction() {
        return "accept".equalsIgnoreCase(action) || "reject".equalsIgnoreCase(action);
    }
}
