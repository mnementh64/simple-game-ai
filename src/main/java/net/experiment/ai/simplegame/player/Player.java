package net.experiment.ai.simplegame.player;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import net.experiment.ai.simplegame.game.GameBoardPosition;
import net.experiment.ai.simplegame.game.GameWorld;

public class Player {
    // environment properties
    protected GameWorld gameWorld;
    protected GameBoardPosition position;

    // runtime properties
    private int score;
    private boolean win = false;
    private int nbMoves = 0;
    private int cumulativeNbMoves = 0;

    public Player(GameWorld gameWorld, GameBoardPosition startPosition) {
        this.gameWorld = gameWorld;
        this.position = startPosition;
    }

    public void render(GraphicsContext gc, int width, int height, int tileSize) {
        double centerOfCellX = (position.colIndex + 0.5) * tileSize;
        double centerOfCellY = (position.rowIndex + 0.5) * tileSize;

        if (!win) {
            gc.setFill(Color.GREENYELLOW);
        } else {
            gc.setFill(Color.YELLOW);
        }
        gc.fillOval(centerOfCellX - 5, centerOfCellY - 5, 10, 10);
    }

    public void up() {
        position.up();
        nbMoves++;
        cumulativeNbMoves++;
    }

    public void down() {
        position.down();
        nbMoves++;
        cumulativeNbMoves++;
    }

    public void left() {
        position.left();
        nbMoves++;
        cumulativeNbMoves++;
    }

    public void right() {
        position.right();
        nbMoves++;
        cumulativeNbMoves++;
    }

    /**
     * Reinint internal player properties for next level
     */
    public void reinit() {
        nbMoves = 0;
        // do not reinit cumulativeNbMoves
        win = false;
    }

    public void setStartPosition(GameBoardPosition position) {
        this.position = position;
    }

    public GameBoardPosition getPosition() {
        return new GameBoardPosition(position.rowIndex, position.colIndex);
    }

    public void addToScore(int value) {
        score += value;
    }

    public void win() {
        win = true;
    }

    public int getScore() {
        return score;
    }

    public boolean isWin() {
        return win;
    }

    public int getNbMoves() {
        return nbMoves;
    }

    public int getCumulativeNbMoves() {
        return cumulativeNbMoves;
    }
}
