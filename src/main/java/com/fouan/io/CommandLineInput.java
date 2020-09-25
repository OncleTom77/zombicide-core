package com.fouan.io;

import javax.inject.Named;
import java.util.InputMismatchException;
import java.util.Scanner;

@Named
public class CommandLineInput implements Input {
    private final Output output;
    private final Scanner scanner;

    public CommandLineInput(Output output) {
        this.output = output;
        scanner = new Scanner(System.in);
        scanner.useDelimiter("\\r?\\n|\\r");
    }

    @Override
    public int read() {
        do {
            try {
                return scanner.nextInt();
            } catch (InputMismatchException ignored) {
                scanner.next();
            }
            output.display("Try again");
        } while (true);
    }
}
