package io.thirdreality.project.ai;

import io.thirdreality.project.ai.core.AI;
import io.thirdreality.project.ai.neuron.Neuron;
import io.thirdreality.project.ai.neuron.Runnable;

import java.beans.XMLDecoder;
import java.beans.XMLEncoder;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.util.Scanner;

// Press Shift twice to open the Search Everywhere dialog and type `show whitespaces`,
// then press Enter. You can now see whitespace characters in your code.
public class Main
{
    private Runnable<String> action;
    private AI<String> ai;
    private Scanner s;

    public static void main(String[] args)
    {
        Main m = new Main();

        while(true)
        {
            try
            {
                if(Files.exists(Path.of("ai.data"), LinkOption.NOFOLLOW_LINKS))
                {
                    System.out.println("Loading save file..");

                    AI<String> ai = m.load();

                    m.ai = ai;
                }
            }
            catch(Exception e)
            {
                e.printStackTrace();
            }

            m.run();

            try
            {
                m.save();
            }
            catch(Exception e)
            {
                e.printStackTrace();
            }
        }
    }

    /**
     * Load AI process.
     */
    public AI<String> load() throws IOException, ClassNotFoundException
    {
        XMLDecoder decoder = new XMLDecoder(new FileInputStream("ai.data"));

        AI<String> ai = (AI<String>) decoder.readObject();

        decoder.close();

        return ai;
    }

    /**
     * Save AI process.
     */
    public void save() throws IOException
    {
        XMLEncoder encoder = new XMLEncoder(new FileOutputStream("ai.data"));

        encoder.writeObject(ai);

        encoder.flush();
        encoder.close();
    }

    public Main()
    {
        action = n ->
        {
            // Do nothing.
        };

        this.ai = new AI<String>();

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