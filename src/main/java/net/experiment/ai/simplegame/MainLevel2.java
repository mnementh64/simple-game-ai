package net.experiment.ai.simplegame;

import com.fasterxml.jackson.databind.ObjectMapper;
import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.stage.Stage;
import net.experiment.ai.simplegame.brain.NNBrain;
import net.experiment.ai.simplegame.game.GameLevel;
import net.experiment.ai.simplegame.game.GameWorld;
import net.experiment.ai.simplegame.player.AIPlayer;
import net.mnementh64.neural.Network;

import java.io.File;

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

            ObjectMapper mapper = new ObjectMapper();
            Network network = mapper.readValue(new File("/Users/sylvaincaillet/Downloads/player-106420.json"), Network.class);
            NNBrain brain = new NNBrain(gameWorld, network);
            AIPlayer player = new AIPlayer(gameWorld, 100, brain);
            gameWorld.init(player, GameLevel.LEVEL_3);
            for (int i = 0; i < 100; i++) {
                boolean win = gameWorld.autoMovePlayer();
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

