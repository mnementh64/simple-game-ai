package net.experiment.ai.simplegame;

import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.KeyCode;

public class GameWorld {

    private final Scene mainScene;
    private final Player player;
    private final GraphicsContext gc;
    private final int width;
    private final int height;

    private GameLevel gameLevel;

    public GameWorld(Scene mainScene, Player player, Canvas canvas, GameLevel gameLevel) {
        this.mainScene = mainScene;
        this.player = player;
        this.gc = canvas.getGraphicsContext2D();
        this.width = new Double(canvas.getWidth()).intValue();
        this.height = new Double(canvas.getHeight()).intValue();
        this.gameLevel = gameLevel;
    }

    public void init() {
        render();
        mainScene.setOnKeyPressed(
                keyEvent -> {
                    if (keyEvent.getCode().equals(KeyCode.UP)) {
                        if (gameLevel.allowPositionToPlayer(player.getPosition().newUp())) {
                            player.up();
                        }
                    }
                    if (keyEvent.getCode().equals(KeyCode.DOWN)) {
                        if (gameLevel.allowPositionToPlayer(player.getPosition().newDown())) {
                            player.down();
                        }
                    }
                    if (keyEvent.getCode().equals(KeyCode.LEFT)) {
                        if (gameLevel.allowPositionToPlayer(player.getPosition().newLeft())) {
                            player.left();
                        }
                    }
                    if (keyEvent.getCode().equals(KeyCode.RIGHT)) {
                        if (gameLevel.allowPositionToPlayer(player.getPosition().newRight())) {
                            player.right();
                        }
                    }
                    render();
                });
    }

    private void render() {
        gameLevel.render(gc, width, height);
        player.render(gc, width, height, gameLevel.getTileSize());
    }
}
