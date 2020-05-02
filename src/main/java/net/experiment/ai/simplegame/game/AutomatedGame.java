package net.experiment.ai.simplegame.game;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.util.Duration;
import net.experiment.ai.simplegame.player.AIPlayer;
import net.experiment.ai.simplegame.player.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

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
        playerList.clear();
        for (int i = 0; i < 1000; i++) {
            playerList.add(new AIPlayer(gameWorld, "player " + i, maxMoves));
        }
    }

    public void start() {
        // repeat for each AI PLayer of the population
        double bestFitness = -1.0;
        Player bestPlayer = null;
        for (Player player : playerList) {
            gameWorld.init(player, GameLevel.LEVEL_1);
            for (int i = 0; i < maxMoves; i++) {
                boolean win = gameWorld.autoMovePlayer();
                if (win) {
                    break;
                }
            }
            double fitness = player.calculateFitness();
            System.out.println("********** " + player.toString());
            System.out.println("-----> fitness : " + fitness);

            if (fitness > bestFitness) {
                bestFitness = fitness;
                bestPlayer = player;
            }
        }

        // sort all fitnesses / display
        String output = playerList.stream().mapToDouble(p -> p.calculateFitness()).sorted().mapToObj(String::valueOf).collect(Collectors.joining("\n"));
        System.out.println("output = " + output);

        // replay the best player of this generation
        System.out.println("Finally the best player is " + bestPlayer.toString() + " with a fitness " + bestFitness);
        final AIPlayer winnerForThisGeneration = (AIPlayer) bestPlayer;
        gameWorld.startReplayFor(winnerForThisGeneration);
        Timeline timeline = new Timeline(new KeyFrame(
                Duration.millis(100),
                ae -> {
                    gameWorld.replayNextMove(winnerForThisGeneration);
                }));
        timeline.setCycleCount(maxMoves);
        timeline.play();
        timeline.setOnFinished(event -> {
            System.out.println("********** End of replay ***********");
            gameWorld.stopReplayFor(winnerForThisGeneration);

            try {
                preparePlayers();
                start();
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

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

        // when we have a candidate, let's create the next population from this seed applying a genetic algorithm
        // and start again the training

    }
}
