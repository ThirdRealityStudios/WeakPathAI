package io.thirdreality.project.weakpathai.test;

import io.thirdreality.project.weakpathai.core.AI;
import io.thirdreality.project.weakpathai.core.Equalable;
import io.thirdreality.project.weakpathai.core.Neuron;

import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

public class AITest
{
    private Neuron<String> n;
    private AI<String> ai;

    @BeforeEach
    private void init()
    {
        Equalable<String> e = new Equalable<>()
        {
            public boolean equals(String s0, String s1)
            {
                assertNotNull(s0);
                assertNotNull(s1);

                return s0.equals(s1) && s1.equals(s0);
            }
        };

        n = new Neuron<String>("1+1");

        System.out.println("Input: " + n.getData());

        assertEquals(n.hiddenLayer.size(), 0);
        assertEquals(n.hiddenLayerWeight.size(), 0);

        Neuron n0 = n.add("22", 0);

        assertEquals(n.hiddenLayer.size(), 1);
        assertEquals(n.hiddenLayerWeight.size(), 1);

        Neuron n1 = n.add("hidden", 0);

        assertEquals(n.hiddenLayer.size(), 2);
        assertEquals(n.hiddenLayerWeight.size(), 2);

        Neuron n2 = n.add("hidden", 0);

        assertEquals(n.hiddenLayer.size(), 3);
        assertEquals(n.hiddenLayerWeight.size(), 3);

        Neuron n3 = n.add("22", 1);

        assertEquals(n.hiddenLayer.size(), 4);
        assertEquals(n.hiddenLayerWeight.size(), 4);
        assertEquals(n3.hiddenLayer.size(), 0);
        assertEquals(n3.hiddenLayerWeight.size(), 0);

        n3.add("2", 0);

        assertEquals(n3.hiddenLayer.size(), 1);
        assertEquals(n3.hiddenLayerWeight.size(), 1);

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
    public void testFire()
    {
        String output = ai.fire("1+1");

        System.out.println("Output: " + output);

        assertEquals("2", output);
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
