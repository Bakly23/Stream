package ru.sberbank.study.java.kolpakov.stream;

import org.junit.Test;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;

public class StreamsTest {
    @Test
    public void testStreams() {
        List<String> testArray = Arrays.asList("abcde", "abc", "gosha", "kolpakov", "python", "not", "java");
        Map<String, Integer> expectedResultMap = new HashMap<String, Integer>() {{
            put("ABCDE", 5);
            put("GOSHA", 5);
            put("KOLPAKOV", 8);
            put("PYTHON", 6);
        }};
        Map<CharSequence, Number> resultMap = Streams.of(testArray)
                .filter(str -> str.length() > 4)
                .transform(String::toUpperCase)
                .toMap(str -> str, String::length);
        assertEquals(expectedResultMap, resultMap);
        assertEquals(Arrays.asList("abcde", "abc", "gosha", "kolpakov", "python", "not", "java"), testArray);
    }

    @Test
    public void testStreamsWithMerge() {
        List<String> testArray = Arrays.asList("OOOOO", "MY", "DEFENCE", "SUNNY", "RAINBOW", "OF",
                "GLASS", "WORLD");
        Map<Number, Double> expectedResultMap = new HashMap<Number, Double>() {{
            put(5.0, 3.0);
            put(7.0, 2.0);
        }};
        Map<Number, Number> result = Streams.of(testArray)
                .filter(str -> !str.contains("LD"))
                .transform(String::length)
                .filter(length -> length > 4)
                .transform(Integer::doubleValue)
                .toMap(length -> length, length -> 1, (l, l1) -> l.doubleValue() + l1.doubleValue());
        assertEquals(expectedResultMap, result);
        assertEquals(Arrays.asList("OOOOO", "MY", "DEFENCE", "SUNNY", "RAINBOW", "OF",
                "GLASS", "WORLD"), testArray);
    }
}
