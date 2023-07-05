package io.thirdreality.project.ai.test;

import io.thirdreality.project.ai.core.Equalable;
import io.thirdreality.project.ai.neuron.Neuron;

import io.thirdreality.project.ai.neuron.Runnable;
import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;

public class NeuronTest
{
    private Neuron<String> rootOutputNeuron;
    private Runnable<String> action;

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

        action = n ->
        {
            System.out.println(n.getData());
        };

        rootOutputNeuron = new Neuron<String>("1+1", action);

        assertEquals(rootOutputNeuron.hiddenLayer.size(), 0);
        assertEquals(rootOutputNeuron.hiddenLayerWeight.size(), 0);
    }

    @Test
    public void testFire()
    {
        initConsoleNeuron();

        Neuron<String> n0 = new Neuron<String>("22", action);

        rootOutputNeuron.add(n0, 0);

        assertEquals(rootOutputNeuron.hiddenLayer.size(), 1);
        assertEquals(rootOutputNeuron.hiddenLayerWeight.size(), 1);

        Neuron<String> n1 = new Neuron<String>("hidden", action);

        rootOutputNeuron.add(n1, 0);

        assertEquals(rootOutputNeuron.hiddenLayer.size(), 2);
        assertEquals(rootOutputNeuron.hiddenLayerWeight.size(), 2);

        Neuron<String> n2 = new Neuron<String>("hidden", action);

        rootOutputNeuron.add(n2, 0);

        assertEquals(rootOutputNeuron.hiddenLayer.size(), 3);
        assertEquals(rootOutputNeuron.hiddenLayerWeight.size(), 3);

        Neuron<String> n3 = new Neuron<String>("22", action);

        rootOutputNeuron.add(n3, 1);

        assertEquals(rootOutputNeuron.hiddenLayer.size(), 4);
        assertEquals(rootOutputNeuron.hiddenLayerWeight.size(), 4);
        assertEquals(n3.hiddenLayer.size(), 0);
        assertEquals(n3.hiddenLayerWeight.size(), 0);

        Neuron<String> n3_0 = new Neuron<String>("2", action);

        n3.add(n3_0, 0);

        assertEquals(n3.hiddenLayer.size(), 1);
        assertEquals(n3.hiddenLayerWeight.size(), 1);

        String output = rootOutputNeuron.fire();

        assertEquals("2", output);
    }

    @Test
    public void testAdd0()
    {
        initConsoleNeuron();

        rootOutputNeuron.add(new Neuron<String>("Hello", action), 0);

        assertEquals(1, rootOutputNeuron.hiddenLayer.size());
        assertEquals("Hello", rootOutputNeuron.hiddenLayer.get(0).getData());
        assertEquals(0, rootOutputNeuron.hiddenLayerWeight.get(0));

        rootOutputNeuron.add(new Neuron<String>(",", action), 2);

        assertEquals(2, rootOutputNeuron.hiddenLayer.size());
        assertEquals(",", rootOutputNeuron.hiddenLayer.get(1).getData());
        assertEquals(2, rootOutputNeuron.hiddenLayerWeight.get(1));
    }

    /**
     * Test copying (cloning) neurons by regarding the results
     * of the actions run for each neuron.
     */
    @Test
    public void testCopy0()
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

    @Test
    public void testCopy1()
    {
        initConsoleNeuron();

        Neuron<String> n0 = new Neuron<>("2+2", action);
        rootOutputNeuron.add(n0, 0);
        assertEquals(rootOutputNeuron.hiddenLayer.size(), 1);
        assertEquals(rootOutputNeuron.hiddenLayerWeight.size(), 1);

        Neuron<String> n1 = new Neuron<String>("3+3", action);
        n0.add(n1, 0);
        assertEquals(n0.hiddenLayer.size(), 1);
        assertEquals(n0.hiddenLayerWeight.size(), 1);

        Neuron<String> n2 = new Neuron<String>("4+4", action);
        n1.add(n2, 0);
        assertEquals(n1.hiddenLayer.size(), 1);
        assertEquals(n1.hiddenLayerWeight.size(), 1);

        Neuron<String> rootOutputNeuronCopied = rootOutputNeuron.copy();

        assert rootOutputNeuron != rootOutputNeuronCopied; // No shallow equality,
        assertEquals(rootOutputNeuron, rootOutputNeuronCopied); // but deep equality.

        assert n0 != rootOutputNeuronCopied.hiddenLayer.get(0); // No shallow equality,
        assertEquals(n0, rootOutputNeuronCopied.hiddenLayer.get(0)); // but deep equality.

        assert n1 != rootOutputNeuronCopied.hiddenLayer.get(0).hiddenLayer.get(0); // No shallow equality,
        assertEquals(n1, rootOutputNeuronCopied.hiddenLayer.get(0).hiddenLayer.get(0)); // but deep equality.

        // Repeat the last test with a copied n1 object.
        Neuron<String> n1Copied = n1.copy();

        assert n1Copied != rootOutputNeuronCopied.hiddenLayer.get(0).hiddenLayer.get(0); // No shallow equality,
        assertEquals(n1Copied, rootOutputNeuronCopied.hiddenLayer.get(0).hiddenLayer.get(0)); // but deep equality.

        // Repeat the last test with a copied n2 object.
        Neuron<String> n2Copied = n2.copy();

        assert n2Copied != rootOutputNeuronCopied.hiddenLayer.get(0).hiddenLayer.get(0).hiddenLayer.get(0); // No shallow equality,
        assertEquals(n2Copied, rootOutputNeuronCopied.hiddenLayer.get(0).hiddenLayer.get(0).hiddenLayer.get(0)); // but deep equality.

        /**
         * Now demonstrate that a modification of a copied neuron causes the deep equality to fail.
         */
        assertEquals(n1Copied.hiddenLayer.get(0).getData(), "4+4");
        assertEquals(n1Copied.hiddenLayer.get(0).getData(), n1.hiddenLayer.get(0).getData());

        assert n1Copied != rootOutputNeuronCopied.hiddenLayer.get(0).hiddenLayer.get(0); // No shallow equality,

        // Now modify one underlying neuron of n2 to prove that deep equality checks work:
        n1Copied.hiddenLayer.get(0).setData("whatever");
        assertNotEquals(n1Copied, rootOutputNeuronCopied.hiddenLayer.get(0).hiddenLayer.get(0)); // but also no deep equality (because of copying the underlying neuron of n1 and modifying its value).
    }
}
