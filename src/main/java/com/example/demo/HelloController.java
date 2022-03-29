package com.example.demo;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;


public class HelloController<getRoundRectangle> implements Initializable {
    public AnchorPane anchorPane;
    @FXML
    private Rectangle idSnakeHead;
    @FXML
    private Label idStatus;
    @FXML
    private Label idScore;
    @FXML
    private Rectangle food = new Rectangle();
    @FXML
    private int score;

    private List<Rectangle> snakeRectangles = new ArrayList<Rectangle>();

    private int i;

    private static HelloController helloController;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        createFood();
        helloController = this;
    }

    public List<Rectangle> getSnakeRectangles() {
        return this.snakeRectangles;
    }

    public void setNewRectangle() {
        if (i < 1) {
            snakeRectangles.add(0, idSnakeHead);
            i++;
        }
        snakeRectangles.add(snakeRectangles.size(), HelloController.HelloController().getNewRectangle(snakeRectangles.get(snakeRectangles.size() - 1)));
    }

    public Rectangle getNewRectangle(Rectangle rectangle) {
        Rectangle rectangle1 = new Rectangle();
        rectangle1.setX(rectangle.getX());
        rectangle1.setY(rectangle.getY());
        rectangle1.setHeight(rectangle.getHeight());
        rectangle1.setWidth(rectangle.getWidth());
        rectangle1.setFill(Color.BLUE);
        anchorPane.getChildren().add(rectangle1);
        System.out.println("create new rectangle");
        return rectangle1;
    }

    public Rectangle getIdSnakeHead() {
        return idSnakeHead;
    }

    public Rectangle getFood() {
        return this.food;
    }

    static public HelloController HelloController() {
        return helloController;
    }

    @FXML
    protected void onStartButtonClick() {
        newGame();
    }

    public void printText(String text) {
        Runnable task = () -> {
            Platform.runLater(() -> {
                idStatus.setText(text);
            });
        };
        Thread thread = new Thread(task);
        thread.setDaemon(true);
        thread.start();
    }

    public void printScore() {
        idScore.setText(Integer.toString(score));
    }

    public void setCoordinatesForNextRectangle(Rectangle leadingRectangle, Rectangle followingRectangle) {
        leadingRectangle.setX(followingRectangle.getX());
        leadingRectangle.setY(followingRectangle.getY());
    }

    public void setCoordinatesForSecondRectangle(Rectangle firstRectangle, Rectangle secondRectangle) {
        firstRectangle.setX(secondRectangle.getX() + 15);
        firstRectangle.setY(secondRectangle.getY() + 15);
    }

    public void replaceFood() {
        food.setX(((int) (Math.random() * 39) + 1) * 15);
        food.setY(((int) (Math.random() * 39) + 1) * 15);
    }

    private void increaseScore() {
        this.score += 100;
    }

    public void createFood() {
        this.food.setX(((int) (Math.random() * 39) + 1) * 15);
        this.food.setY(((int) (Math.random() * 39) + 1) * 15);
        this.food.setHeight(idSnakeHead.getHeight());
        this.food.setWidth(idSnakeHead.getWidth());
        this.food.setFill(Color.RED);
        anchorPane.getChildren().add(this.food);
    }


    protected void catchFood() {
        replaceFood();
        Runnable task = () -> {
            Platform.runLater(() -> {
                setNewRectangle();
                increaseScore();
                printScore();
            });
        };
        Thread thread = new Thread(task);
        thread.setDaemon(true);
        thread.start();


    }

    public void newGame() {
        HelloApplication.inputParameter = true;
        HelloApplication.actionPoint = false;
        HelloApplication.currDirection = "";
        idSnakeHead.setY(285.0);
        idSnakeHead.setX(285.0);

        for (int i = 1; i < snakeRectangles.size(); i++) {
            anchorPane.getChildren().remove(snakeRectangles.get((i)));
        }
        snakeRectangles.clear();

        snakeRectangles.add(0, idSnakeHead);
        this.score = 0;
        idStatus.setText("");

        printScore();
        replaceFood();

        System.out.println("new game");
    }

}