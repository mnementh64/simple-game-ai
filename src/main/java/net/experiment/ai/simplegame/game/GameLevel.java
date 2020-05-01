package net.experiment.ai.simplegame.game;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import net.experiment.ai.simplegame.player.Player;

public class GameLevel {
    private final String levelAsString;
    private final int numberOfRows;

    private enum CELL_TYPE {
        EMPTY(0),
        WALL(1),
        DIAMOND(2),
        OUTPUT(3);

        private final int code;

        CELL_TYPE(int code) {
            this.code = code;
        }

        public CELL_TYPE getByCode(int code) {
            for (CELL_TYPE type : CELL_TYPE.values()) {
                if (type.code == code) {
                    return type;
                }
            }
            return null;
        }
    }

    public static final int TILE_SIZE = 20;

    public static final GameLevel LEVEL_1 = new GameLevel(
            "" +
                    "WWWWWWWWWWWWWWOWWWWW" +
                    "W..................W" +
                    "W..................W" +
                    "W..................W" +
                    "W...D..............W" +
                    "W..................W" +
                    "W..................W" +
                    "W..................W" +
                    "W...........D......W" +
                    "W....D.............W" +
                    "W..................W" +
                    "W..................W" +
                    "W..................W" +
                    "WWWWWWWWWWWWWWWWWWWW"
            , 14, 20, 3, 1, 1
    );
    public static final GameLevel LEVEL_2 = new GameLevel(
            "" +
                    "WWWWWWWOWWWWWWWWWWWWWWWWWW" +
                    "W........W...............W" +
                    "W........W...............W" +
                    "W........W..........D....W" +
                    "W........W...............W" +
                    "W...D....W...............W" +
                    "W....WWWWWWWWW...........W" +
                    "W............W...........W" +
                    "W............W...........W" +
                    "W............W...........W" +
                    "W............W...........W" +
                    "W............W....D......W" +
                    "W....D.......WWWWWW......W" +
                    "W..............D..W......W" +
                    "W............WWWWWW......W" +
                    "W.......................DW" +
                    "WWWWWWWWWWWWWWWWWWWWWWWWWW"
            , 17, 26, 6, 10, 10
    );

    private final int[][] levelMatrix;
    private final int nbDiamonds;
    private final int numberOfColumns;
    private final GameBoardPosition startPosition;

    private boolean outputReleased = false;
    private int nbDiamondsFound = 0;

    public GameLevel(String levelAsString, int numberOfRows, int numberOfColumns, int nbDiamonds, int startPlayerRow, int startPlayerCol) {
        this.levelAsString = levelAsString;
        this.numberOfRows = numberOfRows;
        this.numberOfColumns = numberOfColumns;
        this.levelMatrix = new int[numberOfRows][numberOfColumns];
        this.nbDiamonds = nbDiamonds;
        this.startPosition = new GameBoardPosition(startPlayerRow, startPlayerCol);

        initLevelMatrix(levelAsString);
    }

    public void reinit() {
        initLevelMatrix(levelAsString);
        outputReleased = false;
        nbDiamondsFound = 0;
    }

    private void initLevelMatrix(String levelAsString) {
        int rowIndex = 0;
        int colIndex = 0;
        for (char cell : levelAsString.toCharArray()) {
            switch (cell) {
                case 'W':
                    levelMatrix[rowIndex][colIndex] = CELL_TYPE.WALL.code;
                    break;
                case '.':
                    levelMatrix[rowIndex][colIndex] = CELL_TYPE.EMPTY.code;
                    break;
                case 'D':
                    levelMatrix[rowIndex][colIndex] = CELL_TYPE.DIAMOND.code;
                    break;
                case 'O':
                    levelMatrix[rowIndex][colIndex] = CELL_TYPE.OUTPUT.code;
                    break;
            }

            colIndex++;
            if (colIndex >= this.numberOfColumns) {
                colIndex = 0;
                rowIndex++;
            }
        }
    }

    /**
     * Each tile of the game level must be rendered accordingly to the available width / height
     */
    public void render(GraphicsContext gc, int width, int height) {
        // Clear the graphic area
        gc.setFill(Color.WHITE);
        gc.clearRect(0, 0, width, height);

        for (int rowIndex = 0; rowIndex < numberOfRows; rowIndex++) {
            for (int colIndex = 0; colIndex < numberOfColumns; colIndex++) {
                if (levelMatrix[rowIndex][colIndex] == CELL_TYPE.WALL.code || (!outputReleased && levelMatrix[rowIndex][colIndex] == CELL_TYPE.OUTPUT.code)) {
                    renderWall(gc, rowIndex, colIndex);
                } else if (levelMatrix[rowIndex][colIndex] == CELL_TYPE.EMPTY.code) {
                    renderEmpty(gc, rowIndex, colIndex);
                } else if (levelMatrix[rowIndex][colIndex] == CELL_TYPE.DIAMOND.code) {
                    renderDiamond(gc, rowIndex, colIndex);
                } else if (levelMatrix[rowIndex][colIndex] == CELL_TYPE.OUTPUT.code) {
                    renderOutput(gc, rowIndex, colIndex);
                }
            }
        }
    }

    public void manageConsequences(Player player) {
        // earn diamond ?
        GameBoardPosition position = player.getPosition();
        if (levelMatrix[position.rowIndex][position.colIndex] == CELL_TYPE.DIAMOND.code) {
            levelMatrix[position.rowIndex][position.colIndex] = CELL_TYPE.EMPTY.code;
            nbDiamondsFound++;
            player.addToScore(100);

            if (nbDiamondsFound == nbDiamonds) {
                outputReleased = true;
            }
        }

        // on the output ?
        if (levelMatrix[position.rowIndex][position.colIndex] == CELL_TYPE.OUTPUT.code) {
            player.addToScore(500);
            player.win();
        }
    }

    private void renderOutput(GraphicsContext gc, int rowIndex, int colIndex) {
        gc.setFill(Color.DARKBLUE);
        gc.fillRect(colIndex * TILE_SIZE, rowIndex * TILE_SIZE, TILE_SIZE, TILE_SIZE);
        gc.setStroke(Color.LIGHTBLUE);
        gc.strokeRect(colIndex * TILE_SIZE, rowIndex * TILE_SIZE, TILE_SIZE, TILE_SIZE);
    }

    private void renderDiamond(GraphicsContext gc, int rowIndex, int colIndex) {
        gc.setFill(Color.RED);
        gc.fillRect((colIndex + 0.5) * TILE_SIZE + -4, (rowIndex + 0.5) * TILE_SIZE - 4, 8, 8);
    }

    private void renderEmpty(GraphicsContext gc, int rowIndex, int colIndex) {

    }

    private void renderWall(GraphicsContext gc, int rowIndex, int colIndex) {
//        gc.setFill(Color.DARKGRAY);
//        gc.fillRect(colIndex * TILE_SIZE, rowIndex * TILE_SIZE, TILE_SIZE, TILE_SIZE);
//        gc.setStroke(Color.LIGHTGRAY);
//        gc.strokeRect(colIndex * TILE_SIZE, rowIndex * TILE_SIZE, TILE_SIZE, TILE_SIZE);
        gc.drawImage(GameImage.WALL.getImage(), colIndex * TILE_SIZE, rowIndex * TILE_SIZE);
    }

    public GameBoardPosition getStartPosition() {
        return startPosition;
    }

    public int getTileSize() {
        return TILE_SIZE;
    }

    public boolean allowPositionToPlayer(GameBoardPosition position) {
        int cellCode = levelMatrix[position.rowIndex][position.colIndex];
        if (cellCode == CELL_TYPE.WALL.code) {
            return false;
        }
        if (!outputReleased && cellCode == CELL_TYPE.OUTPUT.code) {
            return false;
        }
        return true;
    }

    public int getNumberOfRows() {
        return numberOfRows;
    }

    /**
     * For each direction in the following order, return 3 boolean values :
     * - is there a wall ?
     * - is there a diamond ?
     * - is there an output ?
     * <p>
     * N - NE - E - SE - S - SW - W - NW
     * </p>
     */
    public boolean[] lookInAllDirections(GameBoardPosition position) {
        // TODO
        return new boolean[0];
    }

}
