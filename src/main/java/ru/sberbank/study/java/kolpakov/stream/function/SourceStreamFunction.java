package ru.sberbank.study.java.kolpakov.stream.function;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.function.Function;

/**
 * Created by Georgii Kolpakov on 12.12.16.
 */
public class SourceStreamFunction<B> implements Function<List<? extends B>, List<? extends B>> {
    private final List<? extends B> list;

    public SourceStreamFunction(List<? extends B> list) {
        this.list = list;
    }

    @Override
    public List<? extends B> apply(List<? extends B> bs) {
        // 1. New list created because if we change source list then streams that were created before
        // would be changed, which is wrong behavior.
        // 2. Linked list is chosen because it helps to improve perfomance of filter operation:
        // we are able to quickly remove elements in the middle of the list.
        return new LinkedList<B>(list);
    }
}
