package com.github.amatanhead.pcl.utils;

import org.junit.Test;

import static org.junit.Assert.*;

public class MaybeTest {

    @Test
    public void getVal() {
        assertEquals(new Maybe<>("s").getVal(), "s");
        assertNull(new Maybe<String>().getVal());
    }

    @Test
    public void hasVal() {
        assertTrue(new Maybe<>("s").hasVal());
        assertFalse(new Maybe<String>().hasVal());
    }

    @Test
    public void map() {
        Maybe<Integer> m;

        m = (new Maybe<>("string")).map(String::length);
        assertTrue(m.hasVal());
        assertEquals(m.getVal(), (Integer) 6);

        m = (new Maybe<String>()).map(String::length);
        assertFalse(m.hasVal());
        assertEquals(m.getVal(), null);
    }

    @Test
    public void equality() {
        assertEquals(new Maybe<>("s"), new Maybe<>("s"));
        assertEquals(new Maybe<String>(), new Maybe<String>());
        assertNotEquals(new Maybe<>("s"), new Maybe<>("x"));
        assertNotEquals(new Maybe<>("s"), new Maybe<>(10));
        assertNotEquals(new Maybe<>("s"), new Maybe<String>());
        assertNotEquals(new Maybe<String>(), new Maybe<>("x"));
        assertNotEquals(new Maybe<>("s"), "s");
    }
}
