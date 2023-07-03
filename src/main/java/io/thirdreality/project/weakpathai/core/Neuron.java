package io.thirdreality.project.weakpathai.core;

import javax.xml.crypto.Data;
import java.util.ArrayList;
import static org.junit.jupiter.api.Assertions.*;

public class Neuron<Datatype>
{
    private final ComparableData<Datatype> data;

    public ArrayList<Neuron<Datatype>> hiddenLayer = new ArrayList<>();
    public ArrayList<Integer> hiddenLayerWeight = new ArrayList<>();

    public Neuron(ComparableData<Datatype> data)
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
    public ComparableData<Datatype> fire()
    {
        assertEquals(hiddenLayer.size(), hiddenLayerWeight.size());

        if(hiddenLayer.isEmpty())
        {
            return data;
        }
        else if(hiddenLayer.size() == 1)
        {
            return hiddenLayer.get(0).data;
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

    public Neuron<Datatype> add(ComparableData<Datatype> output, Integer weight)
    {
        Neuron<Datatype> neuron = new Neuron<>(output);

        hiddenLayer.add(neuron);
        hiddenLayerWeight.add(weight);

        assertEquals(hiddenLayer.size(), hiddenLayerWeight.size());

        return neuron;
    }

    public ComparableData<Datatype> getData()
    {
        return data;
    }
}