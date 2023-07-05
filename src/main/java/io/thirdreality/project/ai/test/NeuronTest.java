package io.thirdreality.project.ai.test;

import io.thirdreality.project.ai.core.Equalable;
import io.thirdreality.project.ai.neuron.ConsoleNeuron;
import io.thirdreality.project.ai.neuron.Neuron;

import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;

public class NeuronTest
{
    private ConsoleNeuron rootConsoleNeuron;

    private void initConsoleNeuron()
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

        rootConsoleNeuron = new ConsoleNeuron("1+1");

        assertEquals(rootConsoleNeuron.hiddenLayer.size(), 0);
        assertEquals(rootConsoleNeuron.hiddenLayerWeight.size(), 0);
    }

    @Test
    public void testFire()
    {
        initConsoleNeuron();

        ConsoleNeuron n0 = new ConsoleNeuron("22");

        rootConsoleNeuron.add(n0, 0);

        assertEquals(rootConsoleNeuron.hiddenLayer.size(), 1);
        assertEquals(rootConsoleNeuron.hiddenLayerWeight.size(), 1);

        ConsoleNeuron n1 = new ConsoleNeuron("hidden");

        rootConsoleNeuron.add(n1, 0);

        assertEquals(rootConsoleNeuron.hiddenLayer.size(), 2);
        assertEquals(rootConsoleNeuron.hiddenLayerWeight.size(), 2);

        ConsoleNeuron n2 = new ConsoleNeuron("hidden");

        rootConsoleNeuron.add(n2, 0);

        assertEquals(rootConsoleNeuron.hiddenLayer.size(), 3);
        assertEquals(rootConsoleNeuron.hiddenLayerWeight.size(), 3);

        ConsoleNeuron n3 = new ConsoleNeuron("22");

        rootConsoleNeuron.add(n3, 1);

        assertEquals(rootConsoleNeuron.hiddenLayer.size(), 4);
        assertEquals(rootConsoleNeuron.hiddenLayerWeight.size(), 4);
        assertEquals(n3.hiddenLayer.size(), 0);
        assertEquals(n3.hiddenLayerWeight.size(), 0);

        ConsoleNeuron n3_0 = new ConsoleNeuron("2");

        n3.add(n3_0, 0);

        assertEquals(n3.hiddenLayer.size(), 1);
        assertEquals(n3.hiddenLayerWeight.size(), 1);

        String output = rootConsoleNeuron.fire();

        assertEquals("2", output);
    }

    @Test
    public void testAdd0()
    {
        initConsoleNeuron();

        rootConsoleNeuron.add(new ConsoleNeuron("Hello"), 0);

        assertEquals(1, rootConsoleNeuron.hiddenLayer.size());
        assertEquals("Hello", rootConsoleNeuron.hiddenLayer.get(0).getData());
        assertEquals(0, rootConsoleNeuron.hiddenLayerWeight.get(0));

        rootConsoleNeuron.add(new ConsoleNeuron(","), 2);

        assertEquals(2, rootConsoleNeuron.hiddenLayer.size());
        assertEquals(",", rootConsoleNeuron.hiddenLayer.get(1).getData());
        assertEquals(2, rootConsoleNeuron.hiddenLayerWeight.get(1));
    }

    @Test
    public void testCopyAndRun0()
    {
        // Create an IntegerNeuron which will simply increment its value when run (= fired per fire() method, when used in a neural network).
        IntegerNeuron n0 = new IntegerNeuron(0);
        assertEquals(0, n0.getData());

        n0.run(); // data += 1
        Neuron n1 = n0.copy();
        assertEquals(1, n1.getData());

        n1.run(); // data += 1
        Neuron n2 = n1.copy();
        assertEquals(2, n2.getData());

        n2.run(); // data += 1
        n2.run(); // data += 1
        n2.run(); // data += 1
        n2.run(); // data += 1
        n2.run(); // data += 1
        Neuron n3 = n2.copy();
        assertEquals(7, n2.getData());

        // Now make sure the "old" (not copied) neurons have not been modified:
        assertEquals(n0.getData(), 1);
        assertEquals(n1.getData(), 2);
        assertEquals(n2.getData(), 7);
        assertEquals(n3.getData(), 7);
    }
}
