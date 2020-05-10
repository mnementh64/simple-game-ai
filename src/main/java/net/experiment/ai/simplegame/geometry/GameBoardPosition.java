package net.experiment.ai.simplegame.geometry;

public class GameBoardPosition {
    public int rowIndex; // from 0
    public int colIndex; // from 0

    public GameBoardPosition(int rowIndex, int colIndex) {
        this.rowIndex = rowIndex;
        this.colIndex = colIndex;
    }

    public void up() {
        rowIndex--;
    }

    public void down() {
        rowIndex++;
    }

    public void left() {
        colIndex--;
    }

    public void right() {
        colIndex++;
    }

    public GameBoardPosition newUp() {
        return new GameBoardPosition(rowIndex - 1, colIndex);
    }

    public GameBoardPosition newDown() {
        return new GameBoardPosition(rowIndex + 1, colIndex);
    }

    public GameBoardPosition newLeft() {
        return new GameBoardPosition(rowIndex, colIndex - 1);
    }

    public GameBoardPosition newRight() {
        return new GameBoardPosition(rowIndex, colIndex + 1);
    }

    public GameBoardPosition moveTo(Vector normalizedVector) {
        return new GameBoardPosition(rowIndex + (int) normalizedVector.row, colIndex + (int) normalizedVector.col);
    }

    @Override
    public String toString() {
        return "GameBoardPosition{" +
                "rowIndex=" + rowIndex +
                ", colIndex=" + colIndex +
                '}';
    }
}
