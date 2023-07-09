package io.thirdreality.project.ai.impl.chatbot.core;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class SimilarString
{
    private String string;

    public SimilarString(String underlyingString)
    {
        this.string = underlyingString;
    }

    public String getString()
    {
        return string;
    }

    @Override
    public String toString()
    {
        return string;
    }

    @Override
    public boolean equals(Object o)
    {
        assertTrue(o instanceof SimilarString);

        return o != null && ((SimilarString) o).string.equalsIgnoreCase(string);
    }
}
