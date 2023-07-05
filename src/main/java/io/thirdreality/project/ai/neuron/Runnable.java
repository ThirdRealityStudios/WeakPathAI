package io.thirdreality.project.ai.neuron;

public interface Runnable<Datatype>
{
    abstract void run(Neuron<Datatype> n);
}
