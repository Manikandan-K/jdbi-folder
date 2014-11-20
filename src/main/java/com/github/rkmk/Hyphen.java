package com.github.rkmk;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toSet;

public class Hyphen {

    static <I,O, C extends Collection<I>, R extends Collection<O>> R collect(Stream<O> stream, C collection) {
        return (R) (collection instanceof List ? stream.collect(toList()) : stream.collect(toSet()));
    }

    public static <O, T extends Collection<O>> T where(T collection, Map<String, Object> whereClause) {
        return collect(collection.stream().filter(new CustomPredicate<O>(whereClause)), collection);
    }
}