package ru.sberbank.study.java.kolpakov.stream;

import ru.sberbank.study.java.kolpakov.stream.function.FilterStreamFunction;
import ru.sberbank.study.java.kolpakov.stream.function.SourceStreamFunction;
import ru.sberbank.study.java.kolpakov.stream.function.TransformStreamFunction;

import java.util.*;
import java.util.function.*;

// B is type of source collection that was before action of stream was applied
// A is type of collection after applying action(if action is filtering then type would not be changed and B is equal to A,
// if action is transformer then type would change)
public class Streams<B, A> {
    private final List<Streams> streamsList;
    private final Function<List<? extends B>, List<? extends A>> action;


    private Streams(List<? extends B> list) {
        this.streamsList = new ArrayList<>();
        this.streamsList.add(this);
        this.action = (Function<List<? extends B>, List<? extends A>>) new SourceStreamFunction<B>(list);
    }

    private <P> Streams(Streams<P, B> streams, Function<? super B, ? extends A> transformer) {
        this.streamsList = new ArrayList<>(streams.streamsList);
        this.streamsList.add(this);
        this.action = (Function<List<? extends B>, List<? extends A>>) new TransformStreamFunction<B,A>(transformer);
    }

    private <P> Streams(Streams<P, B> streams, Predicate<? super B> filter) {
        this.streamsList = new ArrayList<>(streams.streamsList);
        this.streamsList.add(this);
        this.action = (Function<List<? extends B>, List<? extends A>>) new FilterStreamFunction<B>(filter);
    }

    public static <T> Streams<T, T> of(List<? extends T> list) {
        return new Streams<>(list);
    }

    public Streams<A, A> filter(Predicate<? super A> filter) {
        return new Streams<>(this, filter);
    }

    public <P> Streams<A, P> transform(Function<? super A, ? extends P> transformer) {
        return new Streams<>(this, transformer);
    }

    public <K, V> Map<K, V> toMap(Function<? super A, ? extends K> keyTransformer,
                                  Function<? super A, ? extends V> valueTransformer) {
        return toMap(keyTransformer, valueTransformer, (v, v2) -> v2);
    }

    public <K, V> Map<K, V> toMap(Function<? super A, ? extends K> keyTransformer,
                                  Function<? super A, ? extends V> valueTransformer,
                                  BiFunction<? super V, ? super V, ? extends V> merger) {
        List<A> result = applyAllAndGetResultingList();
        Map<K, V> map = new HashMap<>(result.size());
        for (A e : result) {
            map.merge(keyTransformer.apply(e), valueTransformer.apply(e), merger);
        }
        return map;
    }

    private List<A> applyAllAndGetResultingList() {
        List sourceListCopy = null;
        for (Streams streams : streamsList) {
            sourceListCopy = (List) streams.action.apply(sourceListCopy);
        }
        return sourceListCopy;
    }
}