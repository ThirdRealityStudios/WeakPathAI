package io.thirdreality.project.ai.neuron;

import java.util.ArrayList;
import static org.junit.jupiter.api.Assertions.*;

public class Neuron<Datatype>
{
    private Datatype data;

    public ArrayList<Neuron<Datatype>> hiddenLayer = new ArrayList<>();
    public ArrayList<Integer> hiddenLayerWeight = new ArrayList<>();

    private Runnable<Datatype> action;

    public Neuron(Datatype data, Runnable action)
    {
        this.data = data;
        this.action = action;
    }

    public void setData(Datatype data)
    {
        this.data = data;
    }

    /**
     * Fires an input value to this neuron.
     * This neuron will try to fire the given
     * value to another neuron until there is an end.
     * The value at the end of the neuronal network
     * is the output value you receive as a return value.
     *
     * @return Output value from the output layer.
     */
    public Datatype fire()
    {
        assertEquals(hiddenLayer.size(), hiddenLayerWeight.size());

        // 'action' always needs to be initialized for a neuron-specific action.
        assertNotNull(action);

        action.run(this);

        if(hiddenLayer.isEmpty())
        {
            return data;
        }
        else if(hiddenLayer.size() == 1)
        {
            return hiddenLayer.get(0).fire();
        }

        int heaviestNeuron = 0;

        for(int i = 1; i < hiddenLayer.size(); i++)
        {
            if(hiddenLayerWeight.get(i) >= hiddenLayerWeight.get(heaviestNeuron))
            {
                // Remember Neurons which are cheaper to go.. (weight greater).
                heaviestNeuron = i;
            }
        }

        return hiddenLayer.get(heaviestNeuron).fire();
    }

    public void add(Neuron neuron, Integer weight)
    {
        hiddenLayer.add(neuron);
        hiddenLayerWeight.add(weight);

        assertEquals(hiddenLayer.size(), hiddenLayerWeight.size());
    }

    public Datatype getData()
    {
        return data;
    }

    /**
     * Will run the given 'action' of the constructor for this neuron.
     */
    public void run()
    {
        action.run(this);
    }

    /**
     * Copies this neuron,
     * including its underlying neurons in the hidden layer.
     * The copied neuron will then be 100% identical
     * with this one including all the underlying neurons
     * connected to this neuron.
     * If you change the copy or the hidden layer of the copied neuron,
     * this will have no effect on the original neuron and its hidden layer neurons.
     * Hence,
     * this copy() operation is very costly in performance
     * but it can be used to reproduce information and
     * use it somewhere else in the neuronal network.
     *
     * Sample use: AI.createPartialSolutions(..).
     *
     * @return Copied neuron.
     */
    public Neuron<Datatype> copy()
    {
        // 'action' always needs to be initialized for a neuron-specific action.
        assertNotNull(action);

        // Copy data and action.
        Neuron<Datatype> neuronCopied = new Neuron<Datatype>(getData(), action);

        // Now copy all underlying neurons, including the weights:
        ArrayList<Neuron<Datatype>> hiddenLayerCopied = new ArrayList<>();
        ArrayList<Integer> hiddenLayerWeightCopied = new ArrayList<>();

        for(Neuron<Datatype> neuron : hiddenLayer)
        {
            // This action is VERY performance intensive as all underlying neurons get copied..
            hiddenLayerCopied.add(neuron.copy());
        }

        neuronCopied.hiddenLayer = hiddenLayerCopied;

        for(Integer i : hiddenLayerWeight)
        {
            // This action here is not that expensive as there are no structural deeper items to copy..
            hiddenLayerWeightCopied.add(i);
        }

        neuronCopied.hiddenLayerWeight = hiddenLayerWeightCopied;

        return neuronCopied;
    }
}