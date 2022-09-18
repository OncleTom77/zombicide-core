package com.fouan.old.io;

import java.util.Set;

public interface ChoiceMaker {

    int getChoice(int min, int max);

    int getChoice(Set<Integer> choices);
}
