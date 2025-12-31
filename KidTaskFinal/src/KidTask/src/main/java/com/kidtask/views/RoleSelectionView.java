package com.kidtask.views;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

public class RoleSelectionView extends VBox {
    private Button kidCard;
    private Button parentCard;
    private Button teacherCard;
    private Runnable onRoleSelected;

    public RoleSelectionView() {
        setupView();
    }

    private void setupView() {
        this.getStylesheets().add(getClass().getResource("/css/style.css").toExternalForm());
        this.getStyleClass().add("welcome-container");
        this.setAlignment(Pos.CENTER);
        this.setSpacing(30);
        this.setPadding(new Insets(40));

        Label title = new Label("Select Your Role");
        title.getStyleClass().add("title-text");
        title.setFont(Font.font("Segoe UI", FontWeight.BOLD, 36));

        HBox roleCards = new HBox(30);
        roleCards.setAlignment(Pos.CENTER);
        roleCards.setPadding(new Insets(20));

        // Kid Card
        kidCard = createRoleCard("Kid", "Complete tasks and earn rewards", Color.web("#FFDAB9"));
        kidCard.setOnAction(e -> {
            if (onRoleSelected != null) {
                onRoleSelected.run();
            }
        });

        // Parent Card
        parentCard = createRoleCard("Parent", "Assign tasks and approve rewards", Color.web("#98FB98"));
        parentCard.setOnAction(e -> {
            if (onRoleSelected != null) {
                onRoleSelected.run();
            }
        });

        // Teacher Card
        teacherCard = createRoleCard("Teacher", "Track progress and rate tasks", Color.web("#D8BFD8"));
        teacherCard.setOnAction(e -> {
            if (onRoleSelected != null) {
                onRoleSelected.run();
            }
        });

        roleCards.getChildren().addAll(kidCard, parentCard, teacherCard);
        this.getChildren().addAll(title, roleCards);
    }

    private Button createRoleCard(String role, String description, Color accentColor) {
        VBox card = new VBox(15);
        card.getStyleClass().add("role-card");
        card.setAlignment(Pos.CENTER);
        card.setPadding(new Insets(30));
        card.setPrefWidth(250);
        card.setPrefHeight(200);

        Label roleLabel = new Label(role);
        roleLabel.setFont(Font.font("Segoe UI", FontWeight.BOLD, 24));
        roleLabel.setTextFill(Color.web("#0056B3"));

        Label descLabel = new Label(description);
        descLabel.setFont(Font.font("Segoe UI", 14));
        descLabel.setTextFill(Color.web("#666666"));
        descLabel.setWrapText(true);
        descLabel.setAlignment(Pos.CENTER);

        card.getChildren().addAll(roleLabel, descLabel);

        Button button = new Button();
        button.setGraphic(card);
        button.setStyle("-fx-background-color: transparent;");
        button.setPrefWidth(250);
        button.setPrefHeight(200);

        return button;
    }

    public void setOnRoleSelected(Runnable handler) {
        this.onRoleSelected = handler;
    }

    public Button getKidCard() {
        return kidCard;
    }

    public Button getParentCard() {
        return parentCard;
    }

    public Button getTeacherCard() {
        return teacherCard;
    }
}



