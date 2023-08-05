package net.prsv.nfa;

import java.util.HashMap;
import java.util.HashSet;

public class StateMachine {
    private final HashSet<String> states;
    private final HashSet<Character> alphabet;
    private final HashSet<String> acceptStates;
    private final HashMap<Pair, HashSet<String>> transitions;
    private final HashMap<String, HashSet<String>> nullTransitions;
    private final HashSet<String> startStates;

    public StateMachine(HashSet<String> states,
                        HashSet<Character> alphabet,
                        HashSet<String> acceptStates,
                        HashMap<Pair, HashSet<String>> transitions,
                        HashMap<String, HashSet<String>> nullTransitions,
                        HashSet<String> startStates) {
        this.states = states;
        this.alphabet = alphabet;
        this.acceptStates = acceptStates;
        this.transitions = transitions;
        this.nullTransitions = nullTransitions;
        this.startStates = startStates;
    }

    public String config() {
        StringBuilder sb = new StringBuilder();
        sb.append("===== NFA configuration =====\n");
        sb.append("States: ");
        states.forEach(state -> sb.append(state).append(" "));
        sb.append("\nStart states: ");
        startStates.forEach(state -> sb.append(state).append(" "));
        sb.append("\nAccept states: ");
        acceptStates.forEach(state -> sb.append(state).append(" "));
        sb.append("\nAlphabet: ");
        alphabet.forEach(symbol -> sb.append(symbol).append(" "));
        sb.append("\nTransitions:\n");
        transitions.keySet().forEach(key -> {
            sb.append("(").append(key).append(") -> ");
            transitions.get(key).forEach(state -> sb.append(state).append(" "));
            sb.append("\n");
        });
        if (!nullTransitions.isEmpty()) {
            sb.append("\nNull transitions: ");
            nullTransitions.keySet().forEach(key -> {
                sb.append(key).append(" -> ");
                nullTransitions.get(key).forEach(state -> sb.append(state).append(" "));
                sb.append("\n");
            });
        }
        return sb.toString();
    }

    public String summary() {
        StringBuilder sb = new StringBuilder();
        sb.append("===== NFA summary =====\n");
        sb.append("States: ").
                append(states.size()).
                append(", accept states: ").
                append(acceptStates.size()).
                append(", transitions: ").
                append(transitions.size()).
                append(", null transitions: ").
                append(nullTransitions.size()).
                append("\n");
        sb.append("Alphabet: ");
        alphabet.forEach(symbol -> sb.append(symbol).append(" "));
        sb.append("\n");
        return sb.toString();
    }

    public boolean run(String input) {
        System.out.print("Start states: ");
        startStates.forEach(state -> System.out.print(state + " "));
        System.out.println();
        HashSet<String> currentStates = startStates;
        if(!input.equals("")) {
            for (int i = 0; i < input.length(); i++) {
                // if any of the current states has null transitions defined, add them to the current states
                for (String state : currentStates) {
                    if (nullTransitions.containsKey(state)) {
                        currentStates.addAll(nullTransitions.get(state));
                    }
                }
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