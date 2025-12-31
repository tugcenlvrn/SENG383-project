package com.kidtask.views;

import com.kidtask.models.User;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

public class LoginView extends VBox {
    private TextField usernameField;
    private PasswordField passwordField;
    private Button loginButton;
    private Button backButton;
    private Label errorLabel;
    private User.Role selectedRole;
    private Runnable onLoginSuccess;
    private Runnable onBack;

    public LoginView(User.Role role) {
        this.selectedRole = role;
        setupView();
    }

    private void setupView() {
        this.getStylesheets().add(getClass().getResource("/css/style.css").toExternalForm());
        this.getStyleClass().add("welcome-container");
        this.setAlignment(Pos.CENTER);
        this.setSpacing(20);
        this.setPadding(new Insets(40));

        VBox loginForm = new VBox(20);
        loginForm.getStyleClass().add("login-form");
        loginForm.setAlignment(Pos.CENTER);
        loginForm.setPadding(new Insets(40));
        loginForm.setPrefWidth(400);
        loginForm.setMaxWidth(400);

        Label title = new Label("Login as " + selectedRole.name());
        title.getStyleClass().add("title-text");
        title.setFont(Font.font("Segoe UI", FontWeight.BOLD, 24));

        VBox fields = new VBox(15);
        fields.setAlignment(Pos.CENTER);
        fields.setPrefWidth(350);

        Label usernameLabel = new Label("Username:");
        usernameLabel.setFont(Font.font("Segoe UI", FontWeight.SEMI_BOLD, 14));
        usernameLabel.setTextFill(javafx.scene.paint.Color.web("#0056B3"));

        usernameField = new TextField();
        usernameField.getStyleClass().add("input-field");
        usernameField.setPrefWidth(350);
        // Test için placeholder - role göre örnek kullanıcı adı
        String usernamePlaceholder = selectedRole == User.Role.KID ? "kid1" : 
                                     selectedRole == User.Role.PARENT ? "parent1" : "teacher1";
        usernameField.setPromptText("Örn: " + usernamePlaceholder);

        Label passwordLabel = new Label("Password:");
        passwordLabel.setFont(Font.font("Segoe UI", FontWeight.SEMI_BOLD, 14));
        passwordLabel.setTextFill(javafx.scene.paint.Color.web("#0056B3"));

        passwordField = new PasswordField();
        passwordField.getStyleClass().add("input-field");
        passwordField.setPrefWidth(350);
        passwordField.setPromptText("Örn: pass123");

        errorLabel = new Label();
        errorLabel.setFont(Font.font("Segoe UI", 12));
        errorLabel.setTextFill(javafx.scene.paint.Color.RED);
        errorLabel.setVisible(false);

        HBox buttons = new HBox(15);
        buttons.setAlignment(Pos.CENTER);

        backButton = new Button("Back");
        backButton.getStyleClass().add("kid-button");
        backButton.setPrefWidth(120);
        backButton.setOnAction(e -> {
            if (onBack != null) {
                onBack.run();
            }
        });

        loginButton = new Button("Login");
        loginButton.getStyleClass().add("kid-button");
        loginButton.setPrefWidth(120);
        loginButton.setOnAction(e -> handleLogin());

        buttons.getChildren().addAll(backButton, loginButton);

        fields.getChildren().addAll(
            usernameLabel, usernameField,
            passwordLabel, passwordField,
            errorLabel, buttons
        );

        loginForm.getChildren().addAll(title, fields);
        this.getChildren().add(loginForm);
    }

    private void handleLogin() {
        String username = usernameField.getText().trim();
        String password = passwordField.getText();

        if (username.isEmpty() || password.isEmpty()) {
            showError("Please fill in all fields");
            return;
        }

        if (onLoginSuccess != null) {
            onLoginSuccess.run();
        }
    }

    public void showError(String message) {
        errorLabel.setText(message);
        errorLabel.setVisible(true);
    }

    public void clearError() {
        errorLabel.setVisible(false);
        errorLabel.setText("");
    }

    public String getUsername() {
        return usernameField.getText().trim();
    }

    public String getPassword() {
        return passwordField.getText();
    }

    public User.Role getSelectedRole() {
        return selectedRole;
    }

    public void setOnLoginSuccess(Runnable handler) {
        this.onLoginSuccess = handler;
    }

    public void setOnBack(Runnable handler) {
        this.onBack = handler;
    }
}

