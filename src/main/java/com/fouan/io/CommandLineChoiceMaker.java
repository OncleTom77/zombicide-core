package com.fouan.io;

import javax.inject.Named;
import java.util.InputMismatchException;
import java.util.Scanner;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

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
        Set<Integer> choices = IntStream.range(min, max + 1)
                .boxed()
                .collect(Collectors.toSet());
        return getChoice(choices);
    }

    @Override
    public int getChoice(Set<Integer> choices) {
        do {
            int choice = read();

            if (choices.contains(choice)) {
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
