package org.example;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.*;

import static org.junit.jupiter.api.Assertions.*;

class FlashCardArchiveTest {

    @Test
    @DisplayName("Test for add card 1")
    void test1() {
        String input = "term1\ndefinition1\n";
        InputStream in = new ByteArrayInputStream(input.getBytes());
        System.setIn(in);

        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));

        FlashCardArchive.addCard();

        String expectedOutput = "The Card:\nThe definition of the card:\nThe pair (\"term1\":\"definition1\") has been added\n";
        assertEquals(expectedOutput, outContent.toString());
    }

    @Test
    @DisplayName("Test for add card 2")
    void test2() {
        String input = "term1\nDef\n";
        InputStream in = new ByteArrayInputStream(input.getBytes());
        System.setIn(in);
        FlashCardArchive.addCard(new FlashCard("Term", "Def"));

        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));

        FlashCardArchive.addCard();

        String expectedOutput = "The definition \"Def\" already exists.";
        assertTrue(outContent.toString().contains(expectedOutput));
    }

    @Test
    @DisplayName("Test for add card 3")
    void test3() {
        String input = "Term\nDefinition\n";
        InputStream in = new ByteArrayInputStream(input.getBytes());
        System.setIn(in);
        FlashCardArchive.addCard(new FlashCard("Term", "Def"));

        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));

        FlashCardArchive.addCard();

        String expectedOutput = "The card \"Term\" already exists.";
        assertTrue(outContent.toString().contains(expectedOutput));
    }

    @Test
    @DisplayName("Test for remove card 1")
    void test4() {
        String input = "Term\n";
        InputStream in = new ByteArrayInputStream(input.getBytes());
        System.setIn(in);
        FlashCardArchive.addCard(new FlashCard("Term", "Def"));

        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));

        FlashCardArchive.removeCard();

        String expectedOutput = "The card has been removed.";
        assertTrue(outContent.toString().contains(expectedOutput));
    }

    @Test
    @DisplayName("Test for remove card 2")
    void test5() {
        String input = "Term\n";
        InputStream in = new ByteArrayInputStream(input.getBytes());
        System.setIn(in);
        FlashCardArchive.addCard(new FlashCard("Term1", "Def"));

        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));

        FlashCardArchive.removeCard();

        String expectedOutput = "Can't remove \"Term\": there is no such card.";
        assertTrue(outContent.toString().contains(expectedOutput));
    }

    @Test
    @DisplayName("Test for import cards 1")
    void test6() throws IOException {
        FlashCardArchive.importCards("src/main/resources/import.txt");
        assertTrue(FlashCardArchive.getFlashCards().containsTerm("Armenia"));
        assertTrue(FlashCardArchive.getFlashCards().containsTerm("Georgia"));
        assertTrue(FlashCardArchive.getFlashCards().containsDefinition("Yerevan"));
        assertTrue(FlashCardArchive.getFlashCards().containsDefinition("Tbilisi"));
    }

    @Test
    @DisplayName("Test for import cards 2")
    void test16() throws IOException {
        FlashCardArchive.addCard(new FlashCard("Armenia", "Gyumri"));
        FlashCardArchive.addCard(new FlashCard("Georgia", "Batumi"));
        FlashCardArchive.importCards("src/main/resources/import.txt");
        assertTrue(FlashCardArchive.getFlashCards().containsTerm("Armenia"));
        assertTrue(FlashCardArchive.getFlashCards().containsTerm("Georgia"));
        assertTrue(FlashCardArchive.getFlashCards().containsDefinition("Yerevan"));
        assertTrue(FlashCardArchive.getFlashCards().containsDefinition("Tbilisi"));
    }


    @Test
    @DisplayName("Test for Hardest cards 1")
    void test7() {
        FlashCardArchive.addCard(new FlashCard("Term", "Def", 8));
        FlashCardArchive.addCard(new FlashCard("Term1", "Def1", 0));

        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));

        FlashCardArchive.hardestCard();

        String expectedOutput = "The hardest card is \"Term\". You have 8 errors answering it.";
        assertTrue(outContent.toString().contains(expectedOutput));
    }

    @Test
    @DisplayName("Test for import cards 2")
    void test8() {
        FlashCardArchive.addCard(new FlashCard("Term", "Def"));
        FlashCardArchive.addCard(new FlashCard("Term1", "Def1"));

        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));

        FlashCardArchive.hardestCard();

        String expectedOutput = "There are no cards with errors.";
        assertTrue(outContent.toString().contains(expectedOutput));
    }

    @Test
    @DisplayName("Test for import cards 3")
    void test9() {
        FlashCardArchive.addCard(new FlashCard("Term", "Def", 3));
        FlashCardArchive.addCard(new FlashCard("Term1", "Def1", 3));

        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));

        FlashCardArchive.hardestCard();

        String expectedOutput = "The hardest cards are \"Term\",\"Term1\". You have 3 errors answering them.";
        assertTrue(outContent.toString().contains(expectedOutput));
    }

    @Test
    @DisplayName("Test for reset errors")
    void test10() {
        FlashCardArchive.addCard(new FlashCard("Term", "Def", 3));
        FlashCardArchive.addCard(new FlashCard("Term1", "Def1", 6));

        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));

        FlashCardArchive.resetErrors();

        String expectedOutput = "Card statistics have been reset.";
        assertTrue(outContent.toString().contains(expectedOutput));
        assertTrue(FlashCardArchive.getFlashCards().findHighestErrors().isEmpty());
    }

    @Test
    @DisplayName("Test for log")
    void test11() {
        FlashCardArchive.addCard(new FlashCard("Term", "Def", 3));
        FlashCardArchive.addCard(new FlashCard("Term1", "Def1", 6));

        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));

        FlashCardArchive.resetErrors();
        FlashCardArchive.exportLog();
        String expectedOutput = "OUTPUT: Card statistics have been reset";
        assertTrue(outContent.toString().contains(expectedOutput));
        assertTrue(FlashCardArchive.getFlashCards().findHighestErrors().isEmpty());
    }

    @Test
    @DisplayName("Test for ask 1")
    void test12() {
        String input = "1\nDef";
        InputStream in = new ByteArrayInputStream(input.getBytes());
        System.setIn(in);
        FlashCard flashCard = new FlashCard("Term1", "Def");
        FlashCardArchive.addCard(flashCard);

        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));

        FlashCardArchive.ask();

        String expectedOutput1 = "Print the definition of \"Term1\":";
        String expectedOutput2 = "Correct";
        String expectedOutput3 = "How many times to ask?";
        assertTrue(outContent.toString().contains(expectedOutput1) &&
                outContent.toString().contains(expectedOutput2) &&
                outContent.toString().contains(expectedOutput3));
        assertEquals(0, flashCard.getErrors());
    }

    @Test
    @DisplayName("Test for ask 2")
    void test13() {
        String input = "1\nDef1";
        InputStream in = new ByteArrayInputStream(input.getBytes());
        System.setIn(in);
        FlashCard flashCard = new FlashCard("Term1", "Def", 5);
        FlashCardArchive.addCard(flashCard);

        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));

        FlashCardArchive.ask();

        String expectedOutput1 = "Print the definition of \"Term1\":";
        String expectedOutput2 = "How many times to ask?";
        String expectedOutput3 = "Wrong. The right answer is \"Def\".";
        assertTrue(outContent.toString().contains(expectedOutput1) &&
                outContent.toString().contains(expectedOutput2) &&
                outContent.toString().contains(expectedOutput3));
    }

    @Test
    @DisplayName("Test for ask 3")
    void test14() {
        String input = "1\nDef1";
        InputStream in = new ByteArrayInputStream(input.getBytes());
        System.setIn(in);
        FlashCard flashCard = new FlashCard("Term", "Def");
        FlashCard flashCard1 = new FlashCard("Term1", "Def1");
        FlashCardArchive.addCard(flashCard);
        FlashCardArchive.addCard(flashCard1);

        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));

        FlashCardArchive.ask();

        String expectedOutput1 = "Print the definition of \"Term\":";
        String expectedOutput2 = "How many times to ask?";
        String expectedOutput3 = "Wrong. The right answer is \"Def\", but your definition is correct for \"Term1\".";
        assertTrue(outContent.toString().contains(expectedOutput1) &&
                outContent.toString().contains(expectedOutput2) &&
                outContent.toString().contains(expectedOutput3));
    }


}