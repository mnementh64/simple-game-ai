package net.experiment.ai.simplegame.game;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.util.Duration;
import net.experiment.ai.simplegame.player.AIPlayer;
import net.experiment.ai.simplegame.player.Player;

import java.util.ArrayList;
import java.util.List;

public class AutomatedGame {

    private final GameWorld gameWorld;
    private final java.time.Duration durationBetweenRepeat;

    private final int maxRepeat;
    private List<Player> playerList = new ArrayList<>();

    public AutomatedGame(GameWorld gameWorld, java.time.Duration durationBetweenRepeat, int maxRepeat) {
        this.gameWorld = gameWorld;
        this.durationBetweenRepeat = durationBetweenRepeat;
        this.maxRepeat = maxRepeat;
    }

    public void prepare() {
        // create a population of AI Players
//        Player player = new AIPlayer(gameWorld);
    }

    public void start() {
        // repeat for each AI PLayer of the population

        // run the playerAI in the game world

        // get his fitness and register it if it's the best

        // reinit the level


        // when we have a candidate, let's create the next population from this seed applying a genetic algorithm
        // and start again the training

        Timeline timeline = new Timeline(new KeyFrame(
                Duration.millis(durationBetweenRepeat.toMillis()),
                ae -> gameWorld.autoMovePlayer()));
//        timeline.setCycleCount(Animation.INDEFINITE);
        timeline.setCycleCount(maxRepeat);
        timeline.play();
//        timeline.setOnFinished();
    }
}
