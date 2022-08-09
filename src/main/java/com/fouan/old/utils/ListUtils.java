package com.fouan.old.utils;

import java.util.Collection;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Stream;

public class ListUtils {

    public static <T, C extends Collection<T>> C concat(Collection<T> a, Collection<T> b, Supplier<C> collectionFactory) {
        return Stream.concat(a.stream(), b.stream())
                .collect(Collectors.toCollection(collectionFactory));
    }

    public static <T> List<T> keepMax(Collection<T> list, Comparator<T> comparator) {
        ArrayList<T> result = new ArrayList<>();

        for (T element : list) {
            int comparison;
            if (result.isEmpty() || (comparison = comparator.compare(result.get(0), element)) == 0) {
                result.add(element);
            } else if (comparison > 0) {
                result.clear();
                result.add(element);
            }
        }

        return result;
    }
}
