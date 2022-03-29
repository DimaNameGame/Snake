package com.example.demo;

import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;

import javafx.scene.input.KeyEvent;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.List;

public class HelloApplication extends Application {
    static private Scene scene;
    private Stage stage;
    private HelloController helloController;
    public static String currDirection = "";
    public static boolean inputParameter = false;
    public static boolean actionPoint = false;
    List<Rectangle> snakeRectangles;


    @Override
    public void start(Stage stage) throws IOException, InterruptedException {


        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("hello-view.fxml"));
        scene = new Scene(fxmlLoader.load(), 720, 800);
        stage.setTitle("Snake");
        helloController = HelloController.HelloController();
        snakeRectangles = helloController.getSnakeRectangles();
        keyListener();
        stage.setScene(scene);
        stage.show();
        System.out.println("outside : " + Thread.currentThread().getId());
        move();
        System.out.println(Thread.currentThread().getId());
        stage.setOnCloseRequest(event -> {
            System.exit(0);
        });
        this.stage = stage;
    }

    public void keyListener() {

        scene.setOnKeyReleased(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent event) {
                if (actionPoint)
                    return;
                inputParameter = false;
                String previosDirection = currDirection;
                switch (event.getCode()) {
                    case UP:
                        if (previosDirection.equals("DOWN")) {
                            currDirection = previosDirection;
                            break;
                        }
                        currDirection = "UP";
                        actionPoint = true;
                        break;
                    case DOWN:
                        if (previosDirection.equals("UP")) {
                            currDirection = previosDirection;
                            break;
                        }
                        currDirection = "DOWN";
                        actionPoint = true;
                        break;
                    case LEFT:
                        if (previosDirection.equals("RIGHT")) {
                            currDirection = previosDirection;
                            break;
                        }
                        currDirection = "LEFT";
                        actionPoint = true;
                        break;
                    case RIGHT:
                        if (previosDirection.equals("LEFT")) {
                            currDirection = previosDirection;
                            break;
                        }
                        currDirection = "RIGHT";
                        actionPoint = true;
                        break;
                }
            }
        });
    }

    public void move() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                System.out.println("inside : " + Thread.currentThread().getId());
                while (true) {
                    checkCatchFood(helloController.getIdSnakeHead(), helloController.getFood());
                    checkHitTail(snakeRectangles);
                    checkStatus(snakeRectangles);
                    movingTail(snakeRectangles);
                    movingHead(helloController.getIdSnakeHead());
                    actionPoint = false;
                    try {
                        Thread.currentThread().sleep(100);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }


                }
            }
        }).start();
    }

    private void movingTail(List<Rectangle> snakeRectangles) {
        Rectangle leadingRectangle;
        Rectangle followingRectangle;
        for (int i = 0; i + 1 < snakeRectangles.size(); i++) {
            leadingRectangle = snakeRectangles.get(snakeRectangles.size() - (i + 1));
            followingRectangle = snakeRectangles.get(snakeRectangles.size() - (i + 2));
            if (snakeRectangles.size() - (i + 2) == 0)
                helloController.setCoordinatesForSecondRectangle(leadingRectangle, followingRectangle);
            else
                helloController.setCoordinatesForNextRectangle(leadingRectangle, followingRectangle);
        }
    }

    private void movingHead(Rectangle head) {
        switch (currDirection) {
            case "UP":
                head.setY(checkBorder(head.getY() - 15));
                break;
            case "DOWN":
                head.setY(checkBorder(head.getY() + 15));
                break;
            case "LEFT":
                head.setX(checkBorder(head.getX() - 15));
                break;
            case "RIGHT":
                head.setX(checkBorder(head.getX() + 15));
                break;
        }
    }

    private static double checkBorder(double coordinate) {
        if (coordinate <= -15)
            coordinate = 570;
        if (coordinate >= 585)
            coordinate = 0;
        return coordinate;
    }

    private void checkCatchFood(Rectangle head, Rectangle food) {
        if (head.getX() + 15 == food.getX() && head.getY() + 15 == food.getY())
            helloController.catchFood();
    }

    private void checkHitTail(List<Rectangle> snakeRectangles) {
        for (int i = 1; i < snakeRectangles.size(); i++) {
            if (snakeRectangles.get(0).getY() + 15 == snakeRectangles.get(i).getY() && snakeRectangles.get(0).getX() + 15 == snakeRectangles.get(i).getX()) {
                inputParameter = (true);
                System.out.println("YOU LOSE");
                helloController.printText("YOU LOSE");
            }
        }
        if (snakeRectangles.size() >= 39 * 39) {
            inputParameter = (true);

            System.out.println("YOU WIN");
            helloController.printText("YOU WIN");
        }
    }

    private void checkStatus(List<Rectangle> snakeRectangles) {
        while (this.inputParameter) {
            try {
                Thread.currentThread().sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            checkHitTail(snakeRectangles);
        }
    }

    public static void main(String[] args) {
        launch();
    }

}