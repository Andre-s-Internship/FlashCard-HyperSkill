package org.example;

import java.io.*;
import java.util.List;
import java.util.Scanner;

public class FlashCardArchive {

    private static final Scanner scanner = new Scanner(System.in);
    private static final StringBuilder log = new StringBuilder();
    private static List<String> terminalArguments;
    private static final FlashCardSet flashCards = new FlashCardSet();
    private static final String IMPORT_COMMAND = "-import";
    private static final String MENU_ACTIONS = "Input the action (add, remove, import, export, ask, exit, log, hardest card, reset stats):";
    private static final String ACTION_ADD = "add";
    private static final String ACTION_REMOVE = "remove";
    private static final String ACTION_IMPORT = "import";
    private static final String ACTION_EXPORT = "export";
    private static final String ACTION_ASK = "ask";
    private static final String ACTION_EXIT = "exit";
    private static final String ACTION_LOG = "log";
    private static final String ACTION_HARDEST_CARD = "hardest card";
    private static final String ACTION_RESET_STATS = "reset stats";

    public static FlashCardSet getFlashCards() {
        return flashCards;
    }

    public static void main(String[] args) throws IOException {
        terminalArguments = List.of(args);
        importSetup(terminalArguments);
        menu();
    }

    static void menu() throws IOException {
        System.out.println(writeLog(LogState.OUTPUT, MENU_ACTIONS));
        String input = scanner.nextLine();
        writeLog(LogState.INPUT, input);
        switch (input) {
            case ACTION_ADD: {
                addCard();
                menu();
            }
            case ACTION_REMOVE: {
                removeCard();
                menu();
            }
            case ACTION_IMPORT: {
                System.out.println(writeLog(LogState.OUTPUT, "File name:"));
                String filePath = scanner.nextLine();
                writeLog(LogState.INPUT, filePath);
                importCards(filePath);
                menu();
            }
            case ACTION_EXPORT: {
                System.out.println(writeLog(LogState.OUTPUT, "File name:"));
                String fileName = scanner.nextLine();
                writeLog(LogState.INPUT, fileName);
                exportCards(fileName);
                menu();
            }
            case ACTION_ASK: {
                ask();
                menu();
            }
            case ACTION_EXIT: {
                System.out.println(writeLog(LogState.OUTPUT, "Bye bye!"));
                if (!terminalArguments.isEmpty() && terminalArguments.contains("-export")) {
                    for (int i = 0; i < terminalArguments.size(); i++) {
                        if (terminalArguments.get(i).equals("-export")) {
                            exportCards(terminalArguments.get(i + 1));
                            break;
                        }
                    }
                }
                System.exit(0);
            }
            case ACTION_LOG: {
                exportLog();
                menu();
            }
            case ACTION_HARDEST_CARD: {
                hardestCard();
                menu();
            }
            case ACTION_RESET_STATS: {
                resetErrors();
                menu();
            }
        }
    }

    static void addCard() {
        String term;
        String def;
        System.out.println(writeLog(LogState.OUTPUT, "The Card:"));
        term = scanner.nextLine();
        writeLog(LogState.INPUT, term);

        if (flashCards.containsTerm(term)) {
            System.out.println(writeLog(LogState.OUTPUT, "The card \"" + term + "\" already exists."));
            return;
        }
        System.out.println(writeLog(LogState.OUTPUT, "The definition of the card:"));
        def = scanner.nextLine();
        writeLog(LogState.INPUT, def);
        if (flashCards.containsDefinition(def)) {
            System.out.println(writeLog(LogState.OUTPUT, "The definition \"" + def + "\" already exists."));
            return;
        }
        flashCards.addFlashCard(new FlashCard(term, def));
        System.out.println(writeLog(LogState.OUTPUT, "The pair (\"" + term + "\":\"" + def + "\") has been added"));
    }

    static void removeCard() {
        System.out.println(writeLog(LogState.OUTPUT, "Which Card?"));
        String term = scanner.nextLine();
        writeLog(LogState.INPUT, term);

        if (!flashCards.containsTerm(term)) {
            System.out.println(writeLog(LogState.OUTPUT, "Can't remove \"" + term + "\": there is no such card."));
        } else {
            flashCards.getInstance().remove(flashCards.findByTerm(term));
            System.out.println(writeLog(LogState.OUTPUT, "The card has been removed."));
        }
    }

    static void importCards(String filePath) throws IOException {
        Scanner sc = null;
        try {
            sc = new Scanner(new File(filePath));
        } catch (FileNotFoundException e) {
            System.out.println(writeLog(LogState.OUTPUT, "\"File not found.\""));
            menu();
        }
        int count = 0;
        while (true) {
            assert sc != null;
            if (!sc.hasNextLine()) break;
                String term = sc.nextLine();
                writeLog(LogState.INPUT, term);

            if (!sc.hasNextLine()) break;
                String def = sc.nextLine();
                writeLog(LogState.INPUT, def);

            if (!sc.hasNextLine()) break;
                int errors = Integer.parseInt(sc.nextLine());
                writeLog(LogState.INPUT, errors + "");

            if (flashCards.containsTerm(term)) {
                flashCards.changeDefinition(term, def);
                count++;
                continue;
            }
            addCard(new FlashCard(term, def, errors));
            count++;
        }
        System.out.println(writeLog(LogState.OUTPUT, count + " cards have been loaded."));

    }

    static void exportCards(String fileName) throws IOException {
        FileWriter fileWriter = new FileWriter(fileName);
        BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
        int size = flashCards.getInstance().size();

        for (FlashCard flashCard : flashCards.getInstance()) {
            bufferedWriter.write(flashCard.getTerm() + "\n" + flashCard.getDefinition() + "\n" + flashCard.getErrors() + "\n");
        }
        bufferedWriter.close();
        System.out.println(writeLog(LogState.OUTPUT, size + " cards have been saved."));
    }

    static void ask() {
        System.out.println(writeLog(LogState.OUTPUT, "How many times to ask?"));
        int count = scanner.nextInt();
        writeLog(LogState.INPUT, count + "");
        scanner.nextLine();

        for (int i = 0; i < count; i++) {
            FlashCard flashCard = flashCards.getInstance().get(i % flashCards.getInstance().size());
            System.out.println(writeLog(LogState.OUTPUT, "Print the definition of \"" + flashCard.getTerm() + "\":"));

            String answer = scanner.nextLine();
            writeLog(LogState.INPUT, answer);

            if (flashCard.getDefinition().equals(answer)) {
                System.out.println(writeLog(LogState.OUTPUT, "Correct!"));

            }
            else if (flashCards.containsDefinition(answer)) {
                System.out.println(writeLog(LogState.OUTPUT, "Wrong. The right answer is \"" + flashCard.getDefinition() + "\"," + " but your definition is correct for \"" + flashCards.findByDefinition(answer).getTerm() + "\"."));
                flashCard.incrementError();
            }
            else {
                System.out.println(writeLog(LogState.OUTPUT, "Wrong. The right answer is \"" + flashCard.getDefinition() + "\"."));
                flashCard.incrementError();
            }
        }
    }

    static boolean addCard(FlashCard flashCard) {
        if (!flashCards.addFlashCard(new FlashCard(flashCard.getTerm(), flashCard.getDefinition(), flashCard.getErrors()))) {
            return true;
        } else {
            FlashCard flashCard1 = flashCards.findByTerm(flashCard.getTerm());
            flashCard1.setDefinition(flashCard.getDefinition());
        }
        return true;
    }

    static void hardestCard() {
        List<FlashCard> flashCardList = flashCards.findHighestErrors();
        if (flashCardList.isEmpty()) {
            System.out.println(writeLog(LogState.OUTPUT, "There are no cards with errors."));
        }
        else if (flashCardList.size() == 1) {
            System.out.println(writeLog(LogState.OUTPUT, "The hardest card is \"" + flashCardList.get(0).getTerm() + "\". You have " + flashCardList.get(0).getErrors() + " errors answering it."));
        }
        else {
            StringBuilder terms = new StringBuilder();
            int errorCount = 0;
            for (FlashCard flashCard : flashCardList) {
                terms.append("\"").append(flashCard.getTerm()).append("\",");
                errorCount = flashCard.getErrors();
            }

            terms.deleteCharAt(terms.length() - 1);
            terms.append(".");
            System.out.println(writeLog(LogState.OUTPUT, "The hardest cards are " + terms + " You have " + errorCount + " errors answering them."));
        }
    }

    static void resetErrors() {
        flashCards.resetErrors();
        System.out.println(writeLog(LogState.OUTPUT, "Card statistics have been reset."));
    }

    static void exportLog() {
        System.out.println(log);
    }

    static String writeLog(LogState state, String s) {
        log.append(state.toString());
        log.append(s);
        log.append("\n");
        return s;
    }

    public static void importSetup(List<String> terminalArguments) throws IOException {
        if (!terminalArguments.isEmpty() && terminalArguments.contains(IMPORT_COMMAND)) {
            for (int i = 0; i < terminalArguments.size(); i++) {
                if (terminalArguments.get(i).equals(IMPORT_COMMAND)) {
                    importCards(terminalArguments.get(i + 1));
                    break;
                }
            }
        }
    }
}
