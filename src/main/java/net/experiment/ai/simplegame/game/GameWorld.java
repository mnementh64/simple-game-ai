package net.experiment.ai.simplegame.game;

import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.KeyCode;
import javafx.scene.paint.Color;
import net.experiment.ai.simplegame.Player;

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
                    } else if (keyEvent.getCode().equals(KeyCode.DOWN)) {
                        if (gameLevel.allowPositionToPlayer(player.getPosition().newDown())) {
                            player.down();
                        }
                    } else if (keyEvent.getCode().equals(KeyCode.LEFT)) {
                        if (gameLevel.allowPositionToPlayer(player.getPosition().newLeft())) {
                            player.left();
                        }
                    } else if (keyEvent.getCode().equals(KeyCode.RIGHT)) {
                        if (gameLevel.allowPositionToPlayer(player.getPosition().newRight())) {
                            player.right();
                        }
                    }

                    // adapt both game level / player to what happen in the new position
                    gameLevel.manageConsequences(player);
                    render();

                    onPlayerWin();
                });
    }

    private void onPlayerWin() {
        if (player.isWin()) {
            GameLevel nextLevel = GameLevel.LEVEL_2;
            nextLevel.reinit();
            init(new Player(nextLevel.getStartPosition()), nextLevel);
        }
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
        double y = (gameLevel.getNumberOfRows() + 1) * gameLevel.getTileSize();
        gc.strokeText("Score : " + player.getScore(), 10, y);
        gc.strokeText("Nb of moves : " + player.getNbMoves(), 200, y);
        if (player.isWin()) {
            gc.strokeText("WIN !", 400, y);
        }
    }
}
