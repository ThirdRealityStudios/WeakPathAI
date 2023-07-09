package io.thirdreality.project.ai.impl.chatbot;

import io.thirdreality.project.ai.AI;
import io.thirdreality.project.ai.neuron.Neuron;
import io.thirdreality.project.ai.neuron.Runnable;

import java.beans.XMLDecoder;
import java.beans.XMLEncoder;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.util.Scanner;

import static org.junit.jupiter.api.Assertions.assertNotNull;

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

                    assertNotNull(ai);

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

    public void feed(String filename) throws IOException
    {
        FileInputStream iS = new FileInputStream(filename);

        FileReader r = new FileReader(filename);

        BufferedReader b = new BufferedReader(r);

        String nextLine = b.readLine();;

        while(nextLine != null)
        {
            String[] words = nextLine.split(" ");

            for(int i = 0; i < words.length && i < 20; i++)
            {
                if(words[i].equals(""))
                    continue;

                ai.synchronize(new Neuron<String>(words[i], n -> {System.out.print(n.getData() + " ");}), 1);
            }

            nextLine = b.readLine();
        }

        // Now process all possible neurons and data..
        ai.finish();

        System.out.println("AI: Data feed done!");

        r.close();
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
            else if("/feed".equals(input))
            {
                try
                {
                    feed("ai.feed");
                }
                catch(IOException e)
                {
                    e.printStackTrace();
                }
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