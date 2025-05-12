package com.minisocial.minisocialapi.services;

import com.minisocial.minisocialapi.common.JwtUtil;
import com.minisocial.minisocialapi.entities.User;
import com.minisocial.minisocialapi.repositories.UserRepository;
import com.minisocial.minisocialapi.common.utils;
import jakarta.ejb.Stateless;
import jakarta.inject.Inject;
import com.minisocial.minisocialapi.errors.*;
import org.mindrot.jbcrypt.BCrypt;

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


        user.setRole(user.getRole().toUpperCase());
        // hash the password before inserting in db
        user.setPassword(hashPassword(user.getPassword()));

        userRepository.save(user);
    }



    // TODO(Mustafa-Mahmoud-5): implement DTOs for params and responses
    // signin
    public String signinUser(User userParams) {
        if (utils.isNull(userParams.getEmail()) || utils.isNull(userParams.getPassword())) {
            throw new BadRequestException("Email and password are required.");
        }

        User user = userRepository.findByEmail(userParams.getEmail());

        if(userRepository.findByEmail(userParams.getEmail()) == null) {
            throw new NotFoundException("User with given email does not exist");
        }

        if (!isPasswordsMatch(userParams.getPassword(), user.getPassword())) {
            throw new UnauthorizedException("Invalid Password");
        }

        return JwtUtil.generateToken(user.getId());
    }





    // helper services
    public String hashPassword(String plainPassword) {
        return BCrypt.hashpw(plainPassword, BCrypt.gensalt());
    }

    public boolean isPasswordsMatch(String plainPassword, String hashedPassword) {
        return BCrypt.checkpw(plainPassword, hashedPassword);
    }
}
