package io.thirdreality.project.ai.neuron;

public class ConsoleNeuron extends Neuron<String>
{
    public ConsoleNeuron(String data)
    {
        super(data);
    }

    @Override
    public void run()
    {
        System.out.println(getData());
    }
}
