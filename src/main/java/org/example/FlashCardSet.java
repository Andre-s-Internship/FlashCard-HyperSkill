package org.example;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class FlashCardSet {

    private final List<FlashCard> flashCardSet;
    private final List<String> terms;
    private final List<String> definitions;
    private final Comparator<FlashCard> flashCardComparator = Comparator.comparingInt(FlashCard::getErrors).reversed();


    FlashCardSet() {
        flashCardSet = new ArrayList<>();
        terms = new ArrayList<>();
        definitions = new ArrayList<>();
    }

    public boolean addFlashCard(FlashCard flashCard) {
        if (!containsTerm(flashCard.getTerm()) && !containsDefinition(flashCard.getDefinition())) {
            flashCardSet.add(flashCard);
            terms.add(flashCard.getTerm());
            definitions.add(flashCard.getDefinition());
            return true;
        }
        return false;
    }

    public boolean containsTerm(String term) {
        return !flashCardSet.isEmpty() && terms.contains(term);
    }

    public boolean containsDefinition(String def) {
        return !flashCardSet.isEmpty() && definitions.contains(def);
    }

    public FlashCard findByDefinition(String def) {
        for (FlashCard flashCard : flashCardSet) {
            if (flashCard.getDefinition().equals(def)) {
                return flashCard;
            }
        }
        return null;
    }

    public FlashCard findByTerm(String term) {
        for (FlashCard flashCard : flashCardSet) {
            if (flashCard.getTerm().equals(term)) {
                return flashCard;
            }
        }
        return null;
    }

    public List<FlashCard> getInstance() {
        return flashCardSet;
    }

    public void changeDefinition(String term, String def) {
        FlashCard flashCard = findByTerm(term);
        definitions.remove(flashCard.getDefinition());
        flashCard.setDefinition(def);
        definitions.add(def);
    }

    public List<FlashCard> findHighestErrors() {
        if (flashCardSet.isEmpty()) {
            return new ArrayList<>();
        }
        flashCardSet.sort(flashCardComparator);
        int highestError = flashCardSet.get(0).getErrors();
        List<FlashCard> mostErrors = new ArrayList<>();
        if (highestError == 0) {
            return new ArrayList<>();
        }
        for (FlashCard flashCard : flashCardSet) {
            if (flashCard.getErrors() == highestError) {
                mostErrors.add(flashCard);
            }
        }

        return mostErrors;
    }

    public void resetErrors() {
        for (FlashCard flashCard : flashCardSet) {
            flashCard.setErrors(0);
        }
    }
}
