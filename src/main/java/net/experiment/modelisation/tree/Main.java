package net.experiment.modelisation.tree;

import javafx.application.Application;
import javafx.concurrent.Task;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.stage.Stage;

public class Main extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Simple game");
        Group root = new Group();
        Canvas canvas = new Canvas(600, 600);
        GraphicsContext gc = canvas.getGraphicsContext2D();

        GameTask task = new GameTask(gc);
        final Thread taskThread = new Thread(task, "simulation");
        taskThread.setDaemon(true);

        root.getChildren().add(canvas);
        primaryStage.setScene(new Scene(root));
        primaryStage.show();

        taskThread.start();
    }

    private static final class GameTask extends Task<Void> {

        private final GraphicsContext gc;

        public GameTask(GraphicsContext gc) {
            this.gc = gc;
        }

        @Override
        protected Void call() throws Exception {
            return null;
        }
    }
}