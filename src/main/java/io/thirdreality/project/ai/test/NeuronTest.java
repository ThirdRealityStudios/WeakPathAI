package io.thirdreality.project.ai.test;

import io.thirdreality.project.ai.core.Equalable;
import io.thirdreality.project.ai.neuron.ConsoleNeuron;
import io.thirdreality.project.ai.neuron.Neuron;

import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

public class NeuronTest
{
    private ConsoleNeuron n;

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

        n = new ConsoleNeuron("1+1");

        assertEquals(n.hiddenLayer.size(), 0);
        assertEquals(n.hiddenLayerWeight.size(), 0);
    }

    @Test
    public void testFire()
    {
        ConsoleNeuron n0 = new ConsoleNeuron("22");

        n.add(n0, 0);

        assertEquals(n.hiddenLayer.size(), 1);
        assertEquals(n.hiddenLayerWeight.size(), 1);

        ConsoleNeuron n1 = new ConsoleNeuron("hidden");

        n.add(n1, 0);

        assertEquals(n.hiddenLayer.size(), 2);
        assertEquals(n.hiddenLayerWeight.size(), 2);

        ConsoleNeuron n2 = new ConsoleNeuron("hidden");

        n.add(n2, 0);

        assertEquals(n.hiddenLayer.size(), 3);
        assertEquals(n.hiddenLayerWeight.size(), 3);

        ConsoleNeuron n3 = new ConsoleNeuron("22");

        n.add(n3, 1);

        assertEquals(n.hiddenLayer.size(), 4);
        assertEquals(n.hiddenLayerWeight.size(), 4);
        assertEquals(n3.hiddenLayer.size(), 0);
        assertEquals(n3.hiddenLayerWeight.size(), 0);

        ConsoleNeuron n3_0 = new ConsoleNeuron("2");

        n3.add(n3_0, 0);

        assertEquals(n3.hiddenLayer.size(), 1);
        assertEquals(n3.hiddenLayerWeight.size(), 1);

        String output = n.fire();

        assertEquals("2", output);
    }

    @Test
    public void testAdd0()
    {
        n.add(new ConsoleNeuron("Hello"), 0);

        assertEquals(1, n.hiddenLayer.size());
        assertEquals("Hello", n.hiddenLayer.get(0).getData());
        assertEquals(0, n.hiddenLayerWeight.get(0));

        n.add(new ConsoleNeuron(","), 2);

        assertEquals(2, n.hiddenLayer.size());
        assertEquals(",", n.hiddenLayer.get(1).getData());
        assertEquals(2, n.hiddenLayerWeight.get(1));
    }
}
