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

public class ParentDashboardView extends BorderPane {
    private DataManager dataManager;
    private User currentUser;
    private VBox sidebar;
    private VBox contentArea;
    private ProgressBar progressBar;
    private Label progressLabel;
    private ScrollPane scrollPane;
    private MenuButton profileMenu;
    private Runnable onLogout;

    public ParentDashboardView(User user) {
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
        this.getStyleClass().add("parent-background");

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

        // Show Approval Center by default
        showApprovalCenter();
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
        Button showTasksBtn = createNavButton("Show Tasks", false);
        showTasksBtn.setOnAction(e -> showTasks());

        Button approvalBtn = createNavButton("Approval Center", true);
        approvalBtn.setOnAction(e -> showApprovalCenter());

        Button assignTaskBtn = createNavButton("Assign Task", false);
        assignTaskBtn.setOnAction(e -> showAssignTask());

        Button wishesBtn = createNavButton("Show Wishes", false);
        wishesBtn.setOnAction(e -> showWishes());

        Button achievementsBtn = createNavButton("Add Achievement", false);
        achievementsBtn.setOnAction(e -> showAddAchievement());

        Button scheduleBtn = createNavButton("Schedule", false);
        scheduleBtn.setOnAction(e -> showSchedule());

        sidebar.getChildren().addAll(logoBox, showTasksBtn, approvalBtn, assignTaskBtn, wishesBtn, achievementsBtn, scheduleBtn);
        return sidebar;
    }

    private Button createNavButton(String text, boolean selected) {
        Button btn = new Button(text);
        btn.getStyleClass().add("parent-nav-button");
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

        Label title = new Label("Family Progress");
        title.setFont(Font.font("Segoe UI", FontWeight.BOLD, 18));
        title.setTextFill(Color.web("#0056B3"));

        HBox progressBox = new HBox(12);
        progressBox.setAlignment(Pos.CENTER);
        progressBar = new ProgressBar();
        progressBar.getStyleClass().add("parent-progress-bar");
        progressBar.setPrefWidth(400);
        progressBar.setPrefHeight(12);

        progressLabel = new Label("0% Complete");
        progressLabel.setFont(Font.font("Segoe UI", FontWeight.BOLD, 18));
        progressLabel.setTextFill(Color.web("#0056B3"));

        progressBox.getChildren().addAll(progressBar, progressLabel);
        progressCard.getChildren().addAll(title, progressBox);

        // Profile Menu
        profileMenu = new MenuButton();
        profileMenu.getStyleClass().add("parent-profile-button");
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

    private void showTasks() {
        contentArea.getChildren().clear();

        Label title = new Label("Current Tasks");
        title.getStyleClass().add("title-text");
        title.setFont(Font.font("Segoe UI", FontWeight.BOLD, 28));

        VBox tasksContainer = new VBox(16);
        tasksContainer.setAlignment(Pos.TOP_CENTER);
        tasksContainer.setPrefWidth(800);

        List<Task> homeTasks = dataManager.getTasksByType(Task.TaskType.HOME);
        for (Task task : homeTasks) {
            tasksContainer.getChildren().add(createTaskCard(task));
        }

        if (tasksContainer.getChildren().isEmpty()) {
            Label noTasks = new Label("No tasks yet!");
            noTasks.setFont(Font.font("Segoe UI", 16));
            noTasks.setTextFill(Color.web("#666666"));
            tasksContainer.getChildren().add(noTasks);
        }

        contentArea.getChildren().addAll(title, tasksContainer);
    }

    private VBox createTaskCard(Task task) {
        VBox card = new VBox(12);
        card.getStyleClass().add("task-card");
        card.setPadding(new Insets(24));
        card.setPrefWidth(800);

        Label title = new Label(task.getTitle());
        title.setFont(Font.font("Segoe UI", FontWeight.BOLD, 18));
        title.setTextFill(Color.web("#333333"));

        Label description = new Label(task.getDescription());
        description.setFont(Font.font("Segoe UI", 14));
        description.setTextFill(Color.web("#666666"));
        description.setWrapText(true);

        Label assignee = new Label("Assigned to: " + task.getAssignee());
        assignee.setFont(Font.font("Segoe UI", 14));
        assignee.setTextFill(Color.web("#0056B3"));

        card.getChildren().addAll(title, description, assignee);
        return card;
    }

    private void showApprovalCenter() {
        contentArea.getChildren().clear();

        Label title = new Label("Approval Center");
        title.getStyleClass().add("title-text");
        title.setFont(Font.font("Segoe UI", FontWeight.BOLD, 28));

        VBox approvalsContainer = new VBox(16);
        approvalsContainer.setAlignment(Pos.TOP_CENTER);
        approvalsContainer.setPrefWidth(800);

        // Sadece Parent'ın verdiği HOME task'ları göster
        // Verileri yeniden yükle
        dataManager.loadAllData();
        List<Task> pendingTasks = dataManager.getTasksByStatus(Task.TaskStatus.PENDING_APPROVAL);
        List<Task> myPendingTasks = pendingTasks.stream()
                .filter(t -> t.getType() == Task.TaskType.HOME)
                .filter(t -> t.getCreator() != null && t.getCreator().equals(currentUser.getUsername()))
                .collect(java.util.stream.Collectors.toList());
        for (Task task : myPendingTasks) {
            approvalsContainer.getChildren().add(createApprovalCard(task));
        }

        if (approvalsContainer.getChildren().isEmpty()) {
            Label noPending = new Label("All caught up! No pending submissions.");
            noPending.setFont(Font.font("Segoe UI", 16));
            noPending.setTextFill(Color.web("#666666"));
            approvalsContainer.getChildren().add(noPending);
        }

        contentArea.getChildren().addAll(title, approvalsContainer);
    }

    private VBox createApprovalCard(Task task) {
        VBox card = new VBox(12);
        card.getStyleClass().add("task-card");
        card.setPadding(new Insets(24));
        card.setPrefWidth(800);

        HBox header = new HBox(20);
        header.setAlignment(Pos.CENTER_LEFT);

        VBox taskInfo = new VBox(8);
        Label assigneeLabel = new Label(task.getAssignee());
        assigneeLabel.setFont(Font.font("Segoe UI", FontWeight.BOLD, 18));
        assigneeLabel.setTextFill(Color.web("#333333"));

        Label title = new Label(task.getTitle());
        title.setFont(Font.font("Segoe UI", 16));
        title.setTextFill(Color.web("#0056B3"));

        taskInfo.getChildren().addAll(assigneeLabel, title);
        HBox.setHgrow(taskInfo, Priority.ALWAYS);

        HBox buttons = new HBox(12);
        Button approveBtn = new Button("Approve");
        approveBtn.getStyleClass().add("parent-button");
        approveBtn.setOnAction(e -> approveTask(task));

        Button rejectBtn = new Button("Reject");
        rejectBtn.getStyleClass().add("parent-reject-button");
        rejectBtn.setOnAction(e -> rejectTask(task));

        buttons.getChildren().addAll(approveBtn, rejectBtn);
        header.getChildren().addAll(taskInfo, buttons);
        card.getChildren().add(header);

        return card;
    }

    private void approveTask(Task task) {
        task.setStatus(Task.TaskStatus.COMPLETED);
        dataManager.saveTask(task);

        // Add points to user and update level (50 points = 1 level)
        User user = dataManager.getUserByUsername(task.getAssignee());
        if (user != null) {
            // Puanları integer olarak direkt topla
            int pointsToAdd = task.getPoints();
            user.setCurrentPoints(user.getCurrentPoints() + pointsToAdd);
            user.setTotalExperience(user.getTotalExperience() + pointsToAdd);
            
            // Level hesaplama: 50 puan = 1 level (minimum level 1)
            int newTotalExp = user.getTotalExperience();
            int newLevel = (newTotalExp / 50) + 1;
            if (newLevel < 1) newLevel = 1;
            if (newLevel > user.getLevel()) {
                user.setLevel(newLevel);
            }
            
            dataManager.saveUser(user);
        }

        showApprovalCenter();
        loadData();
    }

    private void rejectTask(Task task) {
        task.setStatus(Task.TaskStatus.REJECTED);
        dataManager.saveTask(task);
        showApprovalCenter();
    }

    private void showAssignTask() {
        contentArea.getChildren().clear();

        Label title = new Label("Assign New Task");
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
        assignBtn.getStyleClass().add("parent-button");
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
                        Task.TaskType.HOME,
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

    private void showWishes() {
        contentArea.getChildren().clear();

        Label title = new Label("Show Wishes");
        title.getStyleClass().add("title-text");
        title.setFont(Font.font("Segoe UI", FontWeight.BOLD, 28));

        VBox wishesContainer = new VBox(16);
        wishesContainer.setAlignment(Pos.TOP_CENTER);
        wishesContainer.setPrefWidth(800);

        List<Wish> pendingWishes = dataManager.getWishesByStatus(Wish.WishStatus.PENDING);
        for (Wish wish : pendingWishes) {
            wishesContainer.getChildren().add(createWishApprovalCard(wish));
        }

        if (wishesContainer.getChildren().isEmpty()) {
            Label noWishes = new Label("No pending wishes!");
            noWishes.setFont(Font.font("Segoe UI", 16));
            noWishes.setTextFill(Color.web("#666666"));
            wishesContainer.getChildren().add(noWishes);
        }

        contentArea.getChildren().addAll(title, wishesContainer);
    }

    private VBox createWishApprovalCard(Wish wish) {
        VBox card = new VBox(12);
        card.getStyleClass().add("task-card");
        card.setPadding(new Insets(24));
        card.setPrefWidth(800);

        HBox header = new HBox(20);
        header.setAlignment(Pos.CENTER_LEFT);

        VBox wishInfo = new VBox(8);
        Label owner = new Label("From: " + wish.getOwner());
        owner.setFont(Font.font("Segoe UI", FontWeight.BOLD, 16));
        owner.setTextFill(Color.web("#333333"));

        Label title = new Label(wish.getTitle());
        title.setFont(Font.font("Segoe UI", 16));
        title.setTextFill(Color.web("#0056B3"));

        Label cost = new Label("Cost: " + wish.getCost() + " points");
        cost.setFont(Font.font("Segoe UI", 14));
        cost.setTextFill(Color.web("#666666"));

        wishInfo.getChildren().addAll(owner, title, cost);
        HBox.setHgrow(wishInfo, Priority.ALWAYS);

        HBox buttons = new HBox(12);
        Button approveBtn = new Button("Approve");
        approveBtn.getStyleClass().add("parent-button");
        approveBtn.setOnAction(e -> {
            wish.setStatus(Wish.WishStatus.APPROVED);
            dataManager.saveWishes();
            
            // Wish onaylandığında cost'u toplam puandan düş
            User kid = dataManager.getUserByUsername(wish.getOwner());
            if (kid != null) {
                int wishCost = wish.getCost();
                kid.setCurrentPoints(Math.max(0, kid.getCurrentPoints() - wishCost));
                dataManager.saveUser(kid);
            }
            
            showWishes();
        });

        Button rejectBtn = new Button("Reject");
        rejectBtn.getStyleClass().add("parent-reject-button");
        rejectBtn.setOnAction(e -> {
            // Wish'i sil ve puanları geri ver
            User kid = dataManager.getUserByUsername(wish.getOwner());
            if (kid != null) {
                kid.setCurrentPoints(kid.getCurrentPoints() + wish.getCost());
                dataManager.saveUser(kid);
            }
            dataManager.deleteWish(wish.getId());
            showWishes();
        });

        buttons.getChildren().addAll(approveBtn, rejectBtn);
        header.getChildren().addAll(wishInfo, buttons);
        card.getChildren().add(header);

        return card;
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

        Label formTitle = new Label("Create New Incentive");
        formTitle.setFont(Font.font("Segoe UI", FontWeight.BOLD, 20));
        formTitle.setTextFill(Color.web("#0056B3"));

        TextField titleField = new TextField();
        titleField.getStyleClass().add("input-field");
        titleField.setPromptText("Achievement Title (e.g., Math Master)");
        titleField.setPrefWidth(736);

        TextField descriptionField = new TextField();
        descriptionField.getStyleClass().add("input-field");
        descriptionField.setPromptText("Description (Goal) (e.g., Finish 5 Math tasks)");
        descriptionField.setPrefWidth(736);

        TextField rewardField = new TextField();
        rewardField.getStyleClass().add("input-field");
        rewardField.setPromptText("Reward (e.g., Movie Night)");
        rewardField.setPrefWidth(736);

        Button createBtn = new Button("Create Achievement");
        createBtn.getStyleClass().add("parent-button");
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
                    User.Role.PARENT
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

        Label title = new Label("Family Schedule");
        title.getStyleClass().add("title-text");
        title.setFont(Font.font("Segoe UI", FontWeight.BOLD, 28));

        VBox scheduleContainer = new VBox(16);
        scheduleContainer.setAlignment(Pos.TOP_CENTER);
        scheduleContainer.setPrefWidth(800);

        List<Task> homeTasks = dataManager.getTasksByType(Task.TaskType.HOME);
        for (Task task : homeTasks) {
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

        Label dateLabel = new Label("Due: " + task.getDueDate().toString());
        dateLabel.setFont(Font.font("Segoe UI", 14));
        dateLabel.setTextFill(Color.web("#666666"));

        card.getChildren().addAll(title, dateLabel);
        return card;
    }

    private void loadData() {
        // Calculate family progress (home tasks completion rate)
        List<Task> homeTasks = dataManager.getTasksByType(Task.TaskType.HOME);
        if (homeTasks.isEmpty()) {
            progressBar.setProgress(0);
            if (progressLabel != null) {
                progressLabel.setText("0% Complete");
            }
            return;
        }
        long completed = homeTasks.stream()
                .filter(t -> t.getStatus() == Task.TaskStatus.COMPLETED || t.getStatus() == Task.TaskStatus.FINALIZED)
                .count();
        double progress = (double) completed / homeTasks.size();
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
        showApprovalCenter();
    }
}



