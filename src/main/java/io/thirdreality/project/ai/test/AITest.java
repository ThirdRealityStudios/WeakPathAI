package io.thirdreality.project.ai.test;

import java.util.*;

import io.thirdreality.project.ai.AI;
import io.thirdreality.project.ai.neuron.Neuron;

import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

public class AITest
{
    private AI<String> ai;

    @BeforeEach
    private void init()
    {
        ai = new AI<>();
    }

    @Test
    public void testFire0()
    {
        String input = "1+1",
                outputExpected = "2";

        ai.synchronize(new Neuron<String>(input), 0);
        ai.synchronize(new Neuron<String>(outputExpected), 0);

        String output = ai.fire(input);

        assertNotEquals(input, output);
        assertEquals(output, outputExpected);
    }

    @Test
    public void testFire1()
    {
        String input = "1+1",
                outputExpected = "2";

        ai.synchronize(new Neuron<String>(input), 0);
        ai.synchronize(new Neuron<String>("*"), 0);
        ai.synchronize(new Neuron<String>(outputExpected), 0);

        String output = ai.fire(input);

        assertNotEquals(input, output);
        assertEquals(output, outputExpected);
    }

    @Test
    public void testFire2()
    {
        ai.synchronize(new Neuron<String>("1+1"), 0);
        ai.synchronize(new Neuron<String>("*"), 0);
        ai.synchronize(new Neuron<String>("2"), 0);

        ai.finish();

        ai.synchronize(new Neuron<String>("1+1"), 0);
        ai.synchronize(new Neuron<String>("*"), 0);
        ai.synchronize(new Neuron<String>("Wrong result"), 1);

        String output = ai.fire("1+1");

        assertEquals(output, "Wrong result");
    }

    @Test
    public void testFire3()
    {
        ai.synchronize(new Neuron<String>("1+1"), 0);
        ai.synchronize(new Neuron<String>("*"), 0);
        ai.synchronize(new Neuron<String>("2"), 0);

        ai.finish();

        ai.synchronize(new Neuron<String>("1+1"), 0);
        ai.synchronize(new Neuron<String>("*"), 0);
        ai.synchronize(new Neuron<String>("Wrong result"), 0);

        // Change weight of first neuron after "*" to 1
        // (which is greater than of the neuron with the value "Wrong result"),
        // so "2" is preferred / has the highest
        // importance of all neurons in the corresponding hidden layer.
        ai.inputLayer.get(0).hiddenLayer.get(0).hiddenLayerWeight.set(0, 1);

        String output = ai.fire("1+1");

        assertEquals(output, "2");
    }

    @Test
    public void testFire4()
    {
        ai.synchronize(new Neuron<String>("1+1"), 0);
        ai.synchronize(new Neuron<String>("="), 1);
        ai.synchronize(new Neuron<String>("0,5+0,5"), 1);
        ai.synchronize(new Neuron<String>("="), 0);
        ai.synchronize(new Neuron<String>("2"), 0);

        ai.finish();

        ai.synchronize(new Neuron<String>("1+1"), 0);
        ai.synchronize(new Neuron<String>("="), 0);
        ai.synchronize(new Neuron<String>("1,5"), 0);

        ai.finish();

        ai.synchronize(new Neuron<String>("1+1"), 0);
        ai.synchronize(new Neuron<String>("="), 1);
        ai.synchronize(new Neuron<String>("1+1"), 1);
        ai.synchronize(new Neuron<String>("="), 1);
        ai.synchronize(new Neuron<String>("2"), 1);

        String output = ai.fire("1+1");

        assertEquals(output, "2");
    }

    @Test
    public void testSynchronize0()
    {
        assertEquals(ai.inputLayer.size(), 0);

        ai.synchronize(new Neuron<String>("Hi"), 0);

        assertEquals(ai.inputLayer.size(), 1);
        assertEquals(ai.inputLayer.get(0).getData(), "Hi");

        ai.synchronize(new Neuron<String>(","), 0);

        assertEquals(ai.inputLayer.get(0).hiddenLayer.size(), 1);
        assertEquals(ai.inputLayer.get(0).hiddenLayer.get(0).getData(), ",");
        assertEquals(ai.inputLayer.get(0).hiddenLayerWeight.get(0), 0);

        // Insert neuron manually and see if synchronize(..) does recognize..
        Neuron<String> manuallyInsertedNeuron = new Neuron<String>("a");

        ai.inputLayer.get(0).hiddenLayer.get(0).hiddenLayer.add(manuallyInsertedNeuron);
        ai.inputLayer.get(0).hiddenLayer.get(0).hiddenLayerWeight.add(0);

        ai.synchronize(new Neuron<String>("a"), 0);

        assertEquals(ai.inputLayer.get(0).hiddenLayer.get(0).hiddenLayer.size(), 1);
        assertEquals(ai.inputLayer.get(0).hiddenLayer.get(0).hiddenLayer.get(0).getData(), "a");
        assertEquals(ai.inputLayer.get(0).hiddenLayer.get(0).hiddenLayerWeight.get(0), 0);
    }

    @Test
    public void testSynchronize1()
    {
        // weight will have no effect as there is no
        // memory or variable for such "weights" in the input layer (no test required,
        // just to mention here)..
        boolean inputLayerAffected = !ai.synchronize(new Neuron<String>("Hi"), 2);
        assertTrue(inputLayerAffected);

        // Now the weight has an effect and can be tested,
        // as synchronize(..) works in the hidden layer now..
        boolean hiddenLayerAffected = ai.synchronize(new Neuron<String>(","), 2);
        assertEquals(ai.inputLayer.get(0).hiddenLayerWeight.get(0), 2);
        assertTrue(hiddenLayerAffected);

        // Giving a negative weight to a neuron is impossible,
        // as it is simply forbidden.
        // The neuron will have the weight 0 after being synchronized.
        ai.synchronize(new Neuron<String>(" "), -2);
        assertEquals(ai.inputLayer.get(0).hiddenLayer.get(0).hiddenLayerWeight.get(0), 0);
    }

    @Test
    public void testSynchronize2()
    {
        testSynchronize1();

        ai.finish();

        ai.synchronize(new Neuron<String>("Hi"), 0);

        // Because in testSynchronize1() the "," neuron already had the
        // weight value 2, the next weight value will be 4:
        ai.synchronize(new Neuron<String>(","), 2);

        assertEquals(ai.inputLayer.get(0).hiddenLayerWeight.get(0), 4);
    }

    @Test
    public void testHasNeuronSimplyCompared0()
    {
        Neuron<String> nHello0 = new Neuron<>("Hello"),
                       nHello1 = new Neuron<>("Hello"),
                       nSpace0 = new Neuron<>("Space"),
                       nSpace1 = new Neuron<>("Space");

        ai.synchronize(nHello0, 0);
        ai.finish();

        ai.synchronize(nHello1, 0);
        ai.finish();

        assertTrue(ai.hasNeuronSimplyCompared(nHello1));
        assertTrue(ai.hasNeuronSimplyCompared(nHello0));

        ai.synchronize(nHello0, 0);
        ai.synchronize(nSpace0, 0);
        ai.finish();

        assertTrue(ai.hasNeuronSimplyCompared(nHello0));
        assertTrue(ai.hasNeuronSimplyCompared(nHello1));
    }

    @Test
    public void testFinish0()
    {
        assertEquals(0, ai.inputLayer.size());

        ai.synchronize(new Neuron<String>("Hello "), 0);

        assertEquals(1, ai.inputLayer.size());
        assertEquals("Hello ", ai.inputLayer.get(0).getData());

        ai.synchronize(new Neuron<String>("World"), 0);

        assertEquals(1, ai.inputLayer.get(0).hiddenLayer.size());
        assertEquals("World", ai.inputLayer.get(0).hiddenLayer.get(0).getData());
        assertEquals(0, ai.inputLayer.get(0).hiddenLayerWeight.get(0));

        ai.synchronize(new Neuron<String>("!"), 0);

        assertEquals(1, ai.inputLayer.get(0).hiddenLayer.get(0).hiddenLayer.size());
        assertEquals("!", ai.inputLayer.get(0).hiddenLayer.get(0).hiddenLayer.get(0).getData());
        assertEquals(0, ai.inputLayer.get(0).hiddenLayer.get(0).hiddenLayerWeight.get(0));

        // "World " and "!" is missing for the input layer, meaning it is being copied and pulled over to it (as the function of ai.finish() suggests).
        // Thus, the size of the input layer is 3 after finish and 1 as you might expect or think maybe (think of the kinda cross product it is building).
        ArrayList<Neuron<String>> neuronHistory0 = ai.finish();

        assertEquals(3, ai.inputLayer.size());

        assertEquals(3, neuronHistory0.size());
        assertEquals("Hello ", neuronHistory0.get(0).getData());
        assertEquals("World", neuronHistory0.get(1).getData());
        assertEquals("!", neuronHistory0.get(2).getData());

        ai.synchronize(new Neuron<String>("Hallo"), 0);

        assertEquals(4, ai.inputLayer.size());
        assertEquals("Hallo", ai.inputLayer.get(3).getData());

        ai.synchronize(new Neuron<String>(","), 0);

        assertEquals(1, ai.inputLayer.get(3).hiddenLayer.size());
        assertEquals(",", ai.inputLayer.get(3).hiddenLayer.get(0).getData());
        assertEquals(0, ai.inputLayer.get(3).hiddenLayerWeight.get(0));

        // Now "," also gets copied and pulled over to the input layer. Hence after ai.finish() the size of the input layer 5 at a total.
        ArrayList<Neuron<String>> neuronHistory1 = ai.finish();

        assertEquals(5, ai.inputLayer.size());

        assertEquals(2, neuronHistory1.size());
        assertEquals("Hallo", neuronHistory1.get(0).getData());
        assertEquals(",", neuronHistory1.get(1).getData());

        ai.synchronize(new Neuron<String>("Hello "), 0);

        assertEquals(5, ai.inputLayer.size());
        assertEquals("Hello ", ai.inputLayer.get(0).getData());

        ai.synchronize(new Neuron<String>("World"), 0);

        assertEquals(1, ai.inputLayer.get(0).hiddenLayer.size());
        assertEquals("World", ai.inputLayer.get(0).hiddenLayer.get(0).getData());
        assertEquals(0, ai.inputLayer.get(0).hiddenLayerWeight.get(0));

        ai.synchronize(new Neuron<String>("!"), 0);

        assertEquals(ai.inputLayer.get(0).hiddenLayer.get(0).hiddenLayer.size(), 1);
        assertEquals(ai.inputLayer.get(0).hiddenLayer.get(0).hiddenLayer.get(0).getData(), "!");
        assertEquals(ai.inputLayer.get(0).hiddenLayer.get(0).hiddenLayerWeight.get(0), 0);

        ai.synchronize(new Neuron<String>(" "), 0);

        assertEquals(ai.inputLayer.get(0).hiddenLayer.get(0).hiddenLayer.get(0).hiddenLayer.size(), 1);
        assertEquals(ai.inputLayer.get(0).hiddenLayer.get(0).hiddenLayer.get(0).hiddenLayer.get(0).getData(), " ");
        assertEquals(ai.inputLayer.get(0).hiddenLayer.get(0).hiddenLayer.get(0).hiddenLayerWeight.get(0), 0);

        //
        ArrayList<Neuron<String>> neuronHistory2 = ai.finish();

        assertEquals(4, neuronHistory2.size());
        assertEquals("Hello ", neuronHistory2.get(0).getData());
        assertEquals("World", neuronHistory2.get(1).getData());
        assertEquals("!", neuronHistory2.get(2).getData());
        assertEquals(" ", neuronHistory2.get(3).getData());

        assertEquals(ai.inputLayer.size(), 6);

        ai.synchronize(new Neuron<String>("Hallo"), 0);

        assertEquals(ai.inputLayer.size(), 6);
        assertEquals(ai.inputLayer.get(3).getData(), "Hallo");

        ai.synchronize(new Neuron<String>("!"), 0);

        assertEquals(ai.inputLayer.get(3).hiddenLayer.size(), 2);
        assertEquals(ai.inputLayer.get(3).hiddenLayer.get(1).getData(), "!");
        assertEquals(ai.inputLayer.get(3).hiddenLayerWeight.get(1), 0);

        ArrayList<Neuron<String>> neuronHistory3 = ai.finish();

        // Still 6 because "!" already exists in the input layer.
        assertEquals(ai.inputLayer.size(), 6);

        assertEquals(2, neuronHistory3.size());
        assertEquals("Hallo", neuronHistory3.get(0).getData());
        assertEquals("!", neuronHistory3.get(1).getData());
    }
}
