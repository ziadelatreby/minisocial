package com.minisocial.minisocialapi.services;

import com.minisocial.minisocialapi.entities.Group;
import com.minisocial.minisocialapi.entities.User;
import com.minisocial.minisocialapi.entities.UserGroup;
import com.minisocial.minisocialapi.enums.GROUP_TYPE;
import com.minisocial.minisocialapi.enums.USER_GROUP_ROLE;
import com.minisocial.minisocialapi.errors.BadRequestException;
import com.minisocial.minisocialapi.errors.NotAuthorizedException;
import com.minisocial.minisocialapi.errors.NotFoundException;
import com.minisocial.minisocialapi.repositories.GroupRepository;
import com.minisocial.minisocialapi.repositories.UserRepository;
import jakarta.ejb.Stateless;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

import java.util.List;
import java.util.Set;

@Stateless
@Transactional
public class GroupService {

    @Inject
    private GroupRepository groupRepository;

    @Inject
    UserRepository userRepository;

    public GroupService() {
    }


    public void createGroup(Group groupParams, Long ctxUserId) {
        // Get the authenticated user ID from context
        User user = userRepository.findById(ctxUserId);
        if (user == null) {
            throw new NotFoundException("User not found");
        }


        if(groupRepository.findByName(groupParams.getName()) != null) {
            throw new NotFoundException("Group with given name already exists");
        }

        //create the group
        Group group = new Group();
        group.setName(groupParams.getName());
        group.setDescription(groupParams.getDescription());
        group.setType(groupParams.getType());

        groupRepository.save(group);

        // set the group creator's role as admin
        UserGroup userGroup = new UserGroup();
        userGroup.setGroup(group);
        userGroup.setUser(user);
        userGroup.setRole(USER_GROUP_ROLE.ADMIN);
        groupRepository.saveUserGroup(userGroup);
    }

    public GROUP_TYPE joinGroup(Long groupId, Long ctxUserId) {
        Group group = groupRepository.findById(groupId);
        if (group == null) {
            throw new NotFoundException("Group not found");
        }

        User user = userRepository.findById(ctxUserId);
        if (user == null) {
            throw new NotFoundException("User not found");
        }

        // Check if user is already a member
        if (groupRepository.isUserInGroup(ctxUserId, groupId)) {
            throw new BadRequestException("User already a member of the group");
        }

        if (GROUP_TYPE.OPEN.equals(group.getType())) {
            // Immediately join
            UserGroup userGroup = new UserGroup();
            userGroup.setGroup(group);
            userGroup.setUser(user);
            userGroup.setRole(USER_GROUP_ROLE.MEMBER);
            groupRepository.saveUserGroup(userGroup);
            return GROUP_TYPE.OPEN;
        } else {
            if (user.getRequestedGroups().contains(group)) {
                throw new BadRequestException("join request already sent");
            }

            user.getRequestedGroups().add(group);
            group.getJoinRequesters().add(user);

            userRepository.update(user);

            return GROUP_TYPE.CLOSED;
        }
    }



    public void acceptJoinRequest(Long groupId, Long targetUserId, Long ctxUserId) {
        Group group = groupRepository.findById(groupId);
        if (group == null) throw new NotFoundException("Group not found");

        User adminUser = userRepository.findById(ctxUserId);
        if (adminUser == null) throw new NotFoundException("User not found");

        // Make sure ctx user is an admin
        if (!groupRepository.isUserAdminInGroup(ctxUserId, groupId)) {
            throw new NotAuthorizedException("only admins can accept the join requests");
        }

        User targetUser = userRepository.findById(targetUserId);
        if (targetUser == null) throw new NotFoundException("requested user not found");

        if (!targetUser.getRequestedGroups().contains(group)) {
            throw new NotFoundException("No join request found from this user to the group..");
        }

        // Remove join request
        targetUser.getRequestedGroups().remove(group);
        group.getJoinRequesters().remove(targetUser);

        // add user to the group with MEMBER role
        UserGroup userGroup = new UserGroup();
        userGroup.setGroup(group);
        userGroup.setUser(targetUser);
        userGroup.setRole(USER_GROUP_ROLE.MEMBER);
        groupRepository.saveUserGroup(userGroup);
    }

    public void rejectJoinRequest(Long groupId, Long targetUserId, Long ctxUserId) {
        Group group = groupRepository.findById(groupId);
        if (group == null) throw new NotFoundException("Group not found");

        User adminUser = userRepository.findById(ctxUserId);
        if (adminUser == null) throw new NotFoundException("User not found");
        if (!groupRepository.isUserAdminInGroup(ctxUserId, groupId)) {
            throw new NotAuthorizedException("Only admins can reject join requests");
        }
        User targetUser = userRepository.findById(targetUserId);
        if (targetUser == null) throw new NotFoundException("Requested user not found");

        if (!targetUser.getRequestedGroups().contains(group)) {
            throw new NotFoundException("user doesn't have a join request to the group");
        }

        // remove the request
        targetUser.getRequestedGroups().remove(group);
        group.getJoinRequesters().remove(targetUser);
    }

        public void kickMember(Long groupId, Long targetUserId, Long ctxUserId) {

            if(targetUserId.equals(ctxUserId) ) {
                throw new BadRequestException("user cannot kick himself");
            }

            if (!groupRepository.isUserAdminInGroup(ctxUserId, groupId)) {
                throw new NotAuthorizedException("Access denied. Only admins can kick members from their group.");
            }

            User targetUser = userRepository.findById(targetUserId);
            if (targetUser == null) {
                throw new NotFoundException("User not found");
            }

            UserGroup userGroup = groupRepository.findUserGroup(groupId, targetUserId);
            if (userGroup == null) {
                throw new NotFoundException("User is not a member of this group");
            }

            // deleteUserGroup
            groupRepository.deleteUserGroup(userGroup); // save
        }

    public void promoteMemberToAdmin(Long groupId, Long targetUserId, Long ctxUserId) {

        if(targetUserId.equals(ctxUserId) ) {
            throw new BadRequestException("user cannot promote himself");
        }

        if (!groupRepository.isUserAdminInGroup(ctxUserId, groupId)) {
            throw new NotAuthorizedException("User is not an admin of this group.");
        }

        UserGroup userGroup = groupRepository.findUserGroup(groupId, targetUserId);
        if (userGroup == null) {
            throw new NotFoundException("User is not a member of this group");
        }

        userGroup.setRole(USER_GROUP_ROLE.ADMIN);
        groupRepository.updateUserGroup(userGroup);
    }


    public List<User> getGroupJoinRequests(Long groupId, Long ctxUserId) {
        if (!groupRepository.isUserAdminInGroup(ctxUserId, groupId)) {
            throw new NotAuthorizedException("Only admins can view join requests");
        }

        return groupRepository.getJoinRequestsForGroup(groupId);
    }
}
