package com.minisocial.minisocialapi.dtos;

public class GroupDTO {
    private Long id;
    private String name;
    private String description;
    private String type;

    public GroupDTO(Long id, String name, String description, String type) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.type = type;
    }

    public GroupDTO() {

    }

    // Getters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }
    public String getDescription() {
        return description;
    }

    public String getType() {
        return type;
    }
    public void setType(String type) {
        this.type = type;
    }



}
