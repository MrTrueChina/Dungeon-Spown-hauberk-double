package mytest;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.ArrayList;

import vector.Vector;

public class Assertions {
    public static <T> void assertArrayListEquals(ArrayList<T> a, ArrayList<T> b) {
        for (T element : a)
            assertEquals(true, b.contains(element));
        for (T element : b)
            assertEquals(true, a.contains(element));
    }
}
