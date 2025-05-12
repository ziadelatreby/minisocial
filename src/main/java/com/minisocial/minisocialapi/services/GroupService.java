package com.minisocial.minisocialapi.services;

import com.minisocial.minisocialapi.entities.Group;
import com.minisocial.minisocialapi.entities.User;
import com.minisocial.minisocialapi.entities.UserGroup;
import com.minisocial.minisocialapi.enums.GROUP_TYPE;
import com.minisocial.minisocialapi.enums.USER_GROUP_ROLE;
import com.minisocial.minisocialapi.errors.BadRequestException;
import com.minisocial.minisocialapi.errors.NotFoundException;
import com.minisocial.minisocialapi.repositories.GroupRepository;
import com.minisocial.minisocialapi.repositories.UserRepository;
import jakarta.ejb.Stateless;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

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
}
