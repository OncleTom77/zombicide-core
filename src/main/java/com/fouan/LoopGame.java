package com.fouan;

import javax.inject.Inject;
import javax.inject.Named;

@Named
public class LoopGame {

    @Inject
    public LoopGame() {
    }

    public void run() {
        while (true) {
            System.out.println("new turn");
            break;

            // survivors' phase
            // check potential survivors victory

            // zombies' phase
            // check potential survivors defeat

            // zombies invasion
            // check potential survivors defeat
        }
    }
}
