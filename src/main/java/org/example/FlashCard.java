package org.example;

public class FlashCard {

    private final String term;
    private String definition;

    private int errors;

    FlashCard(String card, String def) {
        this.term = card;
        definition = def;
        errors = 0;
    }

    FlashCard(String card, String def, int errors) {
        this.term = card;
        definition = def;
        this.errors = errors;
    }

    public String getTerm() {
        return term;
    }

    public String getDefinition() {
        return definition;
    }

    public void setDefinition(String def) {
        definition = def;
    }

    public void incrementError() {
        errors += 1;
    }

    public int getErrors() {
        return errors;
    }

    public void setErrors(int error) {
        this.errors = error;
    }
}
