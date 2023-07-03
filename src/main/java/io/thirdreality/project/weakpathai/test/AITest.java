package io.thirdreality.project.weakpathai.test;

import io.thirdreality.project.weakpathai.core.AI;
import io.thirdreality.project.weakpathai.core.ComparableData;
import io.thirdreality.project.weakpathai.core.Equalable;
import io.thirdreality.project.weakpathai.core.Neuron;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class AITest
{
    private Neuron<String> n;

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
    }

    @Test
    public void testFire()
    {
        AI<String> ai = new AI<>();

        Equalable<String> equalable = new Equalable<String>()
        {
            @Override
            public boolean equals(String o0, String o1)
            {
                return o0.equals(o1);
            }
        };

        String output = ai.fire(equalable, "1+1");

        System.out.println("Output: " + output);

        assertEquals("2", output);
    }
}
