package com.minisocial.minisocialapi.entities.notification;

import com.minisocial.minisocialapi.entities.User;
import com.minisocial.minisocialapi.entities.Group;



import jakarta.persistence.*;

@Entity
@DiscriminatorValue("GROUP_LEFT")
public class GroupLeaveNotification extends Notification {
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "left_user_id")
    private User leftUser;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "group_id")
    private Group group;
    
    @Column(name = "was_kicked")
    private boolean wasKicked;

    public GroupLeaveNotification() {}

    public GroupLeaveNotification(User recipient, User leftUser, Group group, boolean wasKicked) {
        super(recipient, "GROUP_LEFT", "User " + leftUser.getName() + " left group " + group.getName());
        this.leftUser = leftUser;
        this.group = group;
        this.wasKicked = wasKicked;
    }

    public User getLeftUser() {
        return leftUser;
    }
    public void setLeftUser(User leftUser) {
        this.leftUser = leftUser;
    }
    public Group getGroup() {
        return group;
    }
    public void setGroup(Group group) {
        this.group = group;
    }
    public boolean isWasKicked() {
        return wasKicked;
    }
    public void setWasKicked(boolean wasKicked) {
        this.wasKicked = wasKicked;
    }
}