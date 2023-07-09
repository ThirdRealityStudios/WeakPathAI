package io.thirdreality.project.ai.impl.navigationai.core;

public class World
{
    public static final int SIZE = 8;

    public boolean[][] collider = new boolean[SIZE][SIZE];

    public static boolean isInside(int x, int y)
    {
        return x >= 0 && x < SIZE && y >= 0 && y < SIZE;
    }
}
