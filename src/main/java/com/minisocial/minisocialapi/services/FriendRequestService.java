package com.minisocial.minisocialapi.services;

import com.minisocial.minisocialapi.dtos.FriendRequestActionDTO;
import com.minisocial.minisocialapi.dtos.FriendRequestDTO;
import com.minisocial.minisocialapi.entities.FriendRequest;
import com.minisocial.minisocialapi.entities.User;
import com.minisocial.minisocialapi.errors.BadRequestException;
import com.minisocial.minisocialapi.errors.ConflictException;
import com.minisocial.minisocialapi.errors.ForbiddenException;
import com.minisocial.minisocialapi.errors.NotFoundException;
import com.minisocial.minisocialapi.repositories.FriendRequestRepository;
import com.minisocial.minisocialapi.repositories.UserRepository;
import jakarta.ejb.Stateless;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;


import java.util.List;

@Stateless
public class FriendRequestService {

    @Inject
    private FriendRequestRepository friendRequestRepository;

    @Inject
    private UserRepository userRepository;

    @Transactional
    public void sendFriendRequest(FriendRequestDTO friendRequestDTO, Long authenticatedUserId) {
        if (friendRequestDTO.getFromUserId() == null || friendRequestDTO.getToUserId() == null) {
            throw new BadRequestException("From user ID and to user ID are required");
        }

        if (!friendRequestDTO.getFromUserId().equals(authenticatedUserId)) {
            throw new ForbiddenException("You can only send friend requests from your own account");
        }

        User fromUser = userRepository.findById(friendRequestDTO.getFromUserId());
        User toUser = userRepository.findById(friendRequestDTO.getToUserId());

        if (fromUser == null) {
            throw new NotFoundException("From user not found");
        }
        if (toUser == null) {
            throw new NotFoundException("To user not found");
        }

        if (userRepository.areFriends(fromUser, toUser)) {
            throw new ConflictException("Users are already friends");
        }

        FriendRequest existingRequest = friendRequestRepository.findBySenderAndReceiver(fromUser, toUser);
        if (existingRequest != null) {
            throw new ConflictException("A friend request already exists between these users");
        }

        FriendRequest reverseRequest = friendRequestRepository.findBySenderAndReceiver(toUser, fromUser);
        if (reverseRequest != null) {
            throw new ConflictException("You already have a pending friend request from this user");
        }

        FriendRequest friendRequest = new FriendRequest(fromUser, toUser);
        friendRequestRepository.save(friendRequest);
    }

    @Transactional
    public void handleFriendRequest(Long requestFromUserId, FriendRequestActionDTO actionDTO, Long authenticatedUserId) {
        if (actionDTO == null || actionDTO.getAction() == null) {
            throw new BadRequestException("Action is required");
        }

        if (!actionDTO.isValidAction()) {
            throw new BadRequestException("Action must be either 'accept' or 'reject'");
        }

        User toUser = userRepository.findById(authenticatedUserId);
        if (toUser == null) {
            throw new NotFoundException("Authenticated user not found");
        }

        User fromUser = userRepository.findById(requestFromUserId);
        if (fromUser == null) {
            throw new NotFoundException("Request sender not found");
        }

        FriendRequest friendRequest = friendRequestRepository.findBySenderAndReceiver(fromUser, toUser);
        if (friendRequest == null) {
            throw new NotFoundException("Friend request not found");
        }

        if ("accept".equalsIgnoreCase(actionDTO.getAction())) {
            acceptFriendRequest(friendRequest, fromUser, toUser);
        } else {
            rejectFriendRequest(friendRequest);
        }
    }

    @Transactional
    private void acceptFriendRequest(FriendRequest friendRequest, User fromUser, User toUser) {
        friendRequest.setStatus("accepted");
        friendRequestRepository.update(friendRequest);

        userRepository.addFriendship(fromUser.getId(), toUser.getId());
    }

    private void rejectFriendRequest(FriendRequest friendRequest) {
        friendRequest.setStatus("rejected");
        friendRequestRepository.update(friendRequest);
    }

    @Transactional
    public List<User> getUserFriends(Long userId, String nameFilter, String emailFilter) {
        User user = userRepository.findById(userId);
        if (user == null) {
            throw new NotFoundException("User not found");
        }
        
        return userRepository.findFriends(user, nameFilter, emailFilter);
    }

    @Transactional
    public List<FriendRequest> getPendingFriendRequests(Long userId) {
        User user = userRepository.findById(userId);
        if (user == null) {
            throw new NotFoundException("User not found");
        }

        return friendRequestRepository.findPendingRequestsByReceiver(user);
    }
}
