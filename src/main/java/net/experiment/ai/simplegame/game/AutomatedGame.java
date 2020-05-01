package net.experiment.ai.simplegame.game;

import net.experiment.ai.simplegame.player.AIPlayer;
import net.experiment.ai.simplegame.player.Player;

import java.util.ArrayList;
import java.util.List;

public class AutomatedGame {

    private final GameWorld gameWorld;

    private final int maxMoves;
    private List<Player> playerList = new ArrayList<>();

    public AutomatedGame(GameWorld gameWorld, int maxMoves) {
        this.gameWorld = gameWorld;
        this.maxMoves = maxMoves;
    }

    public void preparePlayers() throws Exception {
        // create a population of AI Players
        for (int i = 0; i < 100; i++) {
            playerList.add(new AIPlayer(gameWorld, "player " + i));
        }
    }

    public void start() {
        // repeat for each AI PLayer of the population
        double bestFitness = -1.0;
        Player bestPlayer = null;
        for (Player player : playerList) {
            gameWorld.init(player, GameLevel.LEVEL_1);
            for (int i = 0; i < maxMoves; i++) {
                gameWorld.autoMovePlayer();
            }
            double fitness = player.calculateFitness();
            System.out.println("********** " + player.toString());
            System.out.println("-----> fitness : " + fitness);

            if (fitness > bestFitness) {
                bestFitness = fitness;
                bestPlayer = player;
            }

//            // run the playerAI in the game world
//            Timeline timeline = new Timeline(new KeyFrame(
//                    Duration.millis(durationBetweenRepeat.toMillis()),
//                    ae -> {
//                        gameWorld.init(player, GameLevel.LEVEL_1);
//                        gameWorld.autoMovePlayer();
//                    }));
////        timeline.setCycleCount(Animation.INDEFINITE);
//            timeline.setCycleCount(maxRepeat);
//            timeline.play();
//            timeline.setOnFinished(event -> {
//                System.out.println("********** " + player.toString());
//                // get his fitness and register it if it's the best
//
//                // reinit the level
//
//            });
        }

        // replay the best player of this generation
        System.out.println("Finally the best player is " + bestPlayer.toString() + " with a fitness " + bestFitness);


        // when we have a candidate, let's create the next population from this seed applying a genetic algorithm
        // and start again the training

    }
}
