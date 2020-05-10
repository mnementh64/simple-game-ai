package net.experiment.ai.simplegame.game;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.util.Duration;
import net.experiment.ai.simplegame.evolution.Evolution;
import net.experiment.ai.simplegame.player.AIPlayer;
import net.experiment.ai.simplegame.player.Player;

import java.util.ArrayList;
import java.util.List;

public class AutomatedGame {

    private static final int POPULATION_SIZE = 1000;
    private final GameWorld gameWorld;

    private final int maxMoves;
    private final Evolution evolutionFactory;
    private List<Player> playerList = new ArrayList<>();
    private int numGeneration = 1;

    public AutomatedGame(GameWorld gameWorld, int maxMoves, Evolution evolutionFactory) {
        this.gameWorld = gameWorld;
        this.maxMoves = maxMoves;
        this.evolutionFactory = evolutionFactory;
    }

    public void preparePlayers() throws Exception {
        // create a population of AI Players
        playerList.clear();
        for (int i = 0; i < 1000; i++) {
            playerList.add(new AIPlayer(gameWorld, maxMoves));
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
            if (fitness > bestFitness) {
                bestFitness = fitness;
                bestPlayer = player;
            }
            if (player.getId() == evolutionFactory.getIdOfLastBestPlayerClone()) {
                System.out.println("Last best player's clone has fitness : " + fitness);
                evolutionFactory.savePlayer(player);
            }
        }

        // sort all fitnesses / display
//        String output = playerList.stream().mapToDouble(p -> p.calculateFitness()).sorted().mapToObj(String::valueOf).collect(Collectors.joining("\n"));
//        System.out.println("output = " + output);

        // replay the best player of this generation
        System.out.println("The best player is " + bestPlayer.toString() + " with a fitness " + bestFitness);
        final AIPlayer winnerForThisGeneration = (AIPlayer) bestPlayer;
        gameWorld.startReplayFor(winnerForThisGeneration);
        Timeline timeline = new Timeline(new KeyFrame(
                Duration.millis(15),
                ae -> {
                    gameWorld.replayNextMove(winnerForThisGeneration);
                }));
        timeline.setCycleCount(maxMoves);
        timeline.play();
        timeline.setOnFinished(event -> {
            gameWorld.stopReplayFor(winnerForThisGeneration);

            try {
                // create players for the next generation
                numGeneration++;
                System.out.println("*************************************************\nNew generation " + numGeneration);
                List<Player> playersForNextGeneration = evolutionFactory.naturalSelection(playerList, POPULATION_SIZE);

                // activate this generation
                playerList.clear();
                playerList.addAll(playersForNextGeneration);

                // and start the simulation
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
    }
}
