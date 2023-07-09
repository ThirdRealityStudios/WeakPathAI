package io.thirdreality.project.ai.impl.navigationai.core;

public class Player
{
    public int x, y;
    public World world;

    public Player(World w)
    {
        this.world = w;
    }

    /**
     * Determines the 3x3 environment of a player.
     *
     * @return
     */
    public EnvironmentData getEnvironment()
    {
        EnvironmentData environmentData = new EnvironmentData();

        boolean[][] environment = environmentData.collider;

        int xArray = 0, yArray = 0;

        for(int x = this.x - 1; x < (this.x + 2); x++)
        {
            for(int y = this.y - 1; y < (this.y + 2); y++)
            {
                if(World.isInside(x, y))
                {
                    environment[xArray][yArray] = world.collider[x][y];
                }

                yArray++;
            }

            yArray = 0;
            xArray++;
        }

        return environmentData;
    }
}
