package io.thirdreality.project.weakpathai.game;

public class EnvironmentData
{
    public boolean[][] collider = new boolean[3][3];

    public EnvironmentData()
    {
        for(int x = 0; x < collider.length; x++)
        {
            for(int y = 0; y < collider.length; y++)
            {
                /*
                 * Make the environment pattern 'true' by standard,
                 * so all fields are being regarded as a collider.
                 * This is due to the exterior area outside the world dimensions
                 * if a player stands next to a border.
                 * Hence, all values (= fields) outside the world dimensions are being regarded as a collider (see JUnit tests, meaning PlayerTest class in package 'test').
                 */
                collider[x][y] = true;
            }
        }
    }
}