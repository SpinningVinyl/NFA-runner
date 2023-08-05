package net.prsv.nfa;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Scanner;

public class MachineParser {
    private final HashSet<String> states = new HashSet<>();
    private final HashSet<Character> alphabet = new HashSet<>();
    private final HashSet<String> acceptStates = new HashSet<>();
    private final HashMap<Pair, HashSet<String>> transitions = new HashMap<>();
    private final HashMap<String, HashSet<String>> nullTransitions = new HashMap<>();

    private final File inputFile;
    private HashSet<String> startStates;

    public MachineParser(File inputFile) {
        this.inputFile = inputFile;
    }

    public StateMachine parse() {
        try(Scanner scanner = new Scanner(inputFile)) {
            String input = scanner.useDelimiter("\\Z").next();
            String[] lines = input.split("\\R");
            for(String line : lines) {
                if (line.charAt(0) == '#') { // lines beginning with # are comments
                    continue;
                }
                if (line.contains(":")) {
                    String[] rightSide = line.substring(line.indexOf(':') + 1).split(",");
                    switch(line.charAt(0)) {
                        case 'Q':
                            for (String state : rightSide) {
                                states.add(state.strip());
                            }
                            break;
                        case 'L':
                            for (String symbol : rightSide) {
                                alphabet.add(symbol.strip().charAt(0));
                            }
                            break;
                        case 'A':
                            for (String acceptState : rightSide) {
                                acceptState = acceptState.strip();
                                if (states.contains(acceptState)) {
                                    acceptStates.add(acceptState.strip());
                                } else {
                                    throw new RuntimeException("Fatal error: accept state " + acceptState + " not found in states");
                                }
                            }
                            break;
                        case 'S':
                            startStates = new HashSet<>();
                            for (String startState : rightSide) {
                                startState = startState.strip();
                                if (states.contains(startState)) {
                                    startStates.add(startState);
                                } else {
                                    throw new RuntimeException("Fatal error: start state " + startState + " not found in states");
                                }
                            }
                    }
                } else if (line.contains("->")) {
                    HashSet<String> newStates = new HashSet<>();
                    String[] temp = line.split("->");
                    String[] rightSide = temp[1].split(",");
                    for (String s : rightSide) {
                        String newState = s.strip();
                        if (!states.contains(newState)) {
                            throw new RuntimeException("Fatal error: state " + newState + " not defined in transition " + line);
                        }
                        newStates.add(newState);
                    }
                    if (!temp[0].contains(",")) {
                        String oldState = temp[0].strip();
                        if (!states.contains(oldState)) {
                            throw new RuntimeException("Fatal error: state " + oldState + " not defined in transition " + line);
                        }
                        nullTransitions.put(oldState, newStates);
                    } else {
                        String[] leftSide = temp[0].split(",");
                        String oldState = leftSide[0].strip();
                        if (!states.contains(oldState)) {
                            throw new RuntimeException("Fatal error: state " + oldState + " not defined in transition " + line);
                        }
                        for (int i = 1; i < leftSide.length; i++) {
                            char symbol = leftSide[i].strip().charAt(0);
                            if (!alphabet.contains(symbol)) {
                                throw new RuntimeException("Fatal error: symbol " + symbol + " not defined in transition " + line);
                            }
                            transitions.put(new Pair(oldState, symbol), newStates);
                        }
                    }
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
//        for (String state : states) {
//            for (char symbol : alphabet) {
//                Pair pair = new Pair(state, symbol);
//                if(!transitions.containsKey(pair)) {
//                    System.out.println("Warning: transition function incomplete.\n\tTransition for pair (" + pair + ") not defined.");
//                }
//            }
//        }
        if(startStates.isEmpty()) {
            throw new RuntimeException("Fatal error: no start states defined.");
        }
        if(acceptStates.isEmpty()) {
            throw new RuntimeException("Fatal error: no accept states defined.");
        }
        return new StateMachine(states, alphabet, acceptStates, transitions, nullTransitions, startStates);
    }

}
