package pr.vladimir.demo1;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import pr.vladimir.demo1.Tiles.GridElement;

import java.io.IOException;

import static pr.vladimir.demo1.Backend.formulaMatrix;

public class Frontend extends Application {

    public static Canvas canvas;
    private final Backend backend;
    public static final int gridSize = 50;
    public static final Vector2D screenSize = new Vector2D(1200, 900);

    public Frontend() {
        this.backend = new Backend();
    }

    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Frontend.class.getResource("project.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), screenSize.getX(), screenSize.getY());
        stage.setTitle("Hello!");
        stage.setScene(scene);
        stage.show();

        canvas = (Canvas) scene.lookup("#canvas");
        canvas.setOnMouseClicked(this::handleMouseClicked);

        drawGrid(canvas.getGraphicsContext2D());
    }

    public static void main(String[] args) {
        launch();
    }

    // Handle mouse click event
    private void handleMouseClicked(MouseEvent event) {
        double mouseX = event.getX() + canvas.getLayoutX();
        double mouseY = event.getY() + canvas.getLayoutY();
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
    }

    private void drawGrid(GraphicsContext gc) {
        double width = canvas.getWidth();
        double height = canvas.getHeight();

        // Draw vertical lines
        for (double x = 0; x < width; x += gridSize) {
            gc.setStroke(Color.GRAY);
            gc.strokeLine(x, 0, x, height);
        }

        // Draw horizontal lines
        for (double y = 0; y < height; y += gridSize) {
            gc.setStroke(Color.GRAY);
            gc.strokeLine(0, y, width, y);
        }
    }

    public Vector2D trackToGrid(double x, double y) {
        return new Vector2D(Math.floor(x / gridSize), Math.floor(y / gridSize));
    }
}