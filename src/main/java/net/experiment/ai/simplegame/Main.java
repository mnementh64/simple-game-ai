package net.experiment.ai.simplegame;

import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderStroke;
import javafx.scene.layout.BorderStrokeStyle;
import javafx.scene.layout.BorderWidths;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;


public class Main extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Simple game");

        Canvas canvas = new Canvas(600, 600);
        Group root = prepareLayout(canvas);

        Scene mainScene = new Scene(root);
        GameLevel level1 = GameLevel.LEVEL_1;
        GameWorld gameWorld = new GameWorld(mainScene, new Player(level1.getStartPosition()), canvas, level1);
        gameWorld.init();

        primaryStage.setScene(mainScene);
        primaryStage.show();
    }

    private Group prepareLayout(Canvas canvas) {
        Group root = new Group();

        Button button1 = new Button("Button2");
        Button button2 = new Button("Button2");
        Label label1 = new Label("The label");
        VBox vBoxLeft = new VBox(button1, button2, label1);
        vBoxLeft.setBorder(new Border(new BorderStroke(Color.BLACK,
                BorderStrokeStyle.SOLID, CornerRadii.EMPTY, BorderWidths.DEFAULT)));
        vBoxLeft.setMinWidth(200);

        HBox hBox = new HBox(vBoxLeft, canvas);
        hBox.setPrefWidth(800);
        hBox.setPrefHeight(600);
        root.getChildren().add(hBox);

        return root;
    }
}