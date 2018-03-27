package com.github.amatanhead.pcl.utils;

import com.github.amatanhead.pcl.utils.GenericTuple.*;
import org.junit.Test;

import static org.junit.Assert.*;

public class GenericTupleTest {
    @Test
    public void constructors() {
        Tuple1<String> t1 = new Tuple1<>("1");
        assertEquals(t1.t1, "1");

        Tuple2<String, String> t2 = new Tuple2<>("1", "2");
        assertEquals(t2.t1, "1");
        assertEquals(t2.t2, "2");

        Tuple3<String, String, String> t3 = new Tuple3<>("1", "2", "3");
        assertEquals(t3.t1, "1");
        assertEquals(t3.t2, "2");
        assertEquals(t3.t3, "3");

        Tuple4<String, String, String, String> t4 = new Tuple4<>("1", "2", "3", "4");
        assertEquals(t4.t1, "1");
        assertEquals(t4.t2, "2");
        assertEquals(t4.t3, "3");
        assertEquals(t4.t4, "4");

        Tuple5<String, String, String, String, String> t5 = new Tuple5<>("1", "2", "3", "4", "5");
        assertEquals(t5.t1, "1");
        assertEquals(t5.t2, "2");
        assertEquals(t5.t3, "3");
        assertEquals(t5.t4, "4");
        assertEquals(t5.t5, "5");
    }

    @Test
    public void equality() {
        assertEquals(new Tuple1<>("1"), new Tuple1<>("1"));
        assertNotEquals(new Tuple1<>("1"), new Tuple1<>("2"));
        assertNotEquals(new Tuple1<>("1"), new Tuple1<>(1));
        assertNotEquals(new Tuple1<>("1"), new Tuple2<>("1", "2"));

        assertEquals(new Tuple2<>("1", "2"), new Tuple2<>("1", "2"));
        assertNotEquals(new Tuple2<>("1", "2"), new Tuple2<>("1", "1"));
        assertNotEquals(new Tuple2<>("1", "2"), new Tuple2<>("2", "2"));
        assertNotEquals(new Tuple2<>("1", "2"), new Tuple2<>(1, "2"));
        assertNotEquals(new Tuple2<>("1", "2"), new Tuple2<>("1", 2));
        assertNotEquals(new Tuple2<>("1", "2"), new Tuple3<>("1", "2", "3"));

        assertEquals(new Tuple3<>("1", "2", "3"), new Tuple3<>("1", "2", "3"));
        assertNotEquals(new Tuple3<>("1", "2", "3"), new Tuple3<>("2", "2", "3"));
        assertNotEquals(new Tuple3<>("1", "2", "3"), new Tuple3<>("1", "3", "3"));
        assertNotEquals(new Tuple3<>("1", "2", "3"), new Tuple3<>("1", "2", "1"));
        assertNotEquals(new Tuple3<>("1", "2", "3"), new Tuple3<>(1, "2", "3"));
        assertNotEquals(new Tuple3<>("1", "2", "3"), new Tuple3<>("1", 2, "3"));
        assertNotEquals(new Tuple3<>("1", "2", "3"), new Tuple3<>("1", "2", 3));
        assertNotEquals(new Tuple3<>("1", "2", "3"), new Tuple4<>("1", "2", "3", "4"));

        assertEquals(new Tuple4<>("1", "2", "3", "4"), new Tuple4<>("1", "2", "3", "4"));
        assertNotEquals(new Tuple4<>("1", "2", "3", "4"), new Tuple4<>("2", "2", "3", "4"));
        assertNotEquals(new Tuple4<>("1", "2", "3", "4"), new Tuple4<>("1", "3", "3", "4"));
        assertNotEquals(new Tuple4<>("1", "2", "3", "4"), new Tuple4<>("1", "2", "4", "4"));
        assertNotEquals(new Tuple4<>("1", "2", "3", "4"), new Tuple4<>("1", "2", "3", "1"));
        assertNotEquals(new Tuple4<>("1", "2", "3", "4"), new Tuple4<>(1, "2", "3", "4"));
        assertNotEquals(new Tuple4<>("1", "2", "3", "4"), new Tuple4<>("1", 2, "3", "4"));
        assertNotEquals(new Tuple4<>("1", "2", "3", "4"), new Tuple4<>("1", "2", 3, "4"));
        assertNotEquals(new Tuple4<>("1", "2", "3", "4"), new Tuple4<>("1", "2", "3", 4));
        assertNotEquals(new Tuple4<>("1", "2", "3", "4"), new Tuple5<>("1", "2", "3", "4", "5"));

        assertEquals(new Tuple5<>("1", "2", "3", "4", "5"), new Tuple5<>("1", "2", "3", "4", "5"));
        assertNotEquals(new Tuple5<>("1", "2", "3", "4", "5"), new Tuple5<>("2", "2", "3", "4", "5"));
        assertNotEquals(new Tuple5<>("1", "2", "3", "4", "5"), new Tuple5<>("1", "4", "3", "4", "5"));
        assertNotEquals(new Tuple5<>("1", "2", "3", "4", "5"), new Tuple5<>("1", "2", "4", "4", "5"));
        assertNotEquals(new Tuple5<>("1", "2", "3", "4", "5"), new Tuple5<>("1", "2", "3", "5", "5"));
        assertNotEquals(new Tuple5<>("1", "2", "3", "4", "5"), new Tuple5<>("1", "2", "3", "4", "1"));
        assertNotEquals(new Tuple5<>("1", "2", "3", "4", "5"), new Tuple5<>(1, "2", "3", "4", "5"));
        assertNotEquals(new Tuple5<>("1", "2", "3", "4", "5"), new Tuple5<>("1", 2, "3", "4", "5"));
        assertNotEquals(new Tuple5<>("1", "2", "3", "4", "5"), new Tuple5<>("1", "2", 3, "4", "5"));
        assertNotEquals(new Tuple5<>("1", "2", "3", "4", "5"), new Tuple5<>("1", "2", "3", 4, "5"));
        assertNotEquals(new Tuple5<>("1", "2", "3", "4", "5"), new Tuple5<>("1", "2", "3", "4", 5));
        assertNotEquals(new Tuple5<>("1", "2", "3", "4", "5"), new Tuple4<>("1", "2", "3", "4"));
    }
}
