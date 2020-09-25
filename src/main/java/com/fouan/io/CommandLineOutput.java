package com.fouan.io;

import javax.inject.Named;

@Named
public class CommandLineOutput implements Output {

    @Override
    public void display(String value) {
        System.out.println(value);
    }
}
