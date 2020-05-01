package net.experiment.ai.simplegame.game;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.util.Duration;

public class AutomatedGame {

    private final GameWorld gameWorld;
    private final java.time.Duration durationBetweenRepeat;

    public AutomatedGame(GameWorld gameWorld, java.time.Duration durationBetweenRepeat) {
        this.gameWorld = gameWorld;
        this.durationBetweenRepeat = durationBetweenRepeat;
    }

    public void start() {
        Timeline timeline = new Timeline(new KeyFrame(
                Duration.millis(durationBetweenRepeat.toMillis()),
                ae -> {
                    gameWorld.start();
                }));
        timeline.setCycleCount(Animation.INDEFINITE);
        timeline.play();
    }
}
