package io.thirdreality.project.ai.neuron;

import java.io.Serializable;

public interface Runnable<Datatype> extends Serializable
{
    abstract void run(Neuron<Datatype> n);
}
