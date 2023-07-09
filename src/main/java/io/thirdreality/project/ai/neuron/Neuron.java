package io.thirdreality.project.ai.neuron;

import java.io.Serializable;
import java.util.ArrayList;
import static org.junit.jupiter.api.Assertions.*;

public class Neuron<Datatype> implements Serializable
{
    private Datatype data;

    public ArrayList<Neuron<Datatype>> hiddenLayer = new ArrayList<>();
    public ArrayList<Integer> hiddenLayerWeight = new ArrayList<>();

    private Runnable<Datatype> action;

    /**
     * Create a neuron with an action to call when triggered / fired.
     *
     * @param data Data to save in this neuron.
     * @param action Action to call when fired.
     */
    public Neuron(Datatype data, Runnable<Datatype> action)
    {
        this.data = data;
        this.action = action;
    }

    public Neuron()
    {
        this.action = n ->
        {

        };
    }

    /**
     * Create a neuron with NO action to call when triggered / fired.
     *
     * @param data Data to save in this neuron.
     */
    public Neuron(Datatype data)
    {
        this.data = data;
        this.action = n -> {};
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
            if(hiddenLayerWeight.get(i) > hiddenLayerWeight.get(heaviestNeuron))
            {
                // Remember Neurons which are cheaper to go.. (meaning the heaviest weight of a neuron).
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

    /**
     * Evaluates deep equality by calling the
     * equals(..) for all possible data of a neuron.
     * Underlying neurons and hidden layer structures
     * do also have to be deeply equal.
     * Hence,
     * the call to this method is highly expensive
     * but it is normal for neural networks.
     *
     * @param o Neuron to compare (as an Object)
     * @return true if deeply equal. Otherwise false.
     */
    @Override
    public boolean equals(Object o)
    {
        // Make sure, that a "null" neuron does not cause a NullPointerException.
        if(o == null)
        {
            return false;
        }

        Neuron<Datatype> otherNeuron = (Neuron<Datatype>) o;

        if(otherNeuron.hiddenLayer.size() != hiddenLayer.size() || otherNeuron.hiddenLayerWeight.size() != hiddenLayerWeight.size())
        {
            return false;
        }

        // Check deep equality of the neurons referenced in the hidden layer and their hidden layer and so on..
        for(int i = 0; i < hiddenLayer.size(); i++)
        {
            Neuron<Datatype> n0 = hiddenLayer.get(i),
                             n1 = otherNeuron.hiddenLayer.get(i);

            if(!n0.equals(n1))
            {
                return false;
            }
        }

        // Check deep equality of the weights for each neuron referenced in the hidden layer.
        for(int i = 0; i < hiddenLayerWeight.size(); i++)
        {
            Integer n0 = hiddenLayerWeight.get(i),
                    n1 = otherNeuron.hiddenLayerWeight.get(i);

            if(!n0.equals(n1))
            {
                return false;
            }
        }

        return otherNeuron.data.equals(data) && action.equals(action);
    }
}