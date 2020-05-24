package net.experiment.ai.simplegame;

import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.stage.Stage;
import net.experiment.ai.simplegame.game.GameLevel;
import net.experiment.ai.simplegame.game.GameWorld;
import net.experiment.ai.simplegame.player.PerceptronBrainPlayer;

public class MainLevel2 extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        try {
            boolean automated = true;

            // Dialog layout
            primaryStage.setTitle("Simple game");
            Canvas canvas = new Canvas(600, 600);
            Group root = new Group();
            Scene mainScene = new Scene(root);
            primaryStage.setScene(mainScene);
            primaryStage.show();

            GameWorld gameWorld = new GameWorld(mainScene, canvas, automated);
            PerceptronBrainPlayer player = new PerceptronBrainPlayer(gameWorld, Main.MAX_MOVES, "/Users/sylvaincaillet/Downloads/player-33083.json");
            gameWorld.init(player, GameLevel.LEVEL_2);
            gameWorld.render();
            for (int i = 0; i < Main.MAX_MOVES; i++) {
                boolean win = gameWorld.autoMovePlayer().win;
                if (win) {
                    break;
                }
            }
            double fitness = player.calculateFitness();
            System.out.println("Score : " + player.getScore() + " / Fitnesss : " + fitness);

        } catch (Exception e) {
            e.printStackTrace();
            System.exit(-1);
        }
    }
}

