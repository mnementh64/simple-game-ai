package net.experiment.ai.simplegame.game;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.util.Duration;

public class AutomatedGame {

    private final GameWorld gameWorld;
    private final java.time.Duration durationBetweenRepeat;

    private Timeline timeline;
    private final int maxRepeat;
    private int nbRepeats;

    public AutomatedGame(GameWorld gameWorld, java.time.Duration durationBetweenRepeat, int maxRepeat) {
        this.gameWorld = gameWorld;
        this.durationBetweenRepeat = durationBetweenRepeat;
        this.maxRepeat = maxRepeat;
    }

    public void start() {
        timeline = new Timeline(new KeyFrame(
                Duration.millis(durationBetweenRepeat.toMillis()),
                ae -> {
                    gameWorld.autoMovePlayer();
                    nbRepeats++;
                    if (nbRepeats > maxRepeat) {
                        stop();
                    }
                }));
        timeline.setCycleCount(Animation.INDEFINITE);
        timeline.play();
    }

    private void stop() {
        timeline.stop();
    }
}
