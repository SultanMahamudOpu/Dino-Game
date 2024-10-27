package org.example.dinogame;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.effect.DropShadow;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.stage.Stage;

public class DinoGame extends Application {

    private static final double WIDTH = 800;
    private static final double HEIGHT = 400;
    private static final double GROUND_HEIGHT = 50;

    private Rectangle dino;
    private boolean jumping = false;
    private double jumpVelocity = 0;
    private double gravity = 0.5;

    private Rectangle cactus;
    private double cactusVelocity = -5;

    private Label scoreLabel;
    private int score = 0;

    @Override
    public void start(Stage primaryStage) {
        Pane root = new Pane();
        root.setStyle("-fx-background-color: linear-gradient(to bottom, #87cefa, #1e90ff);");

        // Dino character as a rectangle
        dino = new Rectangle(50, 50, Color.GREEN);
        dino.setX(100);
        dino.setY(HEIGHT - GROUND_HEIGHT - dino.getHeight());
        dino.setEffect(new DropShadow(10, Color.BLACK));

        // Cactus obstacle as a rectangle
        cactus = new Rectangle(30, 50, Color.RED);
        cactus.setX(WIDTH);
        cactus.setY(HEIGHT - GROUND_HEIGHT - cactus.getHeight());
        cactus.setEffect(new DropShadow(5, Color.BLACK));

        // Score label
        scoreLabel = new Label("Score: 0");
        scoreLabel.setFont(new Font("Arial Black", 24));
        scoreLabel.setTextFill(Color.WHITE);
        scoreLabel.setEffect(new DropShadow(5, Color.BLACK));
        scoreLabel.setLayoutX(WIDTH - 200);
        scoreLabel.setLayoutY(20);

        root.getChildren().addAll(dino, cactus, scoreLabel);

        Scene scene = new Scene(root, WIDTH, HEIGHT);
        scene.setOnKeyPressed(e -> {
            switch (e.getCode()) {
                case SPACE:
                case UP:
                    if (!jumping) {
                        jumping = true;
                        jumpVelocity = -12;
                    }
                    break;
            }
        });

        AnimationTimer timer = new AnimationTimer() {
            @Override
            public void handle(long now) {
                update();
            }
        };
        timer.start();

        primaryStage.setScene(scene);
        primaryStage.setTitle("Dino Game");
        primaryStage.show();
    }

    private void update() {
        // Handle jumping
        if (jumping) {
            dino.setY(dino.getY() + jumpVelocity);
            jumpVelocity += gravity;
            if (dino.getY() >= HEIGHT - GROUND_HEIGHT - dino.getHeight()) {
                dino.setY(HEIGHT - GROUND_HEIGHT - dino.getHeight());
                jumping = false;
            }
        }

        // Move cactus
        cactus.setX(cactus.getX() + cactusVelocity);
        if (cactus.getX() + cactus.getWidth() < 0) {
            cactus.setX(WIDTH);
            score++;
            scoreLabel.setText("Score: " + score);
        }

        // Check collision
        if (dino.getBoundsInParent().intersects(cactus.getBoundsInParent())) {
            gameOver();
        }
    }

    private void gameOver() {
        scoreLabel.setText("Game Over! Final Score: " + score);
        cactusVelocity = 0;
        jumpVelocity = 0;
        jumping = true;
    }

    public static void main(String[] args) {
        launch(args);
    }
}
