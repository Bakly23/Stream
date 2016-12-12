package ru.sberbank.study.java.kolpakov.stream.function;

import java.util.Iterator;
import java.util.List;
import java.util.function.Function;
import java.util.function.Predicate;

/**
 * Created by Georgii Kolpakov on 12.12.16.
 */
public class FilterStreamFunction<B> implements Function<List<? extends B>, List<? extends B>> {
    private final Predicate<? super B> filter;

    public FilterStreamFunction(Predicate<? super B> filter) {
        this.filter = filter;
    }

    @Override
    public List<? extends B> apply(List<? extends B> list) {
        Iterator<? extends B> iterator = list.iterator();
        while (iterator.hasNext()) {
            B element = iterator.next();
            if (!filter.test(element)) {
                iterator.remove();
            }
        }
        return list;
    }
}
