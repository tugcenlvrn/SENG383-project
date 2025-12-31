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

public class KidDashboardView extends BorderPane {
    private DataManager dataManager;
    private User currentUser;
    private VBox sidebar;
    private VBox contentArea;
    private ProgressBar progressBar;
    private Label levelLabel;
    private Label achievementsLabel;
    private Label progressLabel;
    private ScrollPane scrollPane;
    private MenuButton profileMenu;
    private Runnable onLogout;

    public KidDashboardView(User user) {
        this.currentUser = user;
        this.dataManager = DataManager.getInstance();
        // Verileri yeniden yükle
        dataManager.loadAllData();
        // Kullanıcı bilgilerini güncelle
        this.currentUser = dataManager.getUserByUsername(user.getUsername());
        setupView();
        loadData();
    }

    private void setupView() {
        this.getStylesheets().add(getClass().getResource("/css/style.css").toExternalForm());
        this.getStyleClass().add("kid-background");

        // Background gradient corners
        setupBackground();

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

        // Show My Tasks by default
        showMyTasks();
    }

    private void setupBackground() {
        StackPane backgroundPane = new StackPane();
        backgroundPane.getStyleClass().add("kid-background");
        this.setCenter(backgroundPane);
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
        Button tasksBtn = createNavButton("Tasks", true);
        tasksBtn.setOnAction(e -> showMyTasks());

        Button wishesBtn = createNavButton("Wishes", false);
        wishesBtn.setOnAction(e -> showWishes());

        Button achievementsBtn = createNavButton("Achievements", false);
        achievementsBtn.setOnAction(e -> showAchievements());

        Button scheduleBtn = createNavButton("Schedule", false);
        scheduleBtn.setOnAction(e -> showSchedule());

        sidebar.getChildren().addAll(logoBox, tasksBtn, wishesBtn, achievementsBtn, scheduleBtn);
        return sidebar;
    }

    private Button createNavButton(String text, boolean selected) {
        Button btn = new Button(text);
        btn.getStyleClass().add("kid-nav-button");
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

        HBox topRow = new HBox(20);
        topRow.setAlignment(Pos.CENTER);

        HBox levelBox = new HBox(8);
        levelBox.setAlignment(Pos.CENTER);
        Label levelText = new Label("Level:");
        levelText.setFont(Font.font("Segoe UI", FontWeight.BOLD, 16));
        levelText.setTextFill(Color.web("#0056B3"));
        levelLabel = new Label(String.valueOf(currentUser.getLevel()));
        levelLabel.setFont(Font.font("Segoe UI", FontWeight.BOLD, 20));
        levelLabel.setTextFill(Color.web("#0056B3"));
        levelBox.getChildren().addAll(levelText, levelLabel);

        HBox achievementsBox = new HBox(8);
        achievementsBox.setAlignment(Pos.CENTER);
        Label achievementsText = new Label("Achievements:");
        achievementsText.setFont(Font.font("Segoe UI", FontWeight.BOLD, 16));
        achievementsText.setTextFill(Color.web("#0056B3"));
        achievementsLabel = new Label("0/0");
        achievementsLabel.setFont(Font.font("Segoe UI", FontWeight.BOLD, 20));
        achievementsLabel.setTextFill(Color.web("#0056B3"));
        achievementsBox.getChildren().addAll(achievementsText, achievementsLabel);

        topRow.getChildren().addAll(levelBox, achievementsBox);

        // Progress Bar
        HBox progressBox = new HBox(12);
        progressBox.setAlignment(Pos.CENTER);
        progressBar = new ProgressBar();
        progressBar.getStyleClass().add("kid-progress-bar");
        progressBar.setPrefWidth(400);
        progressBar.setPrefHeight(10);

        progressLabel = new Label("0%");
        progressLabel.setFont(Font.font("Segoe UI", FontWeight.BOLD, 14));
        progressLabel.setTextFill(Color.web("#0056B3"));

        progressBox.getChildren().addAll(progressBar, progressLabel);

        progressCard.getChildren().addAll(topRow, progressBox);

        // Profile Menu
        profileMenu = new MenuButton();
        profileMenu.getStyleClass().add("kid-profile-button");
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

    private void showMyTasks() {
        contentArea.getChildren().clear();

        Label title = new Label("My Tasks");
        title.getStyleClass().add("title-text");
        title.setFont(Font.font("Segoe UI", FontWeight.BOLD, 28));

        VBox tasksContainer = new VBox(16);
        tasksContainer.setAlignment(Pos.TOP_CENTER);
        tasksContainer.setPrefWidth(800);

        // Verileri yeniden yükle
        dataManager.loadAllData();
        // Kullanıcı bilgilerini güncelle
        currentUser = dataManager.getUserByUsername(currentUser.getUsername());
        List<Task> myTasks = dataManager.getTasksByAssignee(currentUser.getUsername());
        for (Task task : myTasks) {
            // Sadece ASSIGNED, PENDING_APPROVAL ve REJECTED task'ları göster
            // COMPLETED ve FINALIZED task'ları gösterme
            if (task.getStatus() == Task.TaskStatus.ASSIGNED || 
                task.getStatus() == Task.TaskStatus.PENDING_APPROVAL || 
                task.getStatus() == Task.TaskStatus.REJECTED) {
                tasksContainer.getChildren().add(createTaskCard(task));
            }
        }

        if (tasksContainer.getChildren().isEmpty()) {
            Label noTasks = new Label("No tasks assigned yet!");
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

        HBox header = new HBox(20);
        header.setAlignment(Pos.CENTER_LEFT);

        VBox taskInfo = new VBox(8);
        Label title = new Label(task.getTitle());
        title.setFont(Font.font("Segoe UI", FontWeight.BOLD, 18));
        title.setTextFill(Color.web("#333333"));

        Label description = new Label(task.getDescription());
        description.setFont(Font.font("Segoe UI", 14));
        description.setTextFill(Color.web("#666666"));
        description.setWrapText(true);

        HBox badges = new HBox(8);
        Label pointsBadge = new Label(task.getPoints() + " points");
        pointsBadge.getStyleClass().add("points-badge");
        badges.getChildren().add(pointsBadge);

        // Creator bilgisi
        String creatorText = "";
        if (task.getCreator() != null && !task.getCreator().isEmpty()) {
            User creatorUser = dataManager.getUserByUsername(task.getCreator());
            if (creatorUser != null) {
                if (creatorUser.getRole() == User.Role.PARENT) {
                    creatorText = "From: Parent";
                } else if (creatorUser.getRole() == User.Role.TEACHER) {
                    creatorText = "From: Teacher";
                }
            }
        }
        Label creatorLabel = new Label(creatorText);
        creatorLabel.setFont(Font.font("Segoe UI", 12));
        creatorLabel.setTextFill(Color.web("#0056B3"));

        taskInfo.getChildren().addAll(title, description, badges, creatorLabel);
        HBox.setHgrow(taskInfo, Priority.ALWAYS);

        // Complete butonu, Waiting veya Rejected yazısı
        if (task.getStatus() == Task.TaskStatus.PENDING_APPROVAL) {
            Label waitingLabel = new Label("Waiting");
            waitingLabel.setFont(Font.font("Segoe UI", FontWeight.BOLD, 14));
            waitingLabel.setTextFill(Color.web("#FFA500"));
            header.getChildren().addAll(taskInfo, waitingLabel);
        } else if (task.getStatus() == Task.TaskStatus.REJECTED) {
            Label rejectedLabel = new Label("Rejected");
            rejectedLabel.setFont(Font.font("Segoe UI", FontWeight.BOLD, 14));
            rejectedLabel.setTextFill(Color.web("#FF4444"));
            header.getChildren().addAll(taskInfo, rejectedLabel);
        } else if (task.getStatus() == Task.TaskStatus.ASSIGNED) {
            Button completeBtn = new Button("Complete");
            completeBtn.getStyleClass().add("kid-button");
            completeBtn.setOnAction(e -> {
                task.setStatus(Task.TaskStatus.PENDING_APPROVAL);
                dataManager.saveTask(task);
                
                // Butonu kaldır, Waiting yazısı ekle
                header.getChildren().clear();
                Label waitingLabel = new Label("Waiting");
                waitingLabel.setFont(Font.font("Segoe UI", FontWeight.BOLD, 14));
                waitingLabel.setTextFill(Color.web("#FFA500"));
                header.getChildren().addAll(taskInfo, waitingLabel);
            });
            header.getChildren().addAll(taskInfo, completeBtn);
        }

        card.getChildren().add(header);

        return card;
    }

    private void showWishes() {
        contentArea.getChildren().clear();

        Label title = new Label("My Wishes");
        title.getStyleClass().add("title-text");
        title.setFont(Font.font("Segoe UI", FontWeight.BOLD, 28));

        VBox wishesContainer = new VBox(16);
        wishesContainer.setAlignment(Pos.TOP_CENTER);
        wishesContainer.setPrefWidth(800);

        // Add Wish Form
        VBox formCard = new VBox(16);
        formCard.getStyleClass().add("glass-card");
        formCard.setPadding(new Insets(24));
        formCard.setPrefWidth(800);

        Label formTitle = new Label("Add New Wish");
        formTitle.setFont(Font.font("Segoe UI", FontWeight.BOLD, 20));
        formTitle.setTextFill(Color.web("#0056B3"));

        TextField wishTitleField = new TextField();
        wishTitleField.getStyleClass().add("input-field");
        wishTitleField.setPromptText("Wish title");
        wishTitleField.setPrefWidth(752);

        TextField costField = new TextField();
        costField.getStyleClass().add("input-field");
        costField.setPromptText("Cost (points)");
        costField.setPrefWidth(752);

        Button addBtn = new Button("Add Wish");
        addBtn.getStyleClass().add("kid-button");
        addBtn.setOnAction(e -> {
            String wishTitle = wishTitleField.getText().trim();
            String costStr = costField.getText().trim();
            if (!wishTitle.isEmpty() && !costStr.isEmpty()) {
                try {
                    int cost = Integer.parseInt(costStr);
                    // Puan kontrolü
                    if (cost > currentUser.getCurrentPoints()) {
                        // Hata göster - yeterli puan yok
                        return;
                    }
                    // Puanlardan düş
                    currentUser.setCurrentPoints(currentUser.getCurrentPoints() - cost);
                    dataManager.saveUser(currentUser);
                    
                    Wish wish = new Wish(
                        dataManager.getNextWishId(),
                        wishTitle,
                        cost,
                        Wish.WishStatus.PENDING,
                        currentUser.getUsername()
                    );
                    dataManager.saveWish(wish);
                    wishTitleField.clear();
                    costField.clear();
                    loadData(); // Puanları güncelle
                    showWishes();
                } catch (NumberFormatException ex) {
                    // Show error
                }
            }
        });

        formCard.getChildren().addAll(formTitle, wishTitleField, costField, addBtn);
        wishesContainer.getChildren().add(formCard);

        // List of wishes
        List<Wish> myWishes = dataManager.getWishesByOwner(currentUser.getUsername());
        for (Wish wish : myWishes) {
            wishesContainer.getChildren().add(createWishCard(wish));
        }

        contentArea.getChildren().addAll(title, wishesContainer);
    }

    private VBox createWishCard(Wish wish) {
        VBox card = new VBox(12);
        card.getStyleClass().add("task-card");
        card.setPadding(new Insets(24));
        card.setPrefWidth(800);

        HBox header = new HBox(20);
        header.setAlignment(Pos.CENTER_LEFT);

        Label title = new Label(wish.getTitle());
        title.setFont(Font.font("Segoe UI", FontWeight.BOLD, 18));
        title.setTextFill(Color.web("#333333"));

        Label costLabel = new Label(wish.getCost() + " points");
        costLabel.getStyleClass().add("points-badge");

        Label statusLabel = new Label(wish.getStatus().name());
        statusLabel.setFont(Font.font("Segoe UI", 14));
        statusLabel.setTextFill(wish.getStatus() == Wish.WishStatus.APPROVED ? Color.GREEN : Color.ORANGE);

        HBox.setHgrow(title, Priority.ALWAYS);
        header.getChildren().addAll(title, costLabel, statusLabel);
        card.getChildren().add(header);

        return card;
    }

    private void showAchievements() {
        contentArea.getChildren().clear();

        Label title = new Label("My Achievements");
        title.getStyleClass().add("title-text");
        title.setFont(Font.font("Segoe UI", FontWeight.BOLD, 28));

        VBox achievementsContainer = new VBox(16);
        achievementsContainer.setAlignment(Pos.TOP_CENTER);
        achievementsContainer.setPrefWidth(800);

        List<Achievement> achievements = dataManager.getAchievements();
        for (Achievement achievement : achievements) {
            achievementsContainer.getChildren().add(createAchievementCard(achievement));
        }

        if (achievementsContainer.getChildren().isEmpty()) {
            Label noAchievements = new Label("No achievements yet!");
            noAchievements.setFont(Font.font("Segoe UI", 16));
            noAchievements.setTextFill(Color.web("#666666"));
            achievementsContainer.getChildren().add(noAchievements);
        }

        contentArea.getChildren().addAll(title, achievementsContainer);
    }

    private VBox createAchievementCard(Achievement achievement) {
        VBox card = new VBox(12);
        card.getStyleClass().add("task-card");
        card.setPadding(new Insets(24));
        card.setPrefWidth(800);

        Label title = new Label(achievement.getTitle());
        title.setFont(Font.font("Segoe UI", FontWeight.BOLD, 18));
        title.setTextFill(Color.web("#333333"));

        Label description = new Label(achievement.getDescription());
        description.setFont(Font.font("Segoe UI", 14));
        description.setTextFill(Color.web("#666666"));

        Label reward = new Label("Reward: " + achievement.getReward());
        reward.setFont(Font.font("Segoe UI", 14));
        reward.setTextFill(Color.web("#0056B3"));

        Label creator = new Label("Added by " + achievement.getCreatorRole().name());
        creator.setFont(Font.font("Segoe UI", 12));
        creator.setTextFill(Color.web("#999999"));

        card.getChildren().addAll(title, description, reward, creator);
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

        List<Task> myTasks = dataManager.getTasksByAssignee(currentUser.getUsername());
        for (Task task : myTasks) {
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
        // Güncel kullanıcı bilgilerini yükle
        currentUser = dataManager.getUserByUsername(currentUser.getUsername());
        
        // Level güncelle: 50 puan = 1 level (minimum level 1)
        int totalExp = currentUser.getTotalExperience();
        int calculatedLevel = (totalExp / 50) + 1;
        if (calculatedLevel < 1) calculatedLevel = 1;
        if (calculatedLevel > currentUser.getLevel()) {
            currentUser.setLevel(calculatedLevel);
            dataManager.saveUser(currentUser);
        }
        
        // Update level label
        if (levelLabel != null) {
            levelLabel.setText(String.valueOf(currentUser.getLevel()));
        }
        
        // Update progress bar - level progress (50 puan = 1 level)
        int currentLevelExp = (currentUser.getLevel() - 1) * 50;
        int nextLevelExp = currentUser.getLevel() * 50;
        double progress = 0;
        if (nextLevelExp > currentLevelExp) {
            int expNeeded = nextLevelExp - currentLevelExp;
            int expGained = totalExp - currentLevelExp;
            progress = Math.max(0, Math.min(1, (double) expGained / expNeeded));
        }
        progressBar.setProgress(progress);

        // Update progress label
        if (progressLabel != null) {
            int percentage = (int) (progress * 100);
            progressLabel.setText(percentage + "%");
        }

        // Update achievements count
        long achievementCount = dataManager.getAchievements().size();
        achievementsLabel.setText(achievementCount + "/" + achievementCount);
    }

    public void setOnLogout(Runnable handler) {
        this.onLogout = handler;
    }

    public void refresh() {
        // Verileri yeniden yükle
        dataManager.loadAllData();
        currentUser = dataManager.getUserByUsername(currentUser.getUsername());
        loadData();
        showMyTasks();
    }
}



