package com.kidtask.models;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class Task {
    private int id;
    private String title;
    private String description;
    private int points;
    private TaskStatus status;
    private TaskType type;
    private String assignee;
    private String creator; // Task'ı veren kişi (username)
    private int rating; // 0-5
    private LocalDate dueDate;

    public enum TaskStatus {
        ASSIGNED, PENDING_APPROVAL, COMPLETED, FINALIZED, REJECTED
    }

    public enum TaskType {
        SCHOOL, HOME
    }

    public Task(int id, String title, String description, int points, TaskStatus status, 
                TaskType type, String assignee, String creator, int rating, LocalDate dueDate) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.points = points;
        this.status = status;
        this.type = type;
        this.assignee = assignee;
        this.creator = creator;
        this.rating = rating;
        this.dueDate = dueDate;
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

    public int getPoints() {
        return points;
    }

    public void setPoints(int points) {
        this.points = points;
    }

    public TaskStatus getStatus() {
        return status;
    }

    public void setStatus(TaskStatus status) {
        this.status = status;
    }

    public TaskType getType() {
        return type;
    }

    public void setType(TaskType type) {
        this.type = type;
    }

    public String getAssignee() {
        return assignee;
    }

    public void setAssignee(String assignee) {
        this.assignee = assignee;
    }

    public String getCreator() {
        return creator;
    }

    public void setCreator(String creator) {
        this.creator = creator;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public LocalDate getDueDate() {
        return dueDate;
    }

    public void setDueDate(LocalDate dueDate) {
        this.dueDate = dueDate;
    }

    // Convert to file format
    public String toFileString() {
        DateTimeFormatter formatter = DateTimeFormatter.ISO_LOCAL_DATE;
        return id + ";" + title + ";" + description + ";" + points + ";" + 
               status.name() + ";" + type.name() + ";" + assignee + ";" + 
               (creator != null ? creator : "") + ";" + rating + ";" + 
               (dueDate != null ? dueDate.format(formatter) : "");
    }

    // Create from file string
    public static Task fromFileString(String line) {
        String[] parts = line.split(";");
        DateTimeFormatter formatter = DateTimeFormatter.ISO_LOCAL_DATE;
        
        // Backward compatibility: eski format (9 parça) veya yeni format (10 parça)
        if (parts.length == 9) {
            // Eski format - creator yok, type'dan çıkar
            LocalDate date = parts[8].isEmpty() ? null : LocalDate.parse(parts[8], formatter);
            String creator = ""; // Eski veriler için boş
            return new Task(
                Integer.parseInt(parts[0]),
                parts[1],
                parts[2],
                Integer.parseInt(parts[3]),
                TaskStatus.valueOf(parts[4]),
                TaskType.valueOf(parts[5]),
                parts[6],
                creator,
                Integer.parseInt(parts[7]),
                date
            );
        } else if (parts.length == 10) {
            // Yeni format - creator var
            LocalDate date = parts[9].isEmpty() ? null : LocalDate.parse(parts[9], formatter);
            return new Task(
                Integer.parseInt(parts[0]),
                parts[1],
                parts[2],
                Integer.parseInt(parts[3]),
                TaskStatus.valueOf(parts[4]),
                TaskType.valueOf(parts[5]),
                parts[6],
                parts[7],
                Integer.parseInt(parts[8]),
                date
            );
        } else {
            throw new IllegalArgumentException("Invalid task format: " + line);
        }
    }
}



