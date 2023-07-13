package com.fouan.display;

import javax.inject.Named;

@Named
public final class CommandLineOutput implements Output {

    @Override
    public void display(String value) {
        System.out.println(value);
    }
}
