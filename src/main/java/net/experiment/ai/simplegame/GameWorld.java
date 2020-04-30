package net.experiment.ai.simplegame;

import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.KeyCode;
import javafx.scene.paint.Color;

public class GameWorld {

    private final Scene mainScene;
    private final GraphicsContext gc;
    private final int width;
    private final int height;

    private Player player;
    private GameLevel gameLevel;

    public GameWorld(Scene mainScene, Canvas canvas) {
        this.mainScene = mainScene;
        this.gc = canvas.getGraphicsContext2D();
        this.width = new Double(canvas.getWidth()).intValue();
        this.height = new Double(canvas.getHeight()).intValue();
        initKeyHandler();
    }

    private void initKeyHandler() {
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

                    gameLevel.manageConsequences(player);
                    render();

                    if (player.isWin()) {
                        try {
                            Thread.sleep(3000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        GameLevel nextLevel = GameLevel.LEVEL_2;
                        nextLevel.reinit();
                        init(new Player(nextLevel.getStartPosition()), nextLevel);
                    }
                });
    }

    public void init(Player player, GameLevel gameLevel) {
        this.player = player;
        this.gameLevel = gameLevel;

        render();
    }

    private void render() {
        gameLevel.render(gc, width, height);
        player.render(gc, width, height, gameLevel.getTileSize());
        renderScore();
    }

    private void renderScore() {
        gc.setStroke(Color.BLACK);
        gc.strokeText("Score : " + player.getScore(), 10, (gameLevel.getNumberOfRows() + 1) * gameLevel.getTileSize());
        if (player.isWin()) {
            gc.strokeText("WIN !", 400, (gameLevel.getNumberOfRows() + 1) * gameLevel.getTileSize());
        }
    }
}
