package net.experiment.ai.simplegame.game;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import net.experiment.ai.simplegame.geometry.GameBoardPosition;
import net.experiment.ai.simplegame.geometry.Vector;
import net.experiment.ai.simplegame.player.Player;

import java.util.Arrays;
import java.util.List;

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
            , 14, 20, 3, 3, 7
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
     * For each direction in the following order, return 3 values :
     * - 1/distance to a wall
     * - 1/distance to a diamond : 0 means no diamond
     * - 1/distance to an output : 0 means no output
     * <p>
     * S - SE - E - NE - N - NW - W - SW
     * because y is aimed to the south in our coordinates system (java canvas !)
     * </p>
     */
    public double[] lookInAllDirections(GameBoardPosition position) {
        List<Vector> allDirections = Arrays.asList(
                new Vector(0, 1),
                new Vector(1, 1),
                new Vector(1, 0),
                new Vector(1, -1),
                new Vector(0, -1),
                new Vector(-1, -1),
                new Vector(-1, 0),
                new Vector(-1, 1)
        );

        int index = 0;
        double[] globalVision = new double[24]; // 3 distances per direction
        for (Vector directionToLook : allDirections) {
            try {
                double[] vision = lookInDirection(directionToLook, position);

                globalVision[index++] = vision[0];
                globalVision[index++] = vision[1];
                globalVision[index++] = vision[2];
            } catch (ArrayIndexOutOfBoundsException e) {
                e.printStackTrace();
            }
        }

        return globalVision;
    }

    /**
     * - 1/distance to a wall
     * - 1/distance to a diamond : 0 means no diamond
     * - 1/distance to an output : 0 means no output
     */
    private double[] lookInDirection(Vector directionToLook, GameBoardPosition playerPosition) {
        double[] vision = new double[3];

        // loop until we find a wall (which is expected !)
        // start by moving
        GameBoardPosition position = playerPosition.moveTo(directionToLook);

        int distance = 1;
        int distanceDiamond = 0;
        int distanceOutput = 0;
        boolean diamondFound = false;
        boolean outputFound = false;
        while (!isWall(position)) {
            if (!diamondFound && isDiamond(position)) {
                diamondFound = true;
                distanceDiamond = distance;
            }
            if (!outputFound && isOutput(position)) {
                outputFound = true;
                distanceOutput = distance;
            }
            position = position.moveTo(directionToLook);
            distance++;
        }

        vision[0] = 1.0 / distance;
        vision[1] = distanceDiamond == 0 ? 0 : 1.0 / distanceDiamond;
        vision[2] = distanceOutput == 0 ? 0 : 1.0 / distanceOutput;

        return vision;
    }

    private boolean isWall(GameBoardPosition position) {
        return levelMatrix[position.rowIndex][position.colIndex] == CELL_TYPE.WALL.code;
    }

    private boolean isDiamond(GameBoardPosition position) {
        return levelMatrix[position.rowIndex][position.colIndex] == CELL_TYPE.DIAMOND.code;
    }

    private boolean isOutput(GameBoardPosition position) {
        return levelMatrix[position.rowIndex][position.colIndex] == CELL_TYPE.OUTPUT.code;
    }
}
