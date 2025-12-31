package com.kidtask.models;

import com.kidtask.models.User.Role;

public class Achievement {
    private int id;
    private String title;
    private String description;
    private String reward;
    private Role creatorRole;

    public Achievement(int id, String title, String description, String reward, Role creatorRole) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.reward = reward;
        this.creatorRole = creatorRole;
    }

    // Getters and Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getReward() {
        return reward;
    }

    public void setReward(String reward) {
        this.reward = reward;
    }

    public Role getCreatorRole() {
        return creatorRole;
    }

    public void setCreatorRole(Role creatorRole) {
        this.creatorRole = creatorRole;
    }

    // Convert to file format
    public String toFileString() {
        return id + ";" + title + ";" + description + ";" + reward + ";" + creatorRole.name();
    }

    // Create from file string
    public static Achievement fromFileString(String line) {
        String[] parts = line.split(";");
        if (parts.length != 5) {
            throw new IllegalArgumentException("Invalid achievement format: " + line);
        }
        return new Achievement(
            Integer.parseInt(parts[0]),
            parts[1],
            parts[2],
            parts[3],
            Role.valueOf(parts[4])
        );
    }
}



