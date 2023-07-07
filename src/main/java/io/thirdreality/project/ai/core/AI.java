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
 * General notice: if you try to check if a neuron is
 * contained in the AI object, then you need to differentiate
 * between the method has "hasNeuronSimplyCompared(Neuron<Datatype> n0)"
 * and "AI.this.inputLayer.contains(Object o)".
 * The difference is that hasNeuronSimplyCompared(..) tells you if
 * a neuron is contained in the input layer if only the 'data'
 * variables do match with each other.
 * AI.this.inputLayer.contains(..) on the other hand performs
 * a complex check it will succeed if all the neuronal nets
 * of both neurons (meaning the hidden layer) will match too exactly.
 * So, if you want to make a simple "contains test",
 * judging by the neurons value simply,
 * then use hasNeuronSimplyCompared(..).
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
            // TODO Find neuron that matches the input most (not 100% but like 98%),
            //  e.g. inherit from String and make a String class that overrides equals(..) and only compare similar strings (case-insensitive etc.).
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

        // Create more neurons in the network, based on the input of this session.
        createPartialSolutions(historyBuffer);

        history = historyBuffer;

        historyBuffer = new ArrayList<>();

        return history;
    }

    /**
     * Tells you if a given neuron is contained in the input layer of this AI.
     * Note that only the 'data' of the corresponding neuron gets considered
     * while searching and not other (deeper) characteristics!
     *
     * @param n0 Neuron to look for in the input layer.
     * @return true if the neuron is contained in the input layer. Otherwise false.
     */
    public boolean hasNeuronSimplyCompared(Neuron<Datatype> n0)
    {
        if(n0 == null)
            return false;

        if(n0.getData() == null)
            return false;

        for(Neuron<Datatype> n1 : inputLayer)
        {
            if(n0.getData().equals(n1.getData()))
                return true;
            else
            {
                // TODO Instead: Try to merge all sub-nets to the existing neuron.
            }
        }

        return false;
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
     *
     * @param history History, returned by finish().
     */
    private void createPartialSolutions(ArrayList<Neuron<Datatype>> history)
    {
        // Use buffer to prevent ConcurrentModificationException because I try to write to inputLayer while it has read-access in the second inner loop.
        ArrayList<Neuron<Datatype>> buffer = new ArrayList<>();

        for(Neuron<Datatype> historyNeuron : history)
        {
            if(!hasNeuronSimplyCompared(historyNeuron))
            {
                buffer.add(historyNeuron);
            }
        }
        // From here, inputLayer can have write-access again.

        // Safely add objects to the inputLayer ArrayList without causing an Exception because of read-access.
        inputLayer.addAll(buffer);
    }
}