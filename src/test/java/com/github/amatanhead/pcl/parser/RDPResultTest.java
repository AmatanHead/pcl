package com.github.amatanhead.pcl.parser;

import org.junit.Test;

import static org.junit.Assert.*;

public class RDPResultTest {
    @Test
    public void constructor() {
        RDPResult result;

        result = new RDPResult(true, "result");
        assertTrue(result.isSuccess());
        assertEquals("result", result.getResult());
        assertNull(result.getErrorMessage());

        result = new RDPResult("error");
        assertFalse(result.isSuccess());
        assertNull(result.getResult());
        assertEquals("error", result.getErrorMessage());
    }
}
