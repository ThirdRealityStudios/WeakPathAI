package io.thirdreality.project.ai.core;

/**
 * This class is a representation of a datatype.
 * You need to override the equals(..) method for your needs
 * so that the AI can correctly fire neurons.
 *
 * By overriding equals(..),
 * you can define thresholds of your datatype for yourself,
 * so that you can leave some interpretation space for more outputs of your AI implementation.
 *
 * @param <Datatype>
 */
public class ComparableData<Datatype>
{
    public Datatype data;
    private Equalable e;

    public ComparableData(Equalable e, Datatype data)
    {
        this.data = data;
        this.e = e;
    }

    @Override
    public boolean equals(Object o)
    {
        return e.equals(o);
    }
}
