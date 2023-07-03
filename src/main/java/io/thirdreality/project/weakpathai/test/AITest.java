package io.thirdreality.project.weakpathai.test;

import io.thirdreality.project.weakpathai.core.AI;
import io.thirdreality.project.weakpathai.core.Equalable;
import io.thirdreality.project.weakpathai.core.Neuron;

import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

public class AITest
{
    private AI<String> ai;

    @BeforeEach
    private void init()
    {
        Equalable<String> equalable = new Equalable<String>()
        {
            @Override
            public boolean equals(String o0, String o1)
            {
                return o0.equals(o1) && o1.equals(o0);
            }
        };

        ai = new AI<>(equalable);
    }

    @Test
    public void testFire0()
    {
        String input = "1+1",
                outputExpected = "2";

        ai.synchronize(input);
        ai.synchronize(outputExpected);

        String output = ai.fire(input);

        assertNotEquals(input, output);
        assertEquals(output, outputExpected);
    }

    @Test
    public void testFire1()
    {
        String input = "1+1",
                outputExpected = "2";

        ai.synchronize(input);
        ai.synchronize("*");
        ai.synchronize(outputExpected);

        String output = ai.fire(input);

        assertNotEquals(input, output);
        assertEquals(output, outputExpected);
    }

    @Test
    public void testFire2()
    {
        ai.synchronize("1+1");
        ai.synchronize("*");
        ai.synchronize("2");

        ai.finish();

        ai.synchronize("1+1");
        ai.synchronize("*");
        ai.synchronize("Wrong result");

        String output = ai.fire("1+1");

        assertEquals(output, "Wrong result");
    }

    @Test
    public void testFire3()
    {
        ai.synchronize("1+1");
        ai.synchronize("*");
        ai.synchronize("2");

        ai.finish();

        ai.synchronize("1+1");
        ai.synchronize("*");
        ai.synchronize("Wrong result");

        // Change weight of first neuron after "*" to 1
        // (which is greater than of the neuron with the value "Wrong result"),
        // so "2" is preferred / has the highest
        // importance of all neurons in the corresponding hidden layer.
        ai.inputLayer.get(0).hiddenLayer.get(0).hiddenLayerWeight.set(0, 1);

        String output = ai.fire("1+1");

        assertEquals(output, "2");
    }

    @Test
    public void testSynchronize()
    {
        assertEquals(ai.inputLayer.size(), 0);

        ai.synchronize("Hi");

        assertEquals(ai.inputLayer.size(), 1);
        assertEquals(ai.inputLayer.get(0).getData(), "Hi");

        ai.synchronize(",");

        assertEquals(ai.inputLayer.get(0).hiddenLayer.size(), 1);
        assertEquals(ai.inputLayer.get(0).hiddenLayer.get(0).getData(), ",");
        assertEquals(ai.inputLayer.get(0).hiddenLayerWeight.get(0), 0);

        // Insert neuron manually and see if synchronize(..) does recognize..
        Neuron<String> manuallyInsertedNeuron = new Neuron<>("a");

        ai.inputLayer.get(0).hiddenLayer.get(0).hiddenLayer.add(manuallyInsertedNeuron);
        ai.inputLayer.get(0).hiddenLayer.get(0).hiddenLayerWeight.add(0);

        ai.synchronize("a");

        assertEquals(ai.inputLayer.get(0).hiddenLayer.get(0).hiddenLayer.size(), 1);
        assertEquals(ai.inputLayer.get(0).hiddenLayer.get(0).hiddenLayer.get(0).getData(), "a");
        assertEquals(ai.inputLayer.get(0).hiddenLayer.get(0).hiddenLayerWeight.get(0), 0);
    }

    @Test
    public void testFinish()
    {
        assertEquals(ai.inputLayer.size(), 0);

        ai.synchronize("Hello ");

        assertEquals(ai.inputLayer.size(), 1);
        assertEquals(ai.inputLayer.get(0).getData(), "Hello ");

        ai.synchronize("World");

        assertEquals(ai.inputLayer.get(0).hiddenLayer.size(), 1);
        assertEquals(ai.inputLayer.get(0).hiddenLayer.get(0).getData(), "World");
        assertEquals(ai.inputLayer.get(0).hiddenLayerWeight.get(0), 0);

        // Insert neuron manually and see if synchronize(..) does recognize..
        Neuron<String> manuallyInsertedNeuron = new Neuron<>("!");

        ai.inputLayer.get(0).hiddenLayer.get(0).hiddenLayer.add(manuallyInsertedNeuron);
        ai.inputLayer.get(0).hiddenLayer.get(0).hiddenLayerWeight.add(0);

        ai.synchronize("!");

        assertEquals(ai.inputLayer.get(0).hiddenLayer.get(0).hiddenLayer.size(), 1);
        assertEquals(ai.inputLayer.get(0).hiddenLayer.get(0).hiddenLayer.get(0).getData(), "!");
        assertEquals(ai.inputLayer.get(0).hiddenLayer.get(0).hiddenLayerWeight.get(0), 0);

        ai.finish();

        ai.synchronize("Hallo");

        assertEquals(ai.inputLayer.size(), 2);
        assertEquals(ai.inputLayer.get(1).getData(), "Hallo");

        ai.synchronize(",");

        assertEquals(ai.inputLayer.get(1).hiddenLayer.size(), 1);
        assertEquals(ai.inputLayer.get(1).hiddenLayer.get(0).getData(), ",");
        assertEquals(ai.inputLayer.get(1).hiddenLayerWeight.get(0), 0);

        ai.finish();

        ai.synchronize("Hello ");

        assertEquals(ai.inputLayer.size(), 2);
        assertEquals(ai.inputLayer.get(0).getData(), "Hello ");

        ai.synchronize("World");

        assertEquals(ai.inputLayer.get(0).hiddenLayer.size(), 1);
        assertEquals(ai.inputLayer.get(0).hiddenLayer.get(0).getData(), "World");
        assertEquals(ai.inputLayer.get(0).hiddenLayerWeight.get(0), 0);

        ai.synchronize("!");

        assertEquals(ai.inputLayer.get(0).hiddenLayer.get(0).hiddenLayer.size(), 1);
        assertEquals(ai.inputLayer.get(0).hiddenLayer.get(0).hiddenLayer.get(0).getData(), "!");
        assertEquals(ai.inputLayer.get(0).hiddenLayer.get(0).hiddenLayerWeight.get(0), 0);

        ai.synchronize(" ");

        assertEquals(ai.inputLayer.get(0).hiddenLayer.get(0).hiddenLayer.get(0).hiddenLayer.size(), 1);
        assertEquals(ai.inputLayer.get(0).hiddenLayer.get(0).hiddenLayer.get(0).hiddenLayer.get(0).getData(), " ");
        assertEquals(ai.inputLayer.get(0).hiddenLayer.get(0).hiddenLayer.get(0).hiddenLayerWeight.get(0), 0);

        ai.finish();

        assertEquals(ai.inputLayer.size(), 2);

        ai.synchronize("Hallo");

        assertEquals(ai.inputLayer.size(), 2);
        assertEquals(ai.inputLayer.get(1).getData(), "Hallo");

        ai.synchronize("!");

        assertEquals(ai.inputLayer.get(1).hiddenLayer.size(), 2);
        assertEquals(ai.inputLayer.get(1).hiddenLayer.get(1).getData(), "!");
        assertEquals(ai.inputLayer.get(1).hiddenLayerWeight.get(1), 0);

        ai.finish();
    }
}
