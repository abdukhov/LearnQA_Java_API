package org.example;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class ShortPhraseTest {

    @ParameterizedTest
    @ValueSource(strings = {"", "Short phrase", "Phrase of more than chars 15 length", "15 characters  "})
    public void CheckPhraseLength_ItIsLongerThan15_TestPass(String phrase) {
        var actualLength = phrase.length();
        var expectedLength = 15;

        assertTrue(actualLength > expectedLength, "The phrase length is " + actualLength + " characters. The expected length should be > 15 characters" );
    }
}
