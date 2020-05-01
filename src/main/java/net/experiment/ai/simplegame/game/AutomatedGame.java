package net.experiment.ai.simplegame.game;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.util.Duration;

public class AutomatedGame {

    private final GameWorld gameWorld;
    private final java.time.Duration durationBetweenRepeat;

    private final int maxRepeat;

    public AutomatedGame(GameWorld gameWorld, java.time.Duration durationBetweenRepeat, int maxRepeat) {
        this.gameWorld = gameWorld;
        this.durationBetweenRepeat = durationBetweenRepeat;
        this.maxRepeat = maxRepeat;
    }

    public void start() {
        Timeline timeline = new Timeline(new KeyFrame(
                Duration.millis(durationBetweenRepeat.toMillis()),
                ae -> gameWorld.autoMovePlayer()));
//        timeline.setCycleCount(Animation.INDEFINITE);
        timeline.setCycleCount(maxRepeat);
        timeline.play();
    }
}
