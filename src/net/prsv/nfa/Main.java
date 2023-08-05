package net.prsv.nfa;

import java.io.File;
import java.util.Scanner;

public class Main {
    
    public static void main(String[] args) {
        if(args.length == 0) {
            System.out.println("Machine description not specified, exiting...");
            return;
        }
        File machineFile = new File(args[0]);
        System.out.println("Parsing machine description...");
        MachineParser mp = new MachineParser(machineFile);
        StateMachine machine = mp.parse();
        System.out.println(machine.summary());

        boolean quit = false;
        Scanner s = new Scanner(System.in);

        while(!quit) {
            System.out.println("Enter the input string, type 'LIST' to list config or 'QUIT' to exit: ");
            System.out.print("> ");
            String input = s.nextLine();
            if(input.trim().equalsIgnoreCase("quit")) {
                quit = true;
                System.out.println("Bye!");
                continue;
            } else if(input.trim().equalsIgnoreCase("list")) {
                System.out.println(machine.config());
                continue;
            }
            boolean accepted = machine.run(input);
            if (accepted) {
                System.out.println("String '" + input + "' accepted.");
            } else {
                System.out.println("String '" + input + "' rejected.");
            }
        }
        s.close();
    }
}