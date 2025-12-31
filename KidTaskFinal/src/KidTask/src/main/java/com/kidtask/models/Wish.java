package com.kidtask.models;

public class Wish {
    private int id;
    private String title;
    private int cost;
    private WishStatus status;
    private String owner;

    public enum WishStatus {
        PENDING, APPROVED
    }

    public Wish(int id, String title, int cost, WishStatus status, String owner) {
        this.id = id;
        this.title = title;
        this.cost = cost;
        this.status = status;
        this.owner = owner;
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

    public int getCost() {
        return cost;
    }

    public void setCost(int cost) {
        this.cost = cost;
    }

    public WishStatus getStatus() {
        return status;
    }

    public void setStatus(WishStatus status) {
        this.status = status;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    // Convert to file format
    public String toFileString() {
        return id + ";" + title + ";" + cost + ";" + status.name() + ";" + owner;
    }

    // Create from file string
    public static Wish fromFileString(String line) {
        String[] parts = line.split(";");
        if (parts.length != 5) {
            throw new IllegalArgumentException("Invalid wish format: " + line);
        }
        return new Wish(
            Integer.parseInt(parts[0]),
            parts[1],
            Integer.parseInt(parts[2]),
            WishStatus.valueOf(parts[3]),
            parts[4]
        );
    }
}



