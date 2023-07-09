package io.thirdreality.project.ai.impl.chatbot;

import io.thirdreality.project.ai.AI;
import io.thirdreality.project.ai.impl.chatbot.core.SimilarString;
import io.thirdreality.project.ai.neuron.Neuron;
import io.thirdreality.project.ai.neuron.Runnable;

import java.beans.XMLDecoder;
import java.beans.XMLEncoder;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.util.LinkedList;
import java.util.Scanner;

import static org.junit.jupiter.api.Assertions.assertNotNull;

// Press Shift twice to open the Search Everywhere dialog and type `show whitespaces`,
// then press Enter. You can now see whitespace characters in your code.
public class Main
{
    private Runnable<SimilarString> action;
    private AI<SimilarString> ai;
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

                    AI<SimilarString> ai = m.load();

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
    public AI<SimilarString> load() throws IOException, ClassNotFoundException
    {
        XMLDecoder decoder = new XMLDecoder(new FileInputStream("ai.data"));

        AI<SimilarString> ai = (AI<SimilarString>) decoder.readObject();

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

                ai.synchronize(new Neuron<SimilarString>(new SimilarString(words[i]), action), 1);
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
            System.out.print(n.getData() + " ");
        };

        this.ai = new AI<SimilarString>();

        this.s = new Scanner(System.in);
    }

    private LinkedList<SimilarString> getInputWords(String line)
    {
        LinkedList<SimilarString> inputs = new LinkedList<>();

        String[] words = line.split(" ");

        for(String word : words)
        {
            // Make a comparable String which does not need to be 100% equal, judging by its characters and case-sensitivity.
            inputs.add(new SimilarString(word));
        }

        return inputs;
    }

    public void run()
    {
        while(true)
        {
            System.out.print("Your input: ");

            LinkedList<SimilarString> inputs = getInputWords(s.nextLine());

            System.out.println();

            if("/exit".equals(inputs.getFirst().getString()))
            {
                break;
            }
            else if("/feed".equals(inputs.getFirst().getString()))
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

            // Synchronize sentence from input
            for(SimilarString word : inputs)
            {
                ai.synchronize(new Neuron<SimilarString>(word, action), 0);
            }

            if(inputs.size() > 2)
                ai.finish();

            // ai.synchronize(new Neuron<SimilarString>(input, action), 1);

            SimilarString n = ai.fire(inputs);

            if(n == null)
                System.out.println("AI: I don't know");
            else
                System.out.println();
        }
    }
}