package net.experiment.ai.simplegame;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class Player {
    private GameBoardPosition position;
    private int score;
    private boolean win = false;
    private int nbMoves = 0;

    public Player(GameBoardPosition startPosition) {
        this.position = startPosition;
    }

    public void render(GraphicsContext gc, int width, int height, int tileSize) {
        double centerOfCellX = (position.colIndex + 0.5) * tileSize;
        double centerOfCellY = (position.rowIndex + 0.5) * tileSize;

        gc.setFill(Color.GREENYELLOW);
        gc.fillOval(centerOfCellX - 5, centerOfCellY - 5, 10, 10);
    }

    public void up() {
        position.up();
        nbMoves++;
    }

    public void down() {
        position.down();
        nbMoves++;
    }

    public void left() {
        position.left();
        nbMoves++;
    }

    public void right() {
        position.right();
        nbMoves++;
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
}
