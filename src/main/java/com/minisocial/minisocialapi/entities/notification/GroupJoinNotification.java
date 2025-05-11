package com.minisocial.minisocialapi.entities.notification;

import com.minisocial.minisocialapi.entities.GroupJoinRequest;
import com.minisocial.minisocialapi.entities.User;
import com.minisocial.minisocialapi.entities.Group;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;

@Entity
@DiscriminatorValue("GROUP_JOINED")
public class GroupJoinNotification extends Notification {

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="group_join_request_id")
    private GroupJoinRequest groupJoinRequest;
    

    // if the recipient is the group owner (or admin), this is the user who joined
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "joined_user_id")
    private User joinedUser;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "group_id")
    private Group group;

    public GroupJoinNotification() {}
    
    public GroupJoinNotification(User recipient, User joinedUser, Group group, GroupJoinRequest request) {
        super(recipient, "GROUP_JOINED", "User " + joinedUser.getName() + " joined group " + group.getName());
        this.joinedUser = joinedUser;
        this.group = group;
        this.groupJoinRequest = request;
    }

    public GroupJoinRequest getGroupJoinRequest() {
        return groupJoinRequest;
    }
    public void setGroupJoinRequest(GroupJoinRequest groupJoinRequest) {
        this.groupJoinRequest = groupJoinRequest;
    }
    public User getJoinedUser() {
        return joinedUser;
    }
    public void setJoinedUser(User joinedUser) {
        this.joinedUser = joinedUser;
    }
    public Group getGroup() {
        return group;
    }
    public void setGroup(Group group) {
        this.group = group;
    }


}