package io.thirdreality.project.ai;

import io.thirdreality.project.ai.core.AI;
import io.thirdreality.project.ai.core.Equalable;
import io.thirdreality.project.ai.neuron.Neuron;
import io.thirdreality.project.ai.neuron.Runnable;

import java.util.Scanner;

// Press Shift twice to open the Search Everywhere dialog and type `show whitespaces`,
// then press Enter. You can now see whitespace characters in your code.
public class Main
{
    private Equalable<String> e;
    private Runnable<String> action;
    private AI<String> ai;
    private Scanner s;

    public static void main(String[] args)
    {
        Main m = new Main();

        while(true)
        {
            m.run();
        }
    }

    public Main()
    {
        this.e = new Equalable<>()
        {
            @Override
            public boolean equals(String o0, String o1)
            {
                return o0.equals(o1);
            }
        };

        action = n ->
        {
            // Do nothing.
        };

        this.ai = new AI<String>(e);

        this.s = new Scanner(System.in);
    }

    public void run()
    {
        while(true)
        {
            System.out.print("Your input: ");

            String input = s.nextLine();

            System.out.println();

            if("/exit".equals(input))
            {
                break;
            }

            ai.synchronize(new Neuron<String>(input, action), 1);

            String n = ai.fire(input);

            // If there is any unknown answer / information, create new neurons and try
            // to sort some data in order to give a better answer next time.
            if(n == null)
            {
                ai.finish();
            }

            System.out.println("AI: " + (n == null ? "I don't know" : n) + "\n");
        }
    }
}