package com.kidtask;

import com.kidtask.models.User;
import com.kidtask.utils.DataManager;
import com.kidtask.views.*;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

public class KidTaskApplication extends Application {
    private Stage primaryStage;
    private StackPane rootPane;
    private DataManager dataManager;
    private User currentUser;

    @Override
    public void start(Stage primaryStage) {
        this.primaryStage = primaryStage;
        this.dataManager = DataManager.getInstance();

        rootPane = new StackPane();
        Scene scene = new Scene(rootPane, 1200, 800);
        primaryStage.setTitle("KidTask - Task Management System");
        
        // Set application icon
        try {
            Image icon = new Image(getClass().getResourceAsStream("/images/logo.PNG"));
            primaryStage.getIcons().add(icon);
        } catch (Exception e) {
            // Icon yüklenemezse sessizce devam et
        }
        
        primaryStage.setScene(scene);
        primaryStage.setMinWidth(1000);
        primaryStage.setMinHeight(700);
        primaryStage.show();

        showWelcomePage();
    }

    private void showWelcomePage() {
        WelcomeView welcomeView = new WelcomeView();
        welcomeView.setOnLoginClick(() -> showRoleSelection());
        rootPane.getChildren().clear();
        rootPane.getChildren().add(welcomeView);
    }

    private void showRoleSelection() {
        RoleSelectionView roleView = new RoleSelectionView();
        roleView.getKidCard().setOnAction(e -> showLogin(User.Role.KID));
        roleView.getParentCard().setOnAction(e -> showLogin(User.Role.PARENT));
        roleView.getTeacherCard().setOnAction(e -> showLogin(User.Role.TEACHER));
        rootPane.getChildren().clear();
        rootPane.getChildren().add(roleView);
    }

    private void showLogin(User.Role role) {
        LoginView loginView = new LoginView(role);
        loginView.setOnBack(() -> showRoleSelection());
        loginView.setOnLoginSuccess(() -> {
            String username = loginView.getUsername();
            String password = loginView.getPassword();

            if (dataManager.authenticateUser(username, password)) {
                User user = dataManager.getUserByUsername(username);
                if (user.getRole() == role) {
                    currentUser = user;
                    showDashboard(user);
                } else {
                    loginView.showError("Invalid role for this account");
                }
            } else {
                loginView.showError("Invalid username or password");
            }
        });
        rootPane.getChildren().clear();
        rootPane.getChildren().add(loginView);
    }

    private void showDashboard(User user) {
        switch (user.getRole()) {
            case KID:
                KidDashboardView kidView = new KidDashboardView(user);
                kidView.setOnLogout(() -> {
                    currentUser = null;
                    dataManager.loadAllData(); // Reload data on logout
                    showWelcomePage();
                });
                rootPane.getChildren().clear();
                rootPane.getChildren().add(kidView);
                break;

            case PARENT:
                ParentDashboardView parentView = new ParentDashboardView(user);
                parentView.setOnLogout(() -> {
                    currentUser = null;
                    dataManager.loadAllData(); // Reload data on logout
                    showWelcomePage();
                });
                rootPane.getChildren().clear();
                rootPane.getChildren().add(parentView);
                break;

            case TEACHER:
                TeacherDashboardView teacherView = new TeacherDashboardView(user);
                teacherView.setOnLogout(() -> {
                    currentUser = null;
                    dataManager.loadAllData(); // Reload data on logout
                    showWelcomePage();
                });
                rootPane.getChildren().clear();
                rootPane.getChildren().add(teacherView);
                break;
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}



