package com.kidtask.models;

public class User {
    private String username;
    private String password;
    private Role role;
    private int level;
    private int currentPoints;
    private int totalExperience;

    public enum Role {
        KID, PARENT, TEACHER
    }

    public User(String username, String password, Role role, int level, int currentPoints, int totalExperience) {
        this.username = username;
        this.password = password;
        this.role = role;
        this.level = level;
        this.currentPoints = currentPoints;
        this.totalExperience = totalExperience;
    }

    // Getters and Setters
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public int getCurrentPoints() {
        return currentPoints;
    }

    public void setCurrentPoints(int currentPoints) {
        this.currentPoints = currentPoints;
    }

    public int getTotalExperience() {
        return totalExperience;
    }

    public void setTotalExperience(int totalExperience) {
        this.totalExperience = totalExperience;
    }

    // Convert to file format
    public String toFileString() {
        return username + ";" + password + ";" + role.name() + ";" + level + ";" + currentPoints + ";" + totalExperience;
    }

    // Create from file string
    public static User fromFileString(String line) {
        String[] parts = line.split(";");
        if (parts.length != 6) {
            throw new IllegalArgumentException("Invalid user format: " + line);
        }
        return new User(
            parts[0],
            parts[1],
            Role.valueOf(parts[2]),
            Integer.parseInt(parts[3]),
            Integer.parseInt(parts[4]),
            Integer.parseInt(parts[5])
        );
    }
}



