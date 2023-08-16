package org.example;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class FlashCardSetTest {

    @Test
    @DisplayName("Test for creation")
    void tests() {
        FlashCardSet flashCardSet = new FlashCardSet();
        assertNotNull(flashCardSet.getInstance());
    }

    @Test
    @DisplayName("Test for add card")
    void tests1() {
        FlashCardSet flashCardSet = new FlashCardSet();
        FlashCard flashCard = new FlashCard("term", "definition");
        flashCardSet.addFlashCard(flashCard);
        assertNotNull(flashCardSet.getInstance());
        assertTrue(flashCardSet.containsTerm("term"));
        assertTrue(flashCardSet.containsDefinition("definition"));
    }

    @Test
    @DisplayName("Test for find by term")
    void tests2() {
        FlashCardSet flashCardSet = new FlashCardSet();
        FlashCard flashCard = new FlashCard("term", "definition");
        flashCardSet.addFlashCard(flashCard);
        FlashCard newFlashCard = flashCardSet.findByTerm("term");
        assertNotNull(newFlashCard);
        assertEquals(newFlashCard, flashCard);
        assertNotNull(flashCardSet.getInstance());
        assertTrue(flashCardSet.containsTerm("term"));
        assertTrue(flashCardSet.containsDefinition("definition"));
    }

    @Test
    @DisplayName("Test for find by definition")
    void tests3() {
        FlashCardSet flashCardSet = new FlashCardSet();
        FlashCard flashCard = new FlashCard("term", "definition");
        flashCardSet.addFlashCard(flashCard);
        FlashCard newFlashCard = flashCardSet.findByDefinition("definition");
        assertNotNull(newFlashCard);
        assertEquals(newFlashCard, flashCard);
        assertNotNull(flashCardSet.getInstance());
        assertTrue(flashCardSet.containsTerm("term"));
        assertTrue(flashCardSet.containsDefinition("definition"));
    }

    @Test
    @DisplayName("Test for find by term")
    void tests4() {
        FlashCardSet flashCardSet = new FlashCardSet();
        FlashCard flashCard = new FlashCard("term", "definition");
        flashCardSet.addFlashCard(flashCard);
        FlashCard newFlashCard = flashCardSet.findByTerm("Card");
        assertNull(newFlashCard);
        assertNotNull(flashCardSet.getInstance());
        assertTrue(flashCardSet.containsTerm("term"));
        assertTrue(flashCardSet.containsDefinition("definition"));
    }

    @Test
    @DisplayName("Test for find by definition")
    void tests5() {
        FlashCardSet flashCardSet = new FlashCardSet();
        FlashCard flashCard = new FlashCard("term", "definition");
        flashCardSet.addFlashCard(flashCard);
        FlashCard newFlashCard = flashCardSet.findByDefinition("Def");
        assertNull(newFlashCard);
        assertNotNull(flashCardSet.getInstance());
        assertTrue(flashCardSet.containsTerm("term"));
        assertTrue(flashCardSet.containsDefinition("definition"));
    }

    @Test
    @DisplayName("Test for change definition")
    void tests6() {
        FlashCardSet flashCardSet = new FlashCardSet();
        FlashCard flashCard = new FlashCard("term", "definition");
        flashCardSet.addFlashCard(flashCard);
        flashCardSet.changeDefinition("term", "new definition");
        assertEquals("new definition", flashCard.getDefinition());
    }

    @Test
    @DisplayName("Test for highest errors")
    void tests7() {
        FlashCardSet flashCardSet = new FlashCardSet();
        FlashCard flashCard = new FlashCard("term", "definition");
        FlashCard flashCard1 = new FlashCard("term1", "definition1", 8);
        flashCardSet.addFlashCard(flashCard);
        flashCardSet.addFlashCard(flashCard1);
        assertEquals(1, flashCardSet.findHighestErrors().size());
        assertEquals(flashCard1, flashCardSet.findHighestErrors().get(0));
    }

    @Test
    @DisplayName("Test for get reset errors")
    void tests8() {
        FlashCardSet flashCardSet = new FlashCardSet();
        FlashCard flashCard = new FlashCard("term", "definition", 7);
        FlashCard flashCard1 = new FlashCard("term1", "definition1", 8);
        flashCardSet.addFlashCard(flashCard);
        flashCardSet.addFlashCard(flashCard1);
        flashCardSet.resetErrors();
        assertEquals(0, flashCard.getErrors());
        assertEquals(0, flashCard1.getErrors());
        assertTrue(flashCardSet.findHighestErrors().isEmpty());
    }

    @Test
    @DisplayName("Test for highest errors")
    void tests9() {
        FlashCardSet flashCardSet = new FlashCardSet();
        FlashCard flashCard = new FlashCard("term", "definition", 7);
        FlashCard flashCard1 = new FlashCard("term1", "definition1", 8);
        assertTrue(flashCardSet.findHighestErrors().isEmpty());
    }

    @Test
    @DisplayName("Test for add card")
    void tests10() {
        FlashCardSet flashCardSet = new FlashCardSet();
        FlashCard flashCard = new FlashCard("term", "definition", 7);
        FlashCard flashCard1 = new FlashCard("term1", "definition", 8);
        flashCardSet.addFlashCard(flashCard);
        assertFalse(flashCardSet.addFlashCard(flashCard1));
        assertEquals("term", flashCardSet.findByDefinition("definition").getTerm());
    }

}