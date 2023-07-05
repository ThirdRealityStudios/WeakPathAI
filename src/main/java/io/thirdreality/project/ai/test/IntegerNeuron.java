package io.thirdreality.project.ai.test;

import io.thirdreality.project.ai.neuron.Neuron;
import io.thirdreality.project.ai.neuron.Runnable;

public class IntegerNeuron extends Neuron<Integer>
{
    public IntegerNeuron(Integer data)
    {
        super(data, new Runnable<Integer>()
        {
            @Override
            public void run(Neuron<Integer> n)
            {
                n.setData(n.getData() + 1);
            }
        });
    }
}
