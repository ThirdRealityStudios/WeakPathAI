package io.thirdreality.project.ai.neuron;

import java.io.InputStreamReader;

public class ConsoleNeuron extends Neuron<String>
{
    public ConsoleNeuron(String data)
    {
        super(data, n ->
        {
            System.out.println(n.getData());
        });
    }
}
