package net.prsv.nfa;

import java.util.HashMap;
import java.util.HashSet;

public class StateMachine {
    private final HashSet<String> states;
    private final HashSet<Character> alphabet;
    private final HashSet<String> acceptStates;
    private final HashMap<Pair, HashSet<String>> transitions;
    private final HashSet<String> startStates;

    public StateMachine(HashSet<String> states,
                        HashSet<Character> alphabet,
                        HashSet<String> acceptStates,
                        HashMap<Pair, HashSet<String>> transitions,
                        HashSet<String> startStates) {
        this.states = states;
        this.alphabet = alphabet;
        this.acceptStates = acceptStates;
        this.transitions = transitions;
        this.startStates = startStates;
    }

    public void printConfig() {
        System.out.println("===== DFA configuration =====");
        System.out.print("States: ");
        states.forEach(state -> System.out.print(state + " "));
        System.out.println("\nStart states: ");
        startStates.forEach(state -> System.out.print(state + " "));
        System.out.print("\nAccept states: ");
        acceptStates.forEach(state -> System.out.print(state + " "));
        System.out.print("\nAlphabet: ");
        alphabet.forEach(symbol -> System.out.print(symbol + " "));
        System.out.println("\nTransitions: ");
        transitions.keySet().forEach(key -> {
            System.out.print("(" + key + ") -> ");
            transitions.get(key).forEach(state -> System.out.print(state + " "));
            System.out.println();
        });
    }

    public void printSummary() {
        System.out.println("===== DFA summary =====");
        System.out.println("States: " + states.size() + ", accept states: " +
                acceptStates.size() + ", transitions: " +
                transitions.size());
        System.out.print("Alphabet: ");
        alphabet.forEach(symbol -> System.out.print(symbol + " "));
        System.out.println();
    }

    public boolean run(String input) {
        System.out.print("Start states: ");
        startStates.forEach(state -> System.out.print(state + " "));
        System.out.println();
        HashSet<String> currentStates = startStates;
        if(!input.equals("")) {
            for (int i = 0; i < input.length(); i++) {
                HashSet<String> newStates = new HashSet<>();
                char symbol = input.charAt(i);
                System.out.print("Current symbol: '" + symbol + "'. ");
                if (!alphabet.contains(symbol)) {
                    System.out.println("Symbol not defined");
                    return false;
                }
                for (String state : currentStates) {
                    HashSet<String> states = transitions.get(new Pair(state, symbol));
                    if (states != null) {
                        newStates.addAll(states);
                    }
                }
                currentStates = newStates;
                System.out.print("Moving to states: ");
                newStates.forEach(state -> System.out.print(state + " "));
                System.out.println();
            }
        }
        currentStates.retainAll(acceptStates);
        return !currentStates.isEmpty();
    }

}