package net.experiment.ai.simplegame.game;

import net.experiment.ai.simplegame.geometry.GameBoardPosition;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class GameLevelTest {

    private GameLevel level;

    @Before
    public void setUp() {
        level = new GameLevel(
                "" +
                        "WWWWWWWWWWWWWWOWWWWW" +
                        "W..................W" +
                        "W............D..D..W" +
                        "W..................W" +
                        "W...D..............W" +
                        "W..................W" +
                        "W...D..........D...W" +
                        "W..................W" +
                        "W...D.......D......W" +
                        "W....D.............W" +
                        "W.........D........W" +
                        "W.........D........W" +
                        "W...D..............W" +
                        "WWWWWWWWWWWWWWWWWWWW"
                , 14, 20, 3, 3, 7
        );
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
    @Test
    public void lookInAllDirections() {
        // Given
        GameBoardPosition playerPosition = new GameBoardPosition(1, 1);

        // When
        double[] vision = level.lookInAllDirections(playerPosition);

        // Then
        // S
        Assert.assertEquals(1.0 / 12.0, vision[0], 0.01);
        Assert.assertEquals(0, vision[1], 0.0);
        Assert.assertEquals(0, vision[2], 0.0);
        // S-E
        Assert.assertEquals(1.0 / 12.0, vision[3], 0.01);
        Assert.assertEquals(1.0 / 3.0, vision[4], 0.0);
        Assert.assertEquals(0, vision[5], 0.0);
        // E
        Assert.assertEquals(1.0 / 18.0, vision[6], 0.01);
        Assert.assertEquals(0, vision[7], 0.0);
        Assert.assertEquals(0, vision[8], 0.0);
        // N-E
        Assert.assertEquals(1.0 / 1.0, vision[9], 0.01);
        Assert.assertEquals(0, vision[10], 0.0);
        Assert.assertEquals(0, vision[11], 0.0);
        // N
        Assert.assertEquals(1.0 / 1.0, vision[12], 0.01);
        Assert.assertEquals(0, vision[13], 0.0);
        Assert.assertEquals(0, vision[14], 0.0);
        // N-W
        Assert.assertEquals(1.0 / 1.0, vision[15], 0.01);
        Assert.assertEquals(0, vision[16], 0.0);
        Assert.assertEquals(0, vision[17], 0.0);
        // W
        Assert.assertEquals(1.0 / 1.0, vision[18], 0.01);
        Assert.assertEquals(0, vision[19], 0.0);
        Assert.assertEquals(0, vision[20], 0.0);
        // S-W
        Assert.assertEquals(1.0 / 1.0, vision[21], 0.01);
        Assert.assertEquals(0, vision[22], 0.0);
        Assert.assertEquals(0, vision[23], 0.0);
    }
}