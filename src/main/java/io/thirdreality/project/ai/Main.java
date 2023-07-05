package io.thirdreality.project.ai;

import io.thirdreality.project.ai.core.AI;
import io.thirdreality.project.ai.core.Equalable;
import io.thirdreality.project.ai.neuron.ConsoleNeuron;
import io.thirdreality.project.ai.neuron.Neuron;

import java.util.Scanner;

// Press Shift twice to open the Search Everywhere dialog and type `show whitespaces`,
// then press Enter. You can now see whitespace characters in your code.
public class Main
{
    public static void main(String[] args)
    {
        Equalable<String> e = new Equalable<>()
        {
            @Override
            public boolean equals(String o0, String o1)
            {
                return o0.equals(o1);
            }
        };

        AI<String> ai = new AI<>(e);

        String input = null;

        Scanner s = new Scanner(System.in);

        do
        {
            System.out.print("Your input: ");

            input = s.nextLine();

            System.out.println();

            ai.synchronize(new ConsoleNeuron(input), 1);
            ai.finish();

            String n = ai.fire(input);

            System.out.println("AI: " + n + "\n");
        }
        while(!"/exit".equals(input));

        s.close();
    }
}