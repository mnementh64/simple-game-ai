package net.experiment.ai.simplegame.game;

import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.KeyCode;
import javafx.scene.paint.Color;
import net.experiment.ai.simplegame.player.AIPlayer;
import net.experiment.ai.simplegame.player.AutomatedPlayer;
import net.experiment.ai.simplegame.player.Player;

public class GameWorld {

    public enum Direction {
        UP, DOWN, LEFT, RIGHT;
    }

    private final Scene mainScene;
    private final GraphicsContext gc;
    private final int width;
    private final int height;
    private boolean automatedGame;
    private boolean replay = false;

    private Player player;
    private GameLevel gameLevel;

    public GameWorld(Scene mainScene, Canvas canvas, boolean automatedGame) {
        this.mainScene = mainScene;
        this.gc = canvas.getGraphicsContext2D();
        this.width = new Double(canvas.getWidth()).intValue();
        this.height = new Double(canvas.getHeight()).intValue();
        this.automatedGame = automatedGame;
    }

    public void init(Player player, GameLevel gameLevel) {
        // reset player's start position according to the level
        player.setStartPosition(gameLevel.getStartPosition());

        this.player = player;
        this.gameLevel = gameLevel;
        render();
    }

    public void autoMovePlayer() {
        try {
            Direction direction = ((AutomatedPlayer) player).computeNextMove();
            playerAskToMove(direction);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void initKeyHandler() {
        mainScene.setOnKeyPressed(
                keyEvent -> {
                    Direction direction = null;
                    if (keyEvent.getCode().equals(KeyCode.UP)) {
                        direction = Direction.UP;
                    } else if (keyEvent.getCode().equals(KeyCode.DOWN)) {
                        direction = Direction.DOWN;
                    } else if (keyEvent.getCode().equals(KeyCode.LEFT)) {
                        direction = Direction.LEFT;
                    } else if (keyEvent.getCode().equals(KeyCode.RIGHT)) {
                        direction = Direction.RIGHT;
                    }

                    if (direction != null) {
                        playerAskToMove(direction);
                    }
                });
    }

    public void startReplayFor(AIPlayer bestPlayer) {
        this.replay = true;
        this.player = bestPlayer;
        gameLevel.reinit();
        bestPlayer.startReplay(gameLevel.getStartPosition());
    }

    public void replayNextMove(AIPlayer player) {
        Direction direction = player.nextReplayDirection();
        playerAskToMove(direction);
    }

    public void stopReplayFor(AIPlayer bestPlayer) {
        this.replay = false;
        bestPlayer.stopReplay();
    }

    private void playerAskToMove(Direction direction) {
        System.out.println("Player asked to move to " + direction);
        switch (direction) {
            case UP:
                if (gameLevel.allowPositionToPlayer(player.getPosition().newUp())) {
                    player.up();
                }
                break;
            case DOWN:
                if (gameLevel.allowPositionToPlayer(player.getPosition().newDown())) {
                    player.down();
                }
                break;
            case LEFT:
                if (gameLevel.allowPositionToPlayer(player.getPosition().newLeft())) {
                    player.left();
                }
                break;
            case RIGHT:
                if (gameLevel.allowPositionToPlayer(player.getPosition().newRight())) {
                    player.right();
                }
                break;
        }
        player.askedToMove(direction); // count the intention to move even if the move is impossible

        // adapt both game level / player to what happen in the new position
        gameLevel.manageConsequences(player);
        render();

        if (!replay) {
            onPlayerWin();
        }
    }

    private void onPlayerWin() {
        if (player.isWin()) {
            GameLevel nextLevel = GameLevel.LEVEL_2;
            nextLevel.reinit();
            player.reinit();

            init(player, nextLevel);
        }
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
        if (player.isWin()) {
            gc.strokeText("WIN !", 200, y);
        }

        double y2 = y + gameLevel.getTileSize();
        gc.strokeText("Nb of moves : " + player.getNbMoves(), 10, y2);
        gc.strokeText("Total nb of moves : " + player.getCumulativeNbMoves(), 200, y2);
    }

    public GameLevel getGameLevel() {
        return gameLevel;
    }
}
