package io.thirdreality.project.ai.core;

import io.thirdreality.project.ai.neuron.Neuron;

import javax.xml.stream.*;
import javax.xml.*;
import java.io.Serializable;
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
public class AI<Datatype> implements Serializable
{
    public ArrayList<Neuron<Datatype>> inputLayer;

    private Neuron<Datatype> syn = null;

    private ArrayList<Neuron<Datatype>> historyBuffer, history;

    public AI()
    {
        inputLayer = new ArrayList<>();

        historyBuffer = new ArrayList<>();
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
            if(n.getData().equals(input))
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
                if(newNeuron.getData().equals(n.getData()))
                {
                    syn = n; // neuron found in the input layer.

                    // Remember neuron synchronized for latter query.
                    historyBuffer.add(syn);

                    return false;
                }
            }

            // If there was no neuron in the input layer, create one there..
            if(syn == null)
            {
                syn = newNeuron;

                // Remember neuron synchronized for latter query.
                historyBuffer.add(syn);

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

            if(newNeuron.getData().equals(n.getData()))
            {
                // synchronize neuron with added weight:
                syn.hiddenLayerWeight.set(i, Math.max(0, syn.hiddenLayerWeight.get(i) + weight));

                syn = n; // neuron found in the hidden layer of 'syn' neuron.

                // Remember neuron synchronized for latter query.
                historyBuffer.add(syn);

                return true;
            }
        }

        // If there was no neuron in the hidden layer, create one there..
        assertTrue(syn != null);

        syn.hiddenLayer.add(newNeuron);
        syn.hiddenLayerWeight.add(Math.max(0, weight));

        syn = newNeuron;

        // Remember neuron synchronized for latter query.
        historyBuffer.add(syn);

        return true;
    }

    /**
     * Closes or finishes the neuron synchronisation like a
     * flush() call on a stream.
     * If you re-use synchronize(..) after calling finish(),
     * then you will start over again in the input layer
     * when synchronizing neurons.
     *
     * @return Neurons synchronized with synchronize(..), whereas the last neuron is the deepest. The first neuron is in the input layer (index = 0).
     */
    public ArrayList<Neuron<Datatype>> finish()
    {
        syn = null;

        history = historyBuffer;

        historyBuffer = new ArrayList<>();

        // Create more neurons in the network, based on the input of this session.
        createPartialSolutions(historyBuffer);

        return history;
    }

    /**
     * This method will create partial solutions from data fed with synchronize()
     * after calling finish().
     * The idea behind this method is not to step through the whole
     * neural network if you already have put answers / data / solutions into the AI.
     * To find these answers much quicker,
     * each neuron is copied and linked to the input layer,
     * including the hidden layer of each neuron.
     * This process might be very slow,
     * but on the other hand,
     * the AI will learn much faster and can create a depth of the neuronal network
     * by using the data input you have given with synchronize(..).
     * Without this method,
     * there will be much less depth in your AI or even no depth at all.
     * By using this internal method in finish(),
     * you can already produce a lot of information and neuronal nets without the need to explicitly
     * feed the AI with data.
     * So, in the end this method will create new and additional neurons in the network.
     *
     * Examples for senseful use:
     * TODO Give examples with chat bot and finding a path.
     *
     * @param history History, returned by finish().
     */
    private void createPartialSolutions(ArrayList<Neuron<Datatype>> history)
    {
        for(Neuron<Datatype> n : history)
        {
            if(!inputLayer.contains(n))
            {
                // TODO: Probably multiple neurons can exist with the same data value but with other networks in the input layer.
                inputLayer.add(n.copy());
            }
        }
    }
}