package ru.sberbank.study.java.kolpakov.stream;

import java.util.*;
import java.util.function.BiFunction;
import java.util.function.Function;

public class Streams<T> {
    private final List<? extends T> list;


    private Streams(List<? extends T> list) {
        //linked list is chosen because it helps to improve perfomance of filter operation: we are able to quickly remove
        //elements in the middle of the list.
        this.list = new LinkedList<>(list);
    }

    public static<T> Streams<T> of(List<? extends T> list) {
        return new Streams<>(list);
    }

    public Streams<T> filter(Function<? super T, Boolean> filter) {
        Iterator<? extends T> iterator = list.iterator();
        while(iterator.hasNext()) {
            T t = iterator.next();
            if(!filter.apply(t)) {
                iterator.remove();
            }
        }
        return this;
    }

    public<R> Streams<R> transform(Function<? super T, ? extends R> transformer) {
        List<R> newList = new LinkedList<>();
        for(T elem : list) {
            newList.add(transformer.apply(elem));
        }
        return Streams.of(newList);
    }

    public<K,V> Map<K, V> toMap(Function<? super T, ? extends K> keyTransformer,
                                Function<? super T, ? extends V> valueTransformer) {
        return toMap(keyTransformer, valueTransformer, (v, v2) -> v2);
    }

    public<K,V> Map<K, V> toMap(Function<? super T, ? extends K> keyTransformer,
                                Function<? super T, ? extends V> valueTransformer,
                                BiFunction<? super V, ? super V, ? extends V> merger) {
        Map<K, V> map = new HashMap<>(list.size());
        for(T t : list) {
            map.merge(keyTransformer.apply(t), valueTransformer.apply(t), merger);
        }
        return map;
    }
}