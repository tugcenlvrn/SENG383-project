package com.kidtask.utils;

import com.kidtask.models.*;
import java.io.*;
import java.nio.file.*;
import java.util.*;
import java.util.stream.Collectors;

public class DataManager {
    private static final String DATA_DIR = "data";
    private static final String USERS_FILE = DATA_DIR + File.separator + "Users.txt";
    private static final String TASKS_FILE = DATA_DIR + File.separator + "Tasks.txt";
    private static final String WISHES_FILE = DATA_DIR + File.separator + "Wishes.txt";
    private static final String ACHIEVEMENTS_FILE = DATA_DIR + File.separator + "Achievements.txt";

    private List<User> users;
    private List<Task> tasks;
    private List<Wish> wishes;
    private List<Achievement> achievements;

    private static DataManager instance;

    private DataManager() {
        ensureDataDirectory();
        loadAllData();
    }

    public static synchronized DataManager getInstance() {
        if (instance == null) {
            instance = new DataManager();
        }
        return instance;
    }

    private void ensureDataDirectory() {
        try {
            Files.createDirectories(Paths.get(DATA_DIR));
            // Create empty files if they don't exist
            createFileIfNotExists(USERS_FILE);
            createFileIfNotExists(TASKS_FILE);
            createFileIfNotExists(WISHES_FILE);
            createFileIfNotExists(ACHIEVEMENTS_FILE);
        } catch (IOException e) {
            System.err.println("Error creating data directory: " + e.getMessage());
        }
    }

    private void createFileIfNotExists(String filePath) {
        File file = new File(filePath);
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                System.err.println("Error creating file " + filePath + ": " + e.getMessage());
            }
        }
    }

    // Load all data from files
    public void loadAllData() {
        users = loadUsers();
        tasks = loadTasks();
        wishes = loadWishes();
        achievements = loadAchievements();
    }

    // ========== USER OPERATIONS ==========
    private List<User> loadUsers() {
        List<User> userList = new ArrayList<>();
        try (BufferedReader reader = Files.newBufferedReader(Paths.get(USERS_FILE))) {
            String line;
            while ((line = reader.readLine()) != null) {
                line = line.trim();
                if (!line.isEmpty()) {
                    try {
                        userList.add(User.fromFileString(line));
                    } catch (Exception e) {
                        System.err.println("Error parsing user: " + line + " - " + e.getMessage());
                    }
                }
            }
        } catch (IOException e) {
            System.err.println("Error loading users: " + e.getMessage());
        }
        return userList;
    }

    public List<User> getUsers() {
        return new ArrayList<>(users);
    }

    public User getUserByUsername(String username) {
        return users.stream()
                .filter(u -> u.getUsername().equals(username))
                .findFirst()
                .orElse(null);
    }

    public boolean authenticateUser(String username, String password) {
        User user = getUserByUsername(username);
        return user != null && user.getPassword().equals(password);
    }

    public void saveUser(User user) {
        User existing = getUserByUsername(user.getUsername());
        if (existing != null) {
            users.remove(existing);
        }
        users.add(user);
        saveUsers();
    }

    public void saveUsers() {
        try (BufferedWriter writer = Files.newBufferedWriter(Paths.get(USERS_FILE))) {
            for (User user : users) {
                writer.write(user.toFileString());
                writer.newLine();
            }
        } catch (IOException e) {
            System.err.println("Error saving users: " + e.getMessage());
        }
    }

    // ========== TASK OPERATIONS ==========
    private List<Task> loadTasks() {
        List<Task> taskList = new ArrayList<>();
        try (BufferedReader reader = Files.newBufferedReader(Paths.get(TASKS_FILE))) {
            String line;
            while ((line = reader.readLine()) != null) {
                line = line.trim();
                if (!line.isEmpty()) {
                    try {
                        taskList.add(Task.fromFileString(line));
                    } catch (Exception e) {
                        System.err.println("Error parsing task: " + line + " - " + e.getMessage());
                    }
                }
            }
        } catch (IOException e) {
            System.err.println("Error loading tasks: " + e.getMessage());
        }
        return taskList;
    }

    public List<Task> getTasks() {
        return new ArrayList<>(tasks);
    }

    public List<Task> getTasksByAssignee(String assignee) {
        return tasks.stream()
                .filter(t -> t.getAssignee().equals(assignee))
                .collect(Collectors.toList());
    }

    public List<Task> getTasksByStatus(Task.TaskStatus status) {
        return tasks.stream()
                .filter(t -> t.getStatus() == status)
                .collect(Collectors.toList());
    }

    public List<Task> getTasksByType(Task.TaskType type) {
        return tasks.stream()
                .filter(t -> t.getType() == type)
                .collect(Collectors.toList());
    }

    public Task getTaskById(int id) {
        return tasks.stream()
                .filter(t -> t.getId() == id)
                .findFirst()
                .orElse(null);
    }

    public void saveTask(Task task) {
        Task existing = getTaskById(task.getId());
        if (existing != null) {
            tasks.remove(existing);
        }
        tasks.add(task);
        saveTasks();
    }

    public void saveTasks() {
        try (BufferedWriter writer = Files.newBufferedWriter(Paths.get(TASKS_FILE))) {
            for (Task task : tasks) {
                writer.write(task.toFileString());
                writer.newLine();
            }
        } catch (IOException e) {
            System.err.println("Error saving tasks: " + e.getMessage());
        }
    }

    public int getNextTaskId() {
        return tasks.stream()
                .mapToInt(Task::getId)
                .max()
                .orElse(0) + 1;
    }

    // ========== WISH OPERATIONS ==========
    private List<Wish> loadWishes() {
        List<Wish> wishList = new ArrayList<>();
        try (BufferedReader reader = Files.newBufferedReader(Paths.get(WISHES_FILE))) {
            String line;
            while ((line = reader.readLine()) != null) {
                line = line.trim();
                if (!line.isEmpty()) {
                    try {
                        wishList.add(Wish.fromFileString(line));
                    } catch (Exception e) {
                        System.err.println("Error parsing wish: " + line + " - " + e.getMessage());
                    }
                }
            }
        } catch (IOException e) {
            System.err.println("Error loading wishes: " + e.getMessage());
        }
        return wishList;
    }

    public List<Wish> getWishes() {
        return new ArrayList<>(wishes);
    }

    public List<Wish> getWishesByOwner(String owner) {
        return wishes.stream()
                .filter(w -> w.getOwner().equals(owner))
                .collect(Collectors.toList());
    }

    public List<Wish> getWishesByStatus(Wish.WishStatus status) {
        return wishes.stream()
                .filter(w -> w.getStatus() == status)
                .collect(Collectors.toList());
    }

    public void saveWish(Wish wish) {
        wishes.add(wish);
        saveWishes();
    }

    public void saveWishes() {
        try (BufferedWriter writer = Files.newBufferedWriter(Paths.get(WISHES_FILE))) {
            for (Wish wish : wishes) {
                writer.write(wish.toFileString());
                writer.newLine();
            }
        } catch (IOException e) {
            System.err.println("Error saving wishes: " + e.getMessage());
        }
    }

    public void deleteWish(int wishId) {
        wishes.removeIf(w -> w.getId() == wishId);
        saveWishes();
    }

    public int getNextWishId() {
        return wishes.stream()
                .mapToInt(Wish::getId)
                .max()
                .orElse(0) + 1;
    }

    // ========== ACHIEVEMENT OPERATIONS ==========
    private List<Achievement> loadAchievements() {
        List<Achievement> achievementList = new ArrayList<>();
        try (BufferedReader reader = Files.newBufferedReader(Paths.get(ACHIEVEMENTS_FILE))) {
            String line;
            while ((line = reader.readLine()) != null) {
                line = line.trim();
                if (!line.isEmpty()) {
                    try {
                        achievementList.add(Achievement.fromFileString(line));
                    } catch (Exception e) {
                        System.err.println("Error parsing achievement: " + line + " - " + e.getMessage());
                    }
                }
            }
        } catch (IOException e) {
            System.err.println("Error loading achievements: " + e.getMessage());
        }
        return achievementList;
    }

    public List<Achievement> getAchievements() {
        return new ArrayList<>(achievements);
    }

    public void saveAchievement(Achievement achievement) {
        achievements.add(achievement);
        saveAchievements();
    }

    public void saveAchievements() {
        try (BufferedWriter writer = Files.newBufferedWriter(Paths.get(ACHIEVEMENTS_FILE))) {
            for (Achievement achievement : achievements) {
                writer.write(achievement.toFileString());
                writer.newLine();
            }
        } catch (IOException e) {
            System.err.println("Error saving achievements: " + e.getMessage());
        }
    }

    public int getNextAchievementId() {
        return achievements.stream()
                .mapToInt(Achievement::getId)
                .max()
                .orElse(0) + 1;
    }

    // ========== LEVEL CALCULATION ==========
    public void updateUserLevel(User user, double averageRating) {
        // Level calculation based on average rating
        // Formula: level = (int)(averageRating * 2) + 1
        int newLevel = (int)(averageRating * 2) + 1;
        if (newLevel > user.getLevel()) {
            user.setLevel(newLevel);
            saveUser(user);
        }
    }

    public double calculateAverageRatingForUser(String username) {
        List<Task> userTasks = tasks.stream()
                .filter(t -> t.getAssignee().equals(username))
                .filter(t -> t.getType() == Task.TaskType.SCHOOL)
                .filter(t -> t.getStatus() == Task.TaskStatus.FINALIZED)
                .filter(t -> t.getRating() > 0)
                .collect(Collectors.toList());

        if (userTasks.isEmpty()) {
            return 0.0;
        }

        double sum = userTasks.stream()
                .mapToInt(Task::getRating)
                .sum();

        return sum / userTasks.size();
    }
}



