package com.kidtask.views;

import com.kidtask.models.*;
import com.kidtask.utils.DataManager;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import java.time.LocalDate;
import java.util.List;

public class TeacherDashboardView extends BorderPane {
    private DataManager dataManager;
    private User currentUser;
    private VBox sidebar;
    private VBox contentArea;
    private ProgressBar progressBar;
    private Label progressLabel;
    private ScrollPane scrollPane;
    private MenuButton profileMenu;
    private Runnable onLogout;

    public TeacherDashboardView(User user) {
        this.currentUser = user;
        this.dataManager = DataManager.getInstance();
        // Verileri yeniden yükle
        dataManager.loadAllData();
        this.currentUser = dataManager.getUserByUsername(user.getUsername());
        setupView();
        loadData();
    }

    private void setupView() {
        this.getStylesheets().add(getClass().getResource("/css/style.css").toExternalForm());
        this.getStyleClass().add("teacher-background");

        // Left Sidebar
        sidebar = createSidebar();
        this.setLeft(sidebar);

        // Top Header
        HBox header = createHeader();
        this.setTop(header);

        // Content Area
        contentArea = new VBox(20);
        contentArea.setPadding(new Insets(20));
        contentArea.setAlignment(Pos.TOP_CENTER);

        scrollPane = new ScrollPane(contentArea);
        scrollPane.setFitToWidth(true);
        scrollPane.setFitToHeight(true);
        scrollPane.setStyle("-fx-background-color: transparent;");
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);

        this.setCenter(scrollPane);

        // Show Rate Tasks by default
        showRateTasks();
    }

    private VBox createSidebar() {
        VBox sidebar = new VBox(20);
        sidebar.getStyleClass().add("glass-sidebar");
        sidebar.setPrefWidth(256);
        sidebar.setPadding(new Insets(24));
        sidebar.setSpacing(8);

        // Logo
        HBox logoBox = new HBox(8);
        logoBox.setAlignment(Pos.CENTER_LEFT);
        logoBox.setPadding(new Insets(0, 0, 48, 0));

        StackPane iconPane = new StackPane();
        Rectangle notebook = new Rectangle(32, 40);
        notebook.setFill(Color.web("#0056B3"));
        notebook.setArcWidth(6);
        notebook.setArcHeight(6);
        
        // Lines on notebook
        VBox lines = new VBox(4);
        lines.setAlignment(Pos.CENTER_LEFT);
        lines.setPadding(new Insets(6, 0, 0, 8));
        for (int i = 0; i < 3; i++) {
            Rectangle line = new Rectangle(18, 1.5);
            line.setFill(Color.WHITE);
            lines.getChildren().add(line);
        }
        
        iconPane.getChildren().addAll(notebook, lines);

        Label logoText = new Label("KidTask");
        logoText.getStyleClass().add("logo-text");
        logoText.setFont(Font.font("Segoe UI", FontWeight.BOLD, 20));

        logoBox.getChildren().addAll(iconPane, logoText);

        // Navigation buttons
        Button addTaskBtn = createNavButton("Add School Task", false);
        addTaskBtn.setOnAction(e -> showAddSchoolTask());

        Button rateTasksBtn = createNavButton("Rate Tasks", true);
        rateTasksBtn.setOnAction(e -> showRateTasks());

        Button achievementsBtn = createNavButton("Add Achievement", false);
        achievementsBtn.setOnAction(e -> showAddAchievement());

        Button scheduleBtn = createNavButton("Schedule", false);
        scheduleBtn.setOnAction(e -> showSchedule());

        sidebar.getChildren().addAll(logoBox, addTaskBtn, rateTasksBtn, achievementsBtn, scheduleBtn);
        return sidebar;
    }

    private Button createNavButton(String text, boolean selected) {
        Button btn = new Button(text);
        btn.getStyleClass().add("teacher-nav-button");
        if (selected) {
            btn.getStyleClass().add("selected");
        }
        btn.setPrefWidth(208);
        btn.setAlignment(Pos.CENTER_LEFT);
        return btn;
    }

    private HBox createHeader() {
        HBox header = new HBox(20);
        header.setPadding(new Insets(24));
        header.setAlignment(Pos.CENTER);
        header.setStyle("-fx-background-color: transparent;");

        // Center Progress Card
        VBox progressCard = new VBox(12);
        progressCard.getStyleClass().add("glass-card");
        progressCard.setPadding(new Insets(24));
        progressCard.setPrefWidth(600);
        progressCard.setAlignment(Pos.CENTER);

        Label title = new Label("Class Average");
        title.setFont(Font.font("Segoe UI", FontWeight.BOLD, 18));
        title.setTextFill(Color.web("#0056B3"));

        HBox progressBox = new HBox(12);
        progressBox.setAlignment(Pos.CENTER);
        progressBar = new ProgressBar();
        progressBar.getStyleClass().add("teacher-progress-bar");
        progressBar.setPrefWidth(400);
        progressBar.setPrefHeight(12);

        progressLabel = new Label("0% Complete");
        progressLabel.setFont(Font.font("Segoe UI", FontWeight.BOLD, 18));
        progressLabel.setTextFill(Color.web("#0056B3"));

        progressBox.getChildren().addAll(progressBar, progressLabel);
        progressCard.getChildren().addAll(title, progressBox);

        // Profile Menu
        profileMenu = new MenuButton();
        profileMenu.getStyleClass().add("teacher-profile-button");
        profileMenu.setPrefSize(48, 48);
        profileMenu.setShape(new Circle(24));

        MenuItem logoutItem = new MenuItem("Logout");
        logoutItem.setOnAction(e -> {
            if (onLogout != null) {
                onLogout.run();
            }
        });
        profileMenu.getItems().add(logoutItem);

        header.getChildren().addAll(progressCard, profileMenu);
        return header;
    }

    private void showAddSchoolTask() {
        contentArea.getChildren().clear();

        Label title = new Label("Add School Task");
        title.getStyleClass().add("title-text");
        title.setFont(Font.font("Segoe UI", FontWeight.BOLD, 28));

        VBox formCard = new VBox(20);
        formCard.getStyleClass().add("glass-card");
        formCard.setPadding(new Insets(32));
        formCard.setPrefWidth(800);

        TextField titleField = new TextField();
        titleField.getStyleClass().add("input-field");
        titleField.setPromptText("Task Title");
        titleField.setPrefWidth(736);

        TextArea descriptionArea = new TextArea();
        descriptionArea.getStyleClass().add("text-area");
        descriptionArea.setPromptText("Description");
        descriptionArea.setPrefWidth(736);
        descriptionArea.setPrefHeight(120);
        descriptionArea.setWrapText(true);

        HBox bottomRow = new HBox(16);
        bottomRow.setSpacing(16);

        DatePicker dueDatePicker = new DatePicker();
        dueDatePicker.getStyleClass().add("input-field");
        dueDatePicker.setPrefWidth(360);

        TextField pointsField = new TextField();
        pointsField.getStyleClass().add("input-field");
        pointsField.setPromptText("Reward Points");
        pointsField.setPrefWidth(360);

        bottomRow.getChildren().addAll(dueDatePicker, pointsField);

        ComboBox<String> assigneeCombo = new ComboBox<>();
        assigneeCombo.getStyleClass().add("input-field");
        assigneeCombo.setPrefWidth(736);
        List<User> kids = dataManager.getUsers().stream()
                .filter(u -> u.getRole() == User.Role.KID)
                .toList();
        for (User kid : kids) {
            assigneeCombo.getItems().add(kid.getUsername());
        }

        Button assignBtn = new Button("Assign Task");
        assignBtn.getStyleClass().add("teacher-button");
        assignBtn.setPrefWidth(736);
        assignBtn.setOnAction(e -> {
            String taskTitle = titleField.getText().trim();
            String description = descriptionArea.getText().trim();
            String assignee = assigneeCombo.getValue();
            LocalDate dueDate = dueDatePicker.getValue();
            String pointsStr = pointsField.getText().trim();

            if (!taskTitle.isEmpty() && !description.isEmpty() && assignee != null && dueDate != null && !pointsStr.isEmpty()) {
                try {
                    int points = Integer.parseInt(pointsStr);
                    Task newTask = new Task(
                        dataManager.getNextTaskId(),
                        taskTitle,
                        description,
                        points,
                        Task.TaskStatus.ASSIGNED,
                        Task.TaskType.SCHOOL,
                        assignee,
                        currentUser.getUsername(), // Creator
                        0,
                        dueDate
                    );
                    dataManager.saveTask(newTask);
                    titleField.clear();
                    descriptionArea.clear();
                    dueDatePicker.setValue(null);
                    pointsField.clear();
                    assigneeCombo.setValue(null);
                } catch (NumberFormatException ex) {
                    // Show error
                }
            }
        });

        formCard.getChildren().addAll(titleField, descriptionArea, bottomRow, assigneeCombo, assignBtn);
        contentArea.getChildren().addAll(title, formCard);
    }

    private void showRateTasks() {
        contentArea.getChildren().clear();

        Label title = new Label("Rate Tasks");
        title.getStyleClass().add("title-text");
        title.setFont(Font.font("Segoe UI", FontWeight.BOLD, 28));

        VBox tasksContainer = new VBox(16);
        tasksContainer.setAlignment(Pos.TOP_CENTER);
        tasksContainer.setPrefWidth(800);

        // Get PENDING_APPROVAL SCHOOL tasks - sadece Teacher'ın verdiği görevler
        // Verileri yeniden yükle
        dataManager.loadAllData();
        List<Task> schoolTasks = dataManager.getTasksByType(Task.TaskType.SCHOOL);
        List<Task> pendingTasks = schoolTasks.stream()
                .filter(t -> t.getStatus() == Task.TaskStatus.PENDING_APPROVAL)
                .filter(t -> t.getCreator() != null && t.getCreator().equals(currentUser.getUsername()))
                .toList();

        for (Task task : pendingTasks) {
            tasksContainer.getChildren().add(createRatingCard(task));
        }

        if (tasksContainer.getChildren().isEmpty()) {
            Label noTasks = new Label("No pending school tasks to rate!");
            noTasks.setFont(Font.font("Segoe UI", 16));
            noTasks.setTextFill(Color.web("#666666"));
            tasksContainer.getChildren().add(noTasks);
        }

        contentArea.getChildren().addAll(title, tasksContainer);
    }

    private VBox createRatingCard(Task task) {
        VBox card = new VBox(16);
        card.getStyleClass().add("task-card");
        card.setPadding(new Insets(24));
        card.setPrefWidth(800);

        VBox taskInfo = new VBox(8);
        Label assignee = new Label("Student: " + task.getAssignee());
        assignee.setFont(Font.font("Segoe UI", FontWeight.BOLD, 18));
        assignee.setTextFill(Color.web("#333333"));

        Label title = new Label(task.getTitle());
        title.setFont(Font.font("Segoe UI", 16));
        title.setTextFill(Color.web("#0056B3"));

        Label description = new Label(task.getDescription());
        description.setFont(Font.font("Segoe UI", 14));
        description.setTextFill(Color.web("#666666"));
        description.setWrapText(true);

        taskInfo.getChildren().addAll(assignee, title, description);

        // Star Rating
        Label ratingLabel = new Label("Rating:");
        ratingLabel.setFont(Font.font("Segoe UI", FontWeight.BOLD, 14));
        ratingLabel.setTextFill(Color.web("#0056B3"));

        HBox starsBox = new HBox(8);
        starsBox.setAlignment(Pos.CENTER_LEFT);

        for (int i = 1; i <= 5; i++) {
            final int rating = i;
            Button starBtn = new Button("★");
            starBtn.getStyleClass().add("star-button");
            if (task.getRating() >= rating) {
                starBtn.setStyle("-fx-text-fill: #FFD700; -fx-background-color: transparent; -fx-font-size: 24px;");
            } else {
                starBtn.setStyle("-fx-text-fill: #CCCCCC; -fx-background-color: transparent; -fx-font-size: 24px;");
            }
            starBtn.setOnAction(e -> rateTask(task, rating));
            starsBox.getChildren().add(starBtn);
        }

        VBox ratingBox = new VBox(8);
        ratingBox.getChildren().addAll(ratingLabel, starsBox);

        card.getChildren().addAll(taskInfo, ratingBox);
        return card;
    }

    private void rateTask(Task task, int rating) {
        task.setRating(rating);
        task.setStatus(Task.TaskStatus.FINALIZED);
        dataManager.saveTask(task);

        // Add points to user and update level (50 points = 1 level)
        User user = dataManager.getUserByUsername(task.getAssignee());
        if (user != null) {
            // Puan ekle (rating'e göre: 1 yıldız = 10 puan, 5 yıldız = 50 puan)
            int pointsEarned = rating * 10;
            int newCurrentPoints = user.getCurrentPoints() + pointsEarned;
            int newTotalExp = user.getTotalExperience() + pointsEarned;
            
            user.setCurrentPoints(newCurrentPoints);
            user.setTotalExperience(newTotalExp);
            
            // Level hesaplama: 50 puan = 1 level (minimum level 1)
            int newLevel = (newTotalExp / 50) + 1;
            if (newLevel < 1) newLevel = 1;
            user.setLevel(newLevel);
            
            dataManager.saveUser(user);
        }

        showRateTasks();
        loadData();
    }

    private void showAddAchievement() {
        contentArea.getChildren().clear();

        Label title = new Label("Achievement System");
        title.getStyleClass().add("title-text");
        title.setFont(Font.font("Segoe UI", FontWeight.BOLD, 28));

        VBox formCard = new VBox(20);
        formCard.getStyleClass().add("glass-card");
        formCard.setPadding(new Insets(32));
        formCard.setPrefWidth(800);

        Label formTitle = new Label("Create New Achievement");
        formTitle.setFont(Font.font("Segoe UI", FontWeight.BOLD, 20));
        formTitle.setTextFill(Color.web("#0056B3"));

        TextField titleField = new TextField();
        titleField.getStyleClass().add("input-field");
        titleField.setPromptText("Achievement Title");
        titleField.setPrefWidth(736);

        TextField descriptionField = new TextField();
        descriptionField.getStyleClass().add("input-field");
        descriptionField.setPromptText("Description (Goal)");
        descriptionField.setPrefWidth(736);

        TextField rewardField = new TextField();
        rewardField.getStyleClass().add("input-field");
        rewardField.setPromptText("Reward");
        rewardField.setPrefWidth(736);

        Button createBtn = new Button("Create Achievement");
        createBtn.getStyleClass().add("teacher-button");
        createBtn.setPrefWidth(736);
        createBtn.setOnAction(e -> {
            String achievementTitle = titleField.getText().trim();
            String description = descriptionField.getText().trim();
            String reward = rewardField.getText().trim();

            if (!achievementTitle.isEmpty() && !description.isEmpty() && !reward.isEmpty()) {
                Achievement achievement = new Achievement(
                    dataManager.getNextAchievementId(),
                    achievementTitle,
                    description,
                    reward,
                    User.Role.TEACHER
                );
                dataManager.saveAchievement(achievement);
                titleField.clear();
                descriptionField.clear();
                rewardField.clear();
            }
        });

        formCard.getChildren().addAll(formTitle, titleField, descriptionField, rewardField, createBtn);

        // Existing achievements
        VBox achievementsList = new VBox(16);
        achievementsList.setAlignment(Pos.TOP_CENTER);
        achievementsList.setPrefWidth(800);
        achievementsList.setPadding(new Insets(20, 0, 0, 0));

        Label listTitle = new Label("Active Achievements");
        listTitle.setFont(Font.font("Segoe UI", FontWeight.BOLD, 20));
        listTitle.setTextFill(Color.web("#0056B3"));

        List<Achievement> achievements = dataManager.getAchievements();
        for (Achievement achievement : achievements) {
            achievementsList.getChildren().add(createAchievementCard(achievement));
        }

        contentArea.getChildren().addAll(title, formCard, listTitle, achievementsList);
    }

    private VBox createAchievementCard(Achievement achievement) {
        VBox card = new VBox(12);
        card.getStyleClass().add("task-card");
        card.setPadding(new Insets(24));
        card.setPrefWidth(800);

        Label title = new Label(achievement.getTitle());
        title.setFont(Font.font("Segoe UI", FontWeight.BOLD, 18));
        title.setTextFill(Color.web("#333333"));

        Label description = new Label("Goal: " + achievement.getDescription());
        description.setFont(Font.font("Segoe UI", 14));
        description.setTextFill(Color.web("#0056B3"));

        Label reward = new Label("Reward: " + achievement.getReward());
        reward.setFont(Font.font("Segoe UI", 14));
        reward.setTextFill(Color.web("#666666"));

        card.getChildren().addAll(title, description, reward);
        return card;
    }

    private void showSchedule() {
        contentArea.getChildren().clear();

        Label title = new Label("Schedule");
        title.getStyleClass().add("title-text");
        title.setFont(Font.font("Segoe UI", FontWeight.BOLD, 28));

        VBox scheduleContainer = new VBox(16);
        scheduleContainer.setAlignment(Pos.TOP_CENTER);
        scheduleContainer.setPrefWidth(800);

        List<Task> schoolTasks = dataManager.getTasksByType(Task.TaskType.SCHOOL);
        for (Task task : schoolTasks) {
            if (task.getDueDate() != null) {
                scheduleContainer.getChildren().add(createScheduleCard(task));
            }
        }

        if (scheduleContainer.getChildren().isEmpty()) {
            Label noSchedule = new Label("No scheduled tasks!");
            noSchedule.setFont(Font.font("Segoe UI", 16));
            noSchedule.setTextFill(Color.web("#666666"));
            scheduleContainer.getChildren().add(noSchedule);
        }

        contentArea.getChildren().addAll(title, scheduleContainer);
    }

    private VBox createScheduleCard(Task task) {
        VBox card = new VBox(12);
        card.getStyleClass().add("task-card");
        card.setPadding(new Insets(24));
        card.setPrefWidth(800);

        Label title = new Label(task.getTitle());
        title.setFont(Font.font("Segoe UI", FontWeight.BOLD, 18));
        title.setTextFill(Color.web("#333333"));

        Label assignee = new Label("Assigned to: " + task.getAssignee());
        assignee.setFont(Font.font("Segoe UI", 14));
        assignee.setTextFill(Color.web("#0056B3"));

        Label dateLabel = new Label("Due: " + task.getDueDate().toString());
        dateLabel.setFont(Font.font("Segoe UI", 14));
        dateLabel.setTextFill(Color.web("#666666"));

        card.getChildren().addAll(title, assignee, dateLabel);
        return card;
    }

    private void loadData() {
        // Calculate class average (school tasks completion rate)
        List<Task> schoolTasks = dataManager.getTasksByType(Task.TaskType.SCHOOL);
        if (schoolTasks.isEmpty()) {
            progressBar.setProgress(0);
            if (progressLabel != null) {
                progressLabel.setText("0% Complete");
            }
            return;
        }
        long completed = schoolTasks.stream()
                .filter(t -> t.getStatus() == Task.TaskStatus.COMPLETED || t.getStatus() == Task.TaskStatus.FINALIZED)
                .count();
        double progress = (double) completed / schoolTasks.size();
        progress = Math.max(0, Math.min(1, progress)); // 0-1 arası sınırla
        progressBar.setProgress(progress);
        
        if (progressLabel != null) {
            int percentage = (int) (progress * 100);
            progressLabel.setText(percentage + "% Complete");
        }
    }

    public void setOnLogout(Runnable handler) {
        this.onLogout = handler;
    }

    public void refresh() {
        // Verileri yeniden yükle
        dataManager.loadAllData();
        currentUser = dataManager.getUserByUsername(currentUser.getUsername());
        loadData();
        showRateTasks();
    }
}



