package com.kidtask.views;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;

public class WelcomeView extends BorderPane {
    private Button loginButton;
    private Button kidCardButton;
    private Button parentCardButton;
    private Button teacherCardButton;
    private Runnable onLoginClick;
    private Runnable onRoleSelected;

    public WelcomeView() {
        setupView();
    }

    private void setupView() {
        this.getStylesheets().add(getClass().getResource("/css/style.css").toExternalForm());
        this.getStyleClass().add("welcome-container");

        // Top bar with Login button - sağ üst köşe
        HBox topBar = new HBox();
        topBar.setAlignment(Pos.TOP_RIGHT);
        topBar.setPadding(new Insets(20, 40, 20, 0));

        loginButton = new Button("Login");
        loginButton.getStyleClass().add("kid-button");
        loginButton.setPrefWidth(100);
        loginButton.setOnAction(e -> {
            if (onLoginClick != null) {
                onLoginClick.run();
            }
        });
        topBar.getChildren().add(loginButton);
        this.setTop(topBar);

        // Logo and tagline
        VBox logoSection = new VBox(10);
        logoSection.setAlignment(Pos.CENTER);

        HBox logoBox = new HBox(8);
        logoBox.setAlignment(Pos.CENTER);

        Label kidLabel = new Label("Kid");
        kidLabel.getStyleClass().add("logo-text");
        kidLabel.setFont(Font.font("Segoe UI", FontWeight.BOLD, 48));

        // Notebook icon (simplified as SVG-like shape)
        StackPane iconPane = new StackPane();
        Rectangle notebook = new Rectangle(40, 50);
        notebook.setFill(Color.web("#0056B3"));
        notebook.setArcWidth(8);
        notebook.setArcHeight(8);
        
        // Lines on notebook
        VBox lines = new VBox(6);
        lines.setAlignment(Pos.CENTER_LEFT);
        lines.setPadding(new Insets(8, 0, 0, 12));
        for (int i = 0; i < 3; i++) {
            Rectangle line = new Rectangle(24, 2);
            line.setFill(Color.WHITE);
            lines.getChildren().add(line);
        }
        
        iconPane.getChildren().addAll(notebook, lines);

        Label taskLabel = new Label("Task");
        taskLabel.getStyleClass().add("logo-text");
        taskLabel.setFont(Font.font("Segoe UI", FontWeight.BOLD, 48));

        logoBox.getChildren().addAll(kidLabel, iconPane, taskLabel);

        Label tagline = new Label("Small steps to big wishes");
        tagline.setFont(Font.font("Segoe UI", FontPosture.ITALIC, 18));
        tagline.setTextFill(Color.web("#666666"));

        logoSection.getChildren().addAll(logoBox, tagline);

        // Welcome card - dikey ve ortalı
        VBox welcomeCard = new VBox(20);
        welcomeCard.getStyleClass().add("glass-card");
        welcomeCard.setAlignment(Pos.CENTER);
        welcomeCard.setPadding(new Insets(40));
        welcomeCard.setMaxWidth(600);
        welcomeCard.setPrefWidth(600);

        Label welcomeTitle = new Label("Welcome to KidTask");
        welcomeTitle.getStyleClass().add("title-text");
        welcomeTitle.setFont(Font.font("Segoe UI", FontWeight.BOLD, 28));

        VBox featuresList = new VBox(12);
        featuresList.setAlignment(Pos.CENTER_LEFT);
        featuresList.setPadding(new Insets(0, 0, 0, 20));

        String[] features = {
            "Complete tasks & earn wishes",
            "Assign tasks & approve rewards",
            "Track school progress & ratings"
        };

        for (String feature : features) {
            HBox featureItem = new HBox(12);
            featureItem.setAlignment(Pos.CENTER_LEFT);
            
            Circle bullet = new Circle(6);
            bullet.setFill(Color.web("#0056B3"));
            
            Label featureLabel = new Label(feature);
            featureLabel.setFont(Font.font("Segoe UI", 16));
            featureLabel.setTextFill(Color.web("#666666"));
            
            featureItem.getChildren().addAll(bullet, featureLabel);
            featuresList.getChildren().add(featureItem);
        }

        welcomeCard.getChildren().addAll(welcomeTitle, featuresList);

        // Ana içerik - logo ve welcome card dikey, ortalı
        VBox mainContent = new VBox(40);
        mainContent.setAlignment(Pos.CENTER);
        mainContent.setPadding(new Insets(20));
        mainContent.getChildren().addAll(logoSection, welcomeCard);
        
        this.setCenter(mainContent);
    }

    public void setOnLoginClick(Runnable handler) {
        this.onLoginClick = handler;
    }

    public Button getLoginButton() {
        return loginButton;
    }
}

