# Weak Path AI (general AI for numerous purposes)

This AI is meant to be used when you want to teach your computer simple context,

such as a chat bot.

Of course, this AI is weak and will never compete with ChatGPT and such but it can

also be used e.g. to train your video game NPCs to find a path in a game-world.

# JavaDOC

If you want to understand the code,

there is a generated HTML JavaDOC in the src folder.

# JUnit tests

This program was briefly (!) tested for its functionalities.

You can find and run the test methods in:

> src/main/java/io/thirdreality/project/ai/test/

# Chat Bot

For myself,

I implemented a simple chatbot which can save its process in a

file called 

> ai.save

The chatbot can be compiled and run in the Main.java file.

Of course,

you still need to feed the chatbot with a lot of data

and in the end you can do whatever you want with the AI..

A chatbot is just an example here..

# How does it work?

First of all,

the AI generates new Neurons for every kind of data you feed it.

There are mostly no redundancies (only if really necessary, but this has logical reasons).

With the new data fed (AI.synchronize() method),

the AI will try to create a kind (!) of cross product (like the one in math studies).

It will create links with neurons after calling AI.finish().

Also, it will copy all neurons including the deeper layers (hidden layer) of each neuron,

so every neuron can get its own independent weight (that is what I meant with "redundant" though it isn't in reality).