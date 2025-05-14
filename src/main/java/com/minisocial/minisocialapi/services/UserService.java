package com.minisocial.minisocialapi.services;

import com.minisocial.minisocialapi.common.JwtUtil;
import com.minisocial.minisocialapi.dtos.UserDTO;
import com.minisocial.minisocialapi.entities.User;
import com.minisocial.minisocialapi.enums.USER_ROLE;
import com.minisocial.minisocialapi.repositories.UserRepository;
import com.minisocial.minisocialapi.common.utils;
import jakarta.ejb.Stateless;
import jakarta.inject.Inject;
import com.minisocial.minisocialapi.errors.*;
import org.mindrot.jbcrypt.BCrypt;

import java.util.Locale;
import java.util.Objects;


@Stateless
public class UserService {

    @Inject
    private UserRepository userRepository;

    public void signup(User user) {
        if(utils.isNull(user.getEmail()) || utils.isNull(user.getName()) || utils.isNull(user.getPassword())|| utils.isNull(user.getRole())) {
            throw new BadRequestException("Email, name, password and role are required");
        }

        if(!user.hasValidRole()) {
            throw new BadRequestException("User role is either admin or member");
        }
        
        if(userRepository.findByEmail(user.getEmail()) != null) {
            throw new ConflictException("User with given email already exists");
        }


        user.setRole(user.getRole());
        // hash the password before inserting in db
        user.setPassword(hashPassword(user.getPassword()));

        userRepository.save(user);
    }



    // TODO(Mustafa-Mahmoud-5): implement DTOs for params and responses
    // signin
    public String signinUser(User userParams) {
        //DEBUGGING
        System.out.println("\tsigining  up user");

        if (utils.isNull(userParams.getEmail()) || utils.isNull(userParams.getPassword())) {
            throw new BadRequestException("Email and password are required.");
        }

        //DEBUGGING
        System.out.println("\tuserParams.getEmail() : " + userParams.getEmail());

        User user = userRepository.findByEmail(userParams.getEmail());

        if(userRepository.findByEmail(userParams.getEmail()) == null) {
            throw new NotFoundException("User with given email does not exist");
        }

        if (!isPasswordsMatch(userParams.getPassword(), user.getPassword())) {
            throw new UnauthorizedException("Invalid Password");
        }

        //DEBUGGING
        System.out.println("\treturning token");

        return JwtUtil.generateToken(user.getId());
    }


    // Admin has Access on updating any user profile
    public UserDTO updateProfile(Long ctxUserId, Long userIdParam, UserDTO updateParams) {
        // NOTE: Validations should be better handled via a check library or in a validations layer
        if(utils.isNull(updateParams.getName()) || utils.isNull(updateParams.getRole()) ||  utils.isNull(updateParams.getBio())) {
            throw new BadRequestException("Name, role and bio are required");
        }

        if(!utils.isValidRole(updateParams.getRole())) {
            throw new BadRequestException("User role is either admin or member");
        }

        // user to update
        User user = userRepository.findById(userIdParam);

        if(user == null) {
            throw new NotFoundException("User with given id does not exist");
        }

        User ctxUser = userRepository.findById(ctxUserId);
        if(!hasPermissions(ctxUser, userIdParam)) {
            throw new UnauthorizedException("You don't have permissions to update this user profile");
        }

        // Prevent role change if not allowed
        if (utils.getEnumVal(USER_ROLE.ADMIN).equals(user.getRole().toLowerCase(Locale.ROOT)) && utils.getEnumVal(USER_ROLE.MEMBER).equals(updateParams.getRole().toLowerCase(Locale.ROOT))) {
            throw new BadRequestException("Admin cannot be demoted.");
        }
        if (utils.getEnumVal(USER_ROLE.MEMBER).equals(user.getRole().toLowerCase(Locale.ROOT)) && utils.getEnumVal(USER_ROLE.ADMIN).equals(updateParams.getRole().toLowerCase(Locale.ROOT))) {
            throw new BadRequestException("User cannot promote themselves to admin.");
        }


        if(!Objects.equals(user.getEmail(), updateParams.getEmail())) {
            if(userRepository.findByEmail(updateParams.getEmail()) != null) {
                throw new ConflictException("User with given email already exists");
            }
        }

        // Update fields
        user.setName(updateParams.getName());
        user.setRole(updateParams.getRole());
        user.setBio(updateParams.getBio());
        user.setEmail(updateParams.getEmail());

        userRepository.save(user);

        return user.toDTO();
    }



    // helper services
    public String hashPassword(String plainPassword) {
        return BCrypt.hashpw(plainPassword, BCrypt.gensalt());
    }

    public boolean isPasswordsMatch(String plainPassword, String hashedPassword) {
        return BCrypt.checkpw(plainPassword, hashedPassword);
    }


    // if contextUserId is the same as paramUserId
    // if contextUser is ADMIN
    public boolean hasPermissions(User ctXUser, Long userIdParam) {
        return userIdParam.equals(ctXUser.getId()) || utils.getEnumVal(USER_ROLE.ADMIN).equals(ctXUser.getRole());
    }
}
