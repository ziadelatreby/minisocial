package com.minisocial.minisocialapi.dtos;

import com.minisocial.minisocialapi.entities.User;

public class UserDTO {
    private Long id;
    private String name;
    private String email;
    private String role;
    private String bio;

    public UserDTO(Long id, String name, String email, String role, String bio) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.role = role;
        this.bio = bio;
    }

    public UserDTO() {

    }


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    public String getBio() {
        return bio;
    }
}
