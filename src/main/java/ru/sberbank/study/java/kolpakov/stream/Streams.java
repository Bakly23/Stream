package ru.sberbank.study.java.kolpakov.stream;

import java.util.*;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Predicate;

// B is type of source collection that was before action of stream was applied
// A is type of collection after applying action(if action is filtering then type would not be changed and B is equal to A,
// if action is transformer then type would change)
public class Streams<B, A> {
    private final List<Streams> streamsList;
    private List<? extends B> list;
    private Predicate<? super B> filter;
    private Function<? super B, ? extends A> transformer;


    private Streams(List<? extends B> list) {
        this.list = new ArrayList<>(list);
        streamsList = new ArrayList<>();
    }

    private <P> Streams(Streams<P, B> streams, Function<? super B, ? extends A> transformer) {
        streamsList = new ArrayList<>(streams.streamsList);
        streamsList.add(streams);
        this.transformer = transformer;
    }

    private <P> Streams(Streams<P, B> streams, Predicate<? super B> filter) {
        streamsList = new ArrayList<>(streams.streamsList);
        streamsList.add(streams);
        this.filter = filter;
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
        streamsList.add(this);
        // 1. New list created because if we change source list then streams that were created before
        // would be changed, which is wrong behavior.
        // 2. Linked list is chosen because it helps to improve perfomance of filter operation:
        // we are able to quickly remove elements in the middle of the list.
        List sourceListCopy = new LinkedList(streamsList.get(0).list);
        for (Streams streams : streamsList) {
            if (streams.filter != null) {
                sourceListCopy = filter(sourceListCopy, streams.filter);
            } else if (streams.transformer != null) {
                sourceListCopy = transform(sourceListCopy, streams.transformer);
            }
        }
        return sourceListCopy;
    }

    private List transform(List sourceListCopy, Function transformer) {
        ListIterator listIterator = sourceListCopy.listIterator();
        while (listIterator.hasNext()) {
            Object element = listIterator.next();
            listIterator.set(transformer.apply(element));
        }
        return sourceListCopy;
    }

    private List filter(List sourceListCopy, Predicate filter) {
        Iterator iterator = sourceListCopy.iterator();
        while (iterator.hasNext()) {
            Object element = iterator.next();
            if (!filter.test(element)) {
                iterator.remove();
            }
        }
        return sourceListCopy;
    }
}