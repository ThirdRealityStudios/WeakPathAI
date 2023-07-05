package io.thirdreality.project.ai.test;

import io.thirdreality.project.game.pathai.World;
import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;

public class WorldTest
{
    private World w = null;

    @BeforeEach
    public void init()
    {
        w = new World();

        w.collider[0][1] = true;
        w.collider[0][2] = true;
        w.collider[0][4] = true;

        w.collider[1][3] = true;

        w.collider[2][0] = true;
        w.collider[2][5] = true;
        w.collider[2][7] = true;

        w.collider[3][1] = true;
        w.collider[3][4] = true;
        w.collider[3][5] = true;

        w.collider[4][0] = true;
        w.collider[4][5] = true;
        w.collider[4][6] = true;
        w.collider[4][7] = true;

        w.collider[5][0] = true;
        w.collider[5][3] = true;
        w.collider[5][7] = true;

        w.collider[6][2] = true;
        w.collider[6][4] = true;

        w.collider[7][0] = true;
        w.collider[7][3] = true;
        w.collider[7][5] = true;
        w.collider[7][7] = true;
    }

    @Test
    public void testIsInside()
    {
        assertTrue(World.isInside(0, 0));
        assertTrue(World.isInside(7, 7));
        assertTrue(World.isInside(0, 7));
        assertTrue(World.isInside(7, 0));

        assertTrue(World.isInside(3, 4));
        assertTrue(World.isInside(4, 4));
        assertTrue(World.isInside(3, 3));
        assertTrue(World.isInside(4, 3));

        assertFalse(World.isInside(-1, 0));
        assertFalse(World.isInside(0, -1));

        assertFalse(World.isInside(8, 0));
        assertFalse(World.isInside(7, -1));

        assertFalse(World.isInside(0, 8));
        assertFalse(World.isInside(-1, 7));

        assertFalse(World.isInside(8, 7));
        assertFalse(World.isInside(7, 8));
    }
}
