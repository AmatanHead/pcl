package com.github.amatanhead.pcl.utils;

import java.io.IOException;
import java.io.InputStreamReader;

/**
 * A collection of commonly used function.
 */
public final class Utils {
    /**
     * Protect constructor since this is a static-only class.
     */
    protected Utils() {
    }

    /**
     * Read all contents from the given {@link InputStreamReader} and return them as a single string.
     *
     * @param inputStreamReader stream reader that should be converted to string. After calling this function,
     *                          the stream will be exhausted and closed.
     * @return full contents of the given stream.
     * @throws IOException if an I/O error occurs.
     */
    static public String streamreaderToString(InputStreamReader inputStreamReader) throws IOException {
        final char[] buffer = new char[1024];
        final StringBuilder out = new StringBuilder();

        while (true) {
            int resultSize = inputStreamReader.read(buffer, 0, buffer.length);
            if (resultSize < 0)
                break;
            out.append(buffer, 0, resultSize);
        }

        inputStreamReader.close();

        return out.toString();
    }
}
