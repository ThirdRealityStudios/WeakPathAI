package io.thirdreality.project.weakpathai.core;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

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

    private Equalable equalsQuery;

    private Neuron<Datatype> syn = null;

    public AI(Equalable equalsQuery)
    {
        inputLayer = new ArrayList<>();

        this.equalsQuery = equalsQuery;
    }

    /**
     * Finds an output,
     * using the existing knowledge (neurons).
     * This method will not train the AI but return an output,
     * based on weights in the network.
     *
     * @return null if the AI has no idea. Otherwise an output.
     */
    public Datatype fire(Datatype input)
    {
        for(Neuron<Datatype> n : inputLayer)
        {
            // TODO Find neuron that matches the input most (not 100% but like 98%)
            // Will fire the first neuron that matches this neuron (regard thresholds).
            if(equalsQuery.equals(n.getData(), input))
            {
                int foo = 1;
                foo = 1;

                return n.fire();
            }
        }

        return null;
    }

    public void synchronize(Datatype data)
    {
        // If to look in the input layer for a neuron.
        if(syn == null)
        {
            for(Neuron<Datatype> n : inputLayer)
            {
                if(equalsQuery.equals(data, n.getData()))
                {
                    syn = n; // neuron found in the input layer.

                    return;
                }
            }

            // If there was no neuron in the input layer, create one there..
            if(syn == null)
            {
                syn = new Neuron<Datatype>(data);

                inputLayer.add(syn);
            }

            return;
        }

        // From here, there is a neuron usable yet..

        // Check if a neuron with the given value exists yet as a target if the 'syn' neuron.
        for(Neuron<Datatype> n : syn.hiddenLayer)
        {
            if(equalsQuery.equals(data, n.getData()))
            {
                syn = n; // neuron found in the hidden layer of 'syn' neuron.

                return;
            }
        }

        // If there was no neuron in the hidden layer, create one there..
        assertTrue(syn != null);

        Neuron newNeuron = new Neuron<Datatype>(data);

        syn.hiddenLayer.add(newNeuron);
        syn.hiddenLayerWeight.add(0);

        syn = newNeuron;

        return;
    }

    public void finish()
    {
        syn = null;
    }
}