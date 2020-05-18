package net.experiment.ai.simplegame.game;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.util.Duration;
import net.experiment.ai.simplegame.evolution.Evolutionable;
import net.experiment.ai.simplegame.player.PerceptronBrainPlayer;

public class AutomatedGame {

    private final GameWorld gameWorld;

    private final int maxMoves;
    private final Evolutionable evolutionFactory;

    public AutomatedGame(GameWorld gameWorld, int maxMoves, Evolutionable evolutionFactory) {
        this.gameWorld = gameWorld;
        this.maxMoves = maxMoves;
        this.evolutionFactory = evolutionFactory;
    }

    public void prepare() throws Exception {
        evolutionFactory.prepare();
    }

    public void start() {
        evolutionFactory.play();

        // replay best player with a timeline
        final PerceptronBrainPlayer winnerForThisGeneration = evolutionFactory.bestPlayer();
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
                evolutionFactory.evolve();

                // and start again the game
                start();
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }
}
