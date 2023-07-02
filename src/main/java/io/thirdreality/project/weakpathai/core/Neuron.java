package io.thirdreality.project.weakpathai.core;

import java.util.ArrayList;

public class Neuron<Data>
{
    public Data data;
    public ArrayList<Link<Data>> targets = new ArrayList<>();

    /**
     *
     * @param n
     * @return true if there is exact one Neuron that is equal by the content.
     */
    public boolean knows(Neuron<Data> n)
    {
        for(Link l : targets)
        {
            if(l.target.equals(n))
            {
                return true;
            }
        }

        return false;
    }

    /**
     *
     * @param n
     * @return false if linking was unsuccessful because there is one Neuron already like the given one.
     */
    public boolean link(Neuron<Data> n)
    {
        if(!knows(n))
        {
            Link l = new Link();
            l.target = n;

            targets.add(l);

            return true;
        }

        return false;
    }
}
