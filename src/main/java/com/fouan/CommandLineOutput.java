package com.fouan;

import javax.inject.Named;

@Named
public class CommandLineOutput implements Output {
    @Override
    public void print(String value) {
        System.out.print(value);
    }

    @Override
    public void println(String value) {
        System.out.println(value);
    }
}
