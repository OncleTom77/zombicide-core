package com.fouan.io;

import javax.inject.Named;
import java.util.InputMismatchException;
import java.util.Scanner;

@Named
public class CommandLineChoiceMaker implements ChoiceMaker {
    private final Output output;
    private final Scanner scanner;

    public CommandLineChoiceMaker(Output output) {
        this.output = output;
        scanner = new Scanner(System.in);
        scanner.useDelimiter("\\r?\\n|\\r");
    }

    @Override
    public int getChoice(int min, int max) {
        do {
            int choice = read();

            if (choice >= min && choice <= max) {
                return choice;
            }
            output.display("Try again");
        } while (true);
    }

    private int read() {
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
