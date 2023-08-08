package org.example;

import java.io.*;
import java.util.List;
import java.util.Scanner;

public class Main {

    private static Scanner scanner = new Scanner(System.in);
    private static StringBuilder log = new StringBuilder();
    private static List<String> terminalArguments;
    private static FlashCardSet flashCards = new FlashCardSet();
    public static void main(String[] args) throws IOException {
        terminalArguments = List.of(args);
        if (!terminalArguments.isEmpty() && terminalArguments.contains("-import")) {
            for (int i = 0; i < terminalArguments.size(); i++) {
                if (terminalArguments.get(i).equals("-import")) {
                    importCards(terminalArguments.get(i + 1));
                    break;
                }
            }
        }
        menu();
    }

    static void menu() throws IOException {
        System.out.println("Input the action (add, remove, import, export, ask, exit, log, hardest card, reset stats):");
        log.append("Input the action (add, remove, import, export, ask, exit, log, hardest card, reset stats):").append("\n");
        String input = scanner.nextLine();
        log.append(input).append("\n");
        switch (input) {
            case "add": {
                addCard();
                menu();
            }
            case "remove": {
                removeCard();
                menu();
            }
            case "import": {
                System.out.println("File name:");
                log.append("OUTPUT: File name.").append("\n");
                String filePath = scanner.nextLine();
                log.append("INPUT: ").append(filePath).append("\n");
                importCards(filePath);
                menu();
            }
            case "export": {
                System.out.println("File name:");
                log.append("OUTPUT: File name").append("\n");
                String fileName = scanner.nextLine();
                log.append("INPUT: ").append(fileName).append("\n");
                exportCards(fileName);
                menu();
            }
            case "ask": {
                ask();
                menu();
            }
            case "exit": {
                System.out.println("Bye bye!");
                log.append("Bye bye").append("\n");
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
            case "log": {
                writeLog();
                menu();
            }
            case "hardest card": {
                hardestCard();
                menu();
            }
            case "reset stats": {
                resetErrors();
                menu();
            }
        }
    }


    static void addCard() {
        String term;
        String def;
        System.out.println("The Card:");
        log.append("OUTPUT: The card").append("\n");
        term = scanner.nextLine();
        log.append("INPUT: ").append(term).append("\n");
        if (flashCards.containsTerm(term)) {
            System.out.println("The card \""+ term + "\" already exists");
            log.append("OUTPUT: The card  \"").append(term).append("\" already exists").append("\n");
            return;
        }
        System.out.println("The definition of the card:");
        log.append("OUTPUT: The definition of the card:").append("\n");
        def = scanner.nextLine();
        log.append("INPUT: ").append(def).append("\n");
        if (flashCards.containsDefinition(def)) {
            System.out.println("The definition \""+ def + "\" already exists.");
            log.append("OUTPUT: The definition ").append(def).append(" already exists.").append("\n");
            return;
        }
        flashCards.addFlashCard(new FlashCard(term, def));
        System.out.println("The pair (\"" + term + "\":\"" + def + "\") has been added");
        log.append("OUTPUT: The pair (\"").append(term).append("\":\"").append(def).append("\") has been added").append("\n");
    }

    static void removeCard() {
        System.out.println("Which Card?");
        log.append("OUTPUT: Which Card?").append("\n");
        String term = scanner.nextLine();
        log.append("INPUT: ").append(term).append("\n");
        if (!flashCards.containsTerm(term)) {
            System.out.println("Can't remove \"" + term + "\": there is no such card.");
            log.append("OUTPUT: Can't remove \"").append(term).append("\": there is no such card.").append("\n");
        } else {
            flashCards.getInstance().remove(flashCards.findByTerm(term));
            System.out.println("The card has been removed.");
            log.append("OUTPUT: The card has been removed.").append("\n");
        }
    }

    static void importCards(String filePath) throws IOException {
        Scanner sc = null;
        try {
            sc = new Scanner(new File(filePath));
        } catch (FileNotFoundException e) {
            System.out.println("\"File not found.\"");
            log.append("OUTPUT: \"File not found.\"").append("\n");
            menu();
        }
        int count = 0;
        while(true) {
            assert sc != null;
            if (!sc.hasNextLine()) break;
            String term = sc.nextLine();
            log.append("INPUT: ").append(term).append("\n");
            if (!sc.hasNextLine()) break;
            String def = sc.nextLine();
            log.append("INPUT: ").append(def).append("\n");
            if (!sc.hasNextLine()) break;
            int errors = Integer.parseInt(sc.nextLine());
            log.append("INPUT: ").append(errors).append("\n");
            if(flashCards.containsTerm(term)) {
                flashCards.changeDefinition(term, def);
                count++;
                continue;
            }
            addCard(new FlashCard(term, def, errors));
            count++;
        }
        System.out.println(count + " cards have been loaded.");
        log.append("OUTPUT: ").append(count).append(" cards have been loaded.").append("\n");

    }
    static void exportCards(String fileName) throws IOException {
        FileWriter fileWriter = new FileWriter(fileName);
        BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
        int size = flashCards.getInstance().size();
        for (FlashCard flashCard : flashCards.getInstance()) {
            bufferedWriter.write(flashCard.getTerm() + "\n" + flashCard.getDefinition() + "\n"
                    + flashCard.getErrors() + "\n");
        }
        bufferedWriter.close();
        System.out.println(size + " cards have been saved.");
        log.append("OUTPUT: ").append(size).append(" cards have been saved.").append("\n");
    }

    static void ask() {
        System.out.println("How many times to ask?");
        log.append("OUTPUT: How many times to ask?").append("\n");
        int count = scanner.nextInt();
        log.append("INPUT: ").append(count).append("\n");
        scanner.nextLine();
        for (int i = 0; i < count; i++) {
            FlashCard flashCard = flashCards.getInstance().get(i % flashCards.getInstance().size());
            System.out.println("Print the definition of \"" + flashCard.getTerm() + "\":");
            log.append("OUTPUT: Print the definition of \"").append(flashCard.getTerm()).append("\":").append("\n");
            String answer = scanner.nextLine();
            log.append("INPUT: ").append(answer).append("\n");
            if(flashCard.getDefinition().equals(answer)) {
                System.out.println("Correct!");
                log.append("OUTPUT: Correct!").append("\n");

            } else if (flashCards.containsDefinition(answer)) {
                System.out.println("Wrong. The right answer is \""+ flashCard.getDefinition()+ "\"," +
                        " but your definition is correct for \"" +
                        flashCards.findByDefinition(answer).getTerm() + "\".");
                log.append("OUTPUT: Wrong. The right answer is \"").append(flashCard.getDefinition()).append("\",").append(" but your definition is correct for \"").append(flashCards.findByDefinition(answer).getTerm()).append("\".").append("\n");
                flashCard.incrementError();
            }
            else {
                System.out.println("Wrong. The right answer is \"" + flashCard.getDefinition() + "\".");
                log.append("OUTPUT: Wrong. The right answer is \"").append(flashCard.getDefinition()).append("\".").append("\n");
                flashCard.incrementError();
            }
        }
    }

    static boolean addCard(FlashCard flashCard) {
        if(!flashCards.addFlashCard(new FlashCard(flashCard.getTerm(),
                flashCard.getDefinition(), flashCard.getErrors()))){
            return true;
        } else {
            FlashCard flashCard1 = flashCards.findByTerm(flashCard.getTerm());
            flashCard1.setDefinition(flashCard.getDefinition());
        }
        return true;
    }

    static void hardestCard() {
        List<FlashCard> flashCardList = flashCards.findHighestErrors();
        if(flashCardList.isEmpty()) {
            System.out.println("There are no cards with errors.");
            log.append("OUTPUT: There are no cards with errors.").append("\n");
        } else if (flashCardList.size() == 1) {
            System.out.println("The hardest card is \"" + flashCardList.get(0).getTerm()
                    + "\". You have " + flashCardList.get(0).getErrors() + " errors answering it.");
            log.append("OUTPUT: The hardest card is \"").append(flashCardList.get(0).getTerm()).append("\". You have ").append(flashCardList.get(0).getErrors()).append(" errors answering it.").append("\n");
        } else {
            StringBuilder terms = new StringBuilder();
            int errorCount = 0;
            for (FlashCard flashCard : flashCardList) {
                terms.append("\"").append(flashCard.getTerm()).append("\",");
                errorCount = flashCard.getErrors();
            }
            terms.deleteCharAt(terms.length() - 1);
            terms.append(".");
            System.out.println("The hardest cards are " + terms +
                    " You have " + errorCount + " errors answering them.");
            log.append("OUTPUT: The hardest cards are ").append(terms).append(" You have ").append(errorCount).append(" errors answering them.").append("\n");
        }
    }

    static void resetErrors() {
        flashCards.resetErrors();
        System.out.println("Card statistics have been reset.");
        log.append("OUTPUT: Card statistics have been reset.").append("\n");
    }

    static void writeLog() throws IOException {
        System.out.println("File name:");
        log.append("OUTPUT: File name").append("\n");
        String fileName = scanner.nextLine();
        log.append("INPUT: ").append(fileName).append("\n");
        FileWriter fileWriter = new FileWriter(fileName);
        BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
        bufferedWriter.write(String.valueOf(log));
        bufferedWriter.close();
        System.out.println("The log has been saved.");
        log.append("OUTPUT: The log has been saved").append("\n");
    }


}