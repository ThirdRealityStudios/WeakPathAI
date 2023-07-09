package io.thirdreality.project.ai.impl.navigationai.test;

import io.thirdreality.project.ai.impl.navigationai.core.Player;
import io.thirdreality.project.ai.impl.navigationai.core.World;
import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

public class PlayerTest
{
    private World w = null;

    @BeforeEach
    private void init()
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
    public void testGetEnvironment0()
    {
        Player p = new Player(w);

        p.x = 1;
        p.y = 5;

        boolean[][] environment = p.getEnvironment().collider;

        assertTrue(environment[0][0]);
        assertFalse(environment[0][1]);
        assertFalse(environment[0][2]);

        assertFalse(environment[1][0]);
        assertFalse(environment[1][1]);
        assertFalse(environment[1][2]);

        assertFalse(environment[2][0]);
        assertTrue(environment[2][1]);
        assertFalse(environment[2][2]);
    }

    @Test
    public void testGetEnvironment1()
    {
        Player p = new Player(w);

        p.x = 6;
        p.y = 1;

        boolean[][] environment = p.getEnvironment().collider;

        assertTrue(environment[0][0]);
        assertFalse(environment[1][0]);
        assertTrue(environment[2][0]);

        assertFalse(environment[0][1]);
        assertFalse(environment[1][1]);
        assertFalse(environment[2][1]);

        assertFalse(environment[0][2]);
        assertTrue(environment[1][2]);
        assertFalse(environment[2][2]);
    }

    @Test
    public void testGetEnvironment2()
    {
        Player p = new Player(w);

        p.x = 4;
        p.y = 6;

        boolean[][] environment = p.getEnvironment().collider;

        assertTrue(environment[0][0]);
        assertTrue(environment[1][0]);
        assertFalse(environment[2][0]);

        assertFalse(environment[0][1]);
        assertTrue(environment[1][1]);
        assertFalse(environment[2][1]);

        assertFalse(environment[0][2]);
        assertTrue(environment[1][2]);
        assertTrue(environment[2][2]);
    }
}
