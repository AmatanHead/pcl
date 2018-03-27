package com.github.amatanhead.pcl.utils;

import org.junit.Test;

import static org.junit.Assert.*;

public class MaybeTest {

    @Test
    public void getVal() {
        assertEquals(new Maybe<String>("s").getVal(), "s");
        assertEquals(new Maybe<String>().getVal(), null);
    }

    @Test
    public void hasVal() {
        assertTrue(new Maybe<String>("s").hasVal());
        assertFalse(new Maybe<String>().hasVal());
    }

    @Test
    public void map() {
        Maybe<Integer> m;

        m = (new Maybe<String>("string")).map(String::length);
        assertTrue(m.hasVal());
        assertEquals(m.getVal(), (Integer) 6);

        m = (new Maybe<String>()).map(String::length);
        assertFalse(m.hasVal());
        assertEquals(m.getVal(), null);
    }

    @Test
    public void equality() {
        assertEquals(new Maybe<String>("s"), new Maybe<String>("s"));
        assertEquals(new Maybe<String>(), new Maybe<String>());
        assertNotEquals(new Maybe<String>("s"), new Maybe<String>("x"));
        assertNotEquals(new Maybe<String>("s"), new Maybe<Integer>(10));
        assertNotEquals(new Maybe<String>("s"), new Maybe<String>());
        assertNotEquals(new Maybe<String>(), new Maybe<String>("x"));
        assertNotEquals(new Maybe<String>("s"), "s");
    }
}
