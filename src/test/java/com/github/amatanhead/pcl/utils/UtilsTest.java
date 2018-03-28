package com.github.amatanhead.pcl.utils;

import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

import static org.junit.Assert.*;

public class UtilsTest {
    @Test
    public void streamReaderToString() throws IOException {
        String s1 = "data 1";
        ByteArrayInputStream is1 = new ByteArrayInputStream(s1.getBytes(StandardCharsets.UTF_8));
        InputStreamReader isr1 = new InputStreamReader(is1, StandardCharsets.UTF_8);

        assertEquals(s1, Utils.streamreaderToString(isr1));

        String s2 = new String(new char[1000]).replace("\0", "data");
        ByteArrayInputStream is2 = new ByteArrayInputStream(s2.getBytes(StandardCharsets.UTF_8));
        InputStreamReader isr2 = new InputStreamReader(is2, StandardCharsets.UTF_8);

        assertEquals(s2, Utils.streamreaderToString(isr2));

        String s3 = "";
        ByteArrayInputStream is3 = new ByteArrayInputStream(s3.getBytes(StandardCharsets.UTF_8));
        InputStreamReader isr3 = new InputStreamReader(is3, StandardCharsets.UTF_8);

        assertEquals(s3, Utils.streamreaderToString(isr3));
    }
}
