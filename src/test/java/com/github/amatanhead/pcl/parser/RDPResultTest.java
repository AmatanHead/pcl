package com.github.amatanhead.pcl.parser;

import org.junit.Test;

import static org.junit.Assert.*;

public class RDPResultTest {
    @Test
    public void constructor() {
        RDPResult result;

        result = new RDPResult(true, "result");
        assertEquals(result.isSuccess(), true);
        assertEquals(result.getResult(), "result");
        assertEquals(result.getErrorMessage(), null);

        result = new RDPResult("error");
        assertEquals(result.isSuccess(), false);
        assertEquals(result.getResult(), null);
        assertEquals(result.getErrorMessage(), "error");
    }
}
