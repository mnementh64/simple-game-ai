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
import net.experiment.ai.simplegame.game.AutomatedGame;
import net.experiment.ai.simplegame.game.GameLevel;
import net.experiment.ai.simplegame.game.GameWorld;
import net.experiment.ai.simplegame.player.AIPlayer;
import net.experiment.ai.simplegame.player.Player;

import java.time.Duration;


public class Main extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        try {
            boolean automated = false;

            // Dialog layout
            primaryStage.setTitle("Simple game");
            Canvas canvas = new Canvas(600, 600);
            Group root = prepareLayout(canvas);
            Scene mainScene = new Scene(root);
            primaryStage.setScene(mainScene);
            primaryStage.show();

            GameWorld gameWorld = new GameWorld(mainScene, canvas, automated);

            GameLevel level1 = GameLevel.LEVEL_1;
            Player player = createPlayer(automated, gameWorld);
            gameWorld.init(player, level1);

            if (automated) {
                AutomatedGame automatedGame = new AutomatedGame(gameWorld, Duration.ofMillis(100), 100);
                automatedGame.prepare();
                automatedGame.start();
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(-1);
        }
    }

    private Player createPlayer(boolean automated, GameWorld gameWorld) throws Exception {
        Player player;
        if (automated) {
            player = new AIPlayer(gameWorld);
        } else {
            player = new Player(gameWorld);
        }
        return player;
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