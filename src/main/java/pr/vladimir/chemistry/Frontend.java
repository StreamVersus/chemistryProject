package pr.vladimir.chemistry;

import javafx.application.Application;
import javafx.concurrent.Task;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleButton;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import pr.vladimir.chemistry.API.*;
import pr.vladimir.chemistry.Tiles.*;

import java.util.InputMismatchException;
import java.util.Objects;
import java.util.Scanner;

import static pr.vladimir.chemistry.Backend.formulaMatrix;

public class Frontend extends Application {

    public static Canvas canvas;
    public static ToggleButton carbonButton, funcButton;
    public static Integer state = 0;
    public static TextField field;
    private final Backend backend;
    public static final int gridSize = 50;
    public static final Vector2D screenSize = new Vector2D(1202, 892);
    public static boolean isVerbose = false;
    private static boolean inputLocked = false;
    public static Integer preset = -1;

    public Frontend() {
        this.backend = new Backend();
    }

    @Override
    public void start(Stage stage) {
        Pane pane = new Pane();
        Scene scene = new Scene(pane, screenSize.getX(), screenSize.getY());
        stage.setTitle("Organic Chemistry");
        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();

        canvas = new Canvas();
        pane.getChildren().add(canvas);
        canvas.setOnMouseClicked(this::handleMouseClicked);
        canvas.setWidth(screenSize.getX());
        canvas.setHeight(screenSize.getY());
        canvas.setLayoutY(40);

        pane.minWidth(screenSize.getX());
        pane.minHeight(screenSize.getY());
        pane.maxWidth(screenSize.getX());
        pane.maxHeight(screenSize.getY());

        field = new TextField();
        field.setLayoutX(7);
        field.setLayoutY(7);
        field.setMinWidth(300);
        pane.getChildren().add(field);

        Button computeButton = new Button("Вычислить");
        computeButton.setFocusTraversable(false);
        pane.getChildren().add(computeButton);
        computeButton.setMinWidth(75);
        computeButton.setLayoutX(315);
        computeButton.setLayoutY(7);
        computeButton.setOnAction(_ -> Logic.treeAnalysis());

        carbonButton = new ToggleButton("Углерод");
        pane.getChildren().add(carbonButton);
        carbonButton.setFocusTraversable(false);
        carbonButton.setMinWidth(70);
        carbonButton.setLayoutX(410);
        carbonButton.setLayoutY(7);
        carbonButton.setSelected(true);
        carbonButton.setOnAction(_ -> {
            if(!carbonButton.isSelected()) {
                carbonButton.setSelected(true);
                return;
            }
            funcButton.setSelected(false);

            state = 0;
        });

        funcButton = new ToggleButton("Функциональные группы");
        pane.getChildren().add(funcButton);
        funcButton.setFocusTraversable(false);
        funcButton.setMinWidth(50);
        funcButton.setLayoutX(490);
        funcButton.setLayoutY(7);
        funcButton.setOnAction(_ -> {
            if(!funcButton.isSelected()) {
                funcButton.setSelected(true);
                return;
            }
            carbonButton.setSelected(false);

            state = 1;
        });
        drawGrid(canvas.getGraphicsContext2D());

        Presets.setPreset(preset);
    }

    public static void main(String[] args) {
        if(args.length != 0) if(Objects.equals(args[0], "-v")) isVerbose = true;
        Scanner scan = new Scanner(System.in);
        System.out.print("Введите номер пресета[0-5]: ");

        try {
            preset = scan.nextInt();
        } catch (InputMismatchException e) {
            System.out.print("Неверное значение: принято стандартное значение null");
        }

        launch();
    }

    private void handleMouseClicked(MouseEvent event) {
        if(inputLocked) return;

        inputLocked = true;
        double mouseX = event.getX();
        double mouseY = event.getY();
        var boxVec = trackToGrid(mouseX, mouseY);

        if(event.getButton() == MouseButton.SECONDARY) {
            backend.handleRMB(boxVec);
        }
        else if (event.getButton() == MouseButton.PRIMARY) {
            backend.handleLMB(boxVec);
        }

        for (GridElement[] array : formulaMatrix) {
            for (GridElement gridElement : array) {
                if(gridElement == null) continue;
                gridElement.canUpdate();
            }
        }
        delay(10, () -> inputLocked = false);
    }

    private void drawGrid(GraphicsContext gc) {
        double width = canvas.getWidth();
        double height = canvas.getHeight();

        for (double x = 1; x < width; x += gridSize) {
            gc.setStroke(Color.GRAY);
            gc.strokeLine(x, 0, x, height);
        }

        for (double y = 1; y < height; y += gridSize) {
            gc.setStroke(Color.GRAY);
            gc.strokeLine(0, y, width, y);
        }
    }

    public Vector2D trackToGrid(double x, double y) {
        return new Vector2D(Math.floor(x / gridSize), Math.floor(y / gridSize));
    }

    public static void delay(long millis, Runnable continuation) {
        Task<Void> sleeper = new Task<>() {
            @Override
            protected Void call() {
                try { Thread.sleep(millis); } catch (InterruptedException _) {}
                return null;
            }
        };
        sleeper.setOnSucceeded(_ -> continuation.run());
        new Thread(sleeper).start();
    }

    public static void renderName(String name) {
        field.setText(name);
    }
}