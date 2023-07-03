package io.thirdreality.project.weakpathai.core;

import java.util.ArrayList;

/**
 * In the input layer,
 * this AI will make a content comparison
 * of the neurons values to know which neuron
 * to fire first.
 * After that, the next neuron is always determined
 * by the one with the greatest weight.
 * If multiple neurons have the same weight,
 * then the last one is chosen simply.
 * For better AI results try to make them as different as possible.
 *
 * @param <Datatype>
 */
public class AI<Datatype>
{
    public ArrayList<Neuron<Datatype>> inputLayer;

    public AI()
    {
        inputLayer = new ArrayList<>();
    }

    /**
     * Finds an output,
     * using the existing knowledge (neurons).
     * This method will not train the AI but return an output,
     * based on weights in the network.
     *
     * @return null if the AI has no idea. Otherwise an output.
     */
    public Datatype fire(ComparableData<Datatype> input)
    {
        // TODO Find neuron that matches the input most (not 100% but like 98%) => Override equals(..) in each Datatype!

        for(Neuron n : inputLayer)
        {
            // Will fire the first neuron that matches this neuron (regard thresholds).
            if(n.getData().equals(input))
            {
                n.fire();
            }
        }

        return null;
    }
}
