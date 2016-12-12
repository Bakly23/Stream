package ru.sberbank.study.java.kolpakov.stream.function;

import java.util.List;
import java.util.ListIterator;
import java.util.function.Function;

public class TransformStreamFunction<B,A> implements Function<List<? super B>, List<? extends A>> {
    private final Function<? super B, ? extends A> transformer;

    public TransformStreamFunction(Function<? super B, ? extends A> transformer) {
        this.transformer = transformer;
    }

    @Override
    public List<? extends A> apply(List<? super B> list) {
        ListIterator listIterator = list.listIterator();
        while (listIterator.hasNext()) {
            Object element = listIterator.next();
            listIterator.set(transformer.apply((B) element));
        }
        return (List<? extends A>) list;
    }
}
