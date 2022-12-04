package com.fouan.old.io;

import java.util.Set;

public interface ChoiceMaker {

    Set<Integer> getChoices(int min, int max, int nb);

    int getChoice(int min, int max);

    int getChoice(Set<Integer> choices);
}
