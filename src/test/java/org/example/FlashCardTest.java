package org.example;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class FlashCardTest {

    @Test
    @DisplayName("Test for get term, get definition, get errors")
    void getTerm() {
        FlashCard flashCard = new FlashCard("Term", "Def");
        assertEquals("Term", flashCard.getTerm());
        assertEquals("Def", flashCard.getDefinition());
        assertEquals(0, flashCard.getErrors());
    }

    @Test
    @DisplayName("Test for set definition")
    void getTerm1() {
        FlashCard flashCard = new FlashCard("Term", "Def", 4);
        assertEquals("Term", flashCard.getTerm());
        assertEquals("Def", flashCard.getDefinition());
        flashCard.setDefinition("Definition");
        assertEquals("Definition", flashCard.getDefinition());
        assertEquals(4, flashCard.getErrors());
    }

    @Test
    @DisplayName("Test for increment error")
    void getTerm2() {
        FlashCard flashCard = new FlashCard("Term", "Def", 4);
        assertEquals("Term", flashCard.getTerm());
        assertEquals("Def", flashCard.getDefinition());
        assertEquals(4, flashCard.getErrors());
        flashCard.incrementError();
        assertEquals(5, flashCard.getErrors());
    }

    @Test
    @DisplayName("Test for set error")
    void getTerm3() {
        FlashCard flashCard = new FlashCard("Term", "Def", 4);
        assertEquals("Term", flashCard.getTerm());
        assertEquals("Def", flashCard.getDefinition());
        assertEquals(4, flashCard.getErrors());
        flashCard.setErrors(7);
        assertEquals(7, flashCard.getErrors());
    }


}