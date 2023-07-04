package io.thirdreality.project.ai.core;

import io.thirdreality.project.ai.neuron.Neuron;

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

    public AI(Equalable<Datatype> equalsMethod)
    {
        inputLayer = new ArrayList<>();

        this.equalsQuery = equalsMethod;
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
                return n.fire();
            }
        }

        return null;
    }

    /**
     * Will synchronize a neuron and works like a data stream
     * until you call finish() to stop adding neurons in a deeper layer.
     * After calling finish() you start again synchronizing neurons from
     * the beginning,
     * so from the input layer.
     *
     * @param newNeuron Data to add or set in the corresponding neuron.
     * @param weight Weight to add or subtract (neuron weight will never go below zero!)
     * @return 'true' if a hidden layer neuron was synchronized and 'false' if a input layer neuron was synchronized.
     */
    public boolean synchronize(Neuron newNeuron, Integer weight)
    {
        // If to look in the input layer for a neuron.
        if(syn == null)
        {
            for(Neuron<Datatype> n : inputLayer)
            {
                if(equalsQuery.equals(newNeuron.getData(), n.getData()))
                {
                    syn = n; // neuron found in the input layer.

                    return false;
                }
            }

            // If there was no neuron in the input layer, create one there..
            if(syn == null)
            {
                syn = newNeuron;

                inputLayer.add(syn);
            }

            return false;
        }

        // From here, there is a neuron usable yet..

        Neuron<Datatype> n = null;

        // Check if a neuron with the given value exists yet as a target if the 'syn' neuron.
        for(int i = 0; i < syn.hiddenLayer.size(); i++)
        {
            n = syn.hiddenLayer.get(i);

            if(equalsQuery.equals(newNeuron.getData(), n.getData()))
            {
                // synchronize neuron with added weight:
                syn.hiddenLayerWeight.set(i, Math.max(0, syn.hiddenLayerWeight.get(i) + weight));

                syn = n; // neuron found in the hidden layer of 'syn' neuron.

                return true;
            }
        }

        // If there was no neuron in the hidden layer, create one there..
        assertTrue(syn != null);

        syn.hiddenLayer.add(newNeuron);
        syn.hiddenLayerWeight.add(Math.max(0, weight));

        syn = newNeuron;

        return true;
    }

    public void finish()
    {
        syn = null;
    }
}