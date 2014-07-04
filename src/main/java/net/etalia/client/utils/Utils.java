package net.etalia.client.utils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Collection;

public class Utils {

	public static void assertFalse(String string, boolean b) {
		if (b) throw new IllegalStateException(string);
	}

	public static void assertTrue(String string, boolean b) {
		if (!b) throw new IllegalStateException(string);
	}

	/**
	 * Joins the given path segments together, adding slashes where appropriate.
	 * @param segments A list of strings representing path segments.
	 * @return the segments joined with forward slash properly to form a valid path.
	 */
	public static String pathConcat(String... segments) {
		StringBuilder sb = new StringBuilder();
		boolean lastEndsWithSlash = false;
		boolean first = true;
		for(String s : segments) {
			if(first) {
				sb.append(s);
				first = false;
			} else {
				if((! lastEndsWithSlash) && (! s.startsWith("/"))) {
					sb.append("/");
					sb.append(s);
				} else if(lastEndsWithSlash && s.startsWith("/")) {
					sb.append(s.substring(1));
				} else {
					sb.append(s);
				}
			}
			if(s.endsWith("/")) {
				lastEndsWithSlash = true;
			}
		}
		return sb.toString();
	}
	
	/**
	 * @see #pathConcat(String...)
	 */
	public static String pathConcat(Collection<String> segments) {
		return pathConcat(segments.toArray(new String[segments.size()]));
	}

	public static void assertNull(String string, Object obj) {
		assertTrue(string, obj == null);
	}

	
	public static int copy(InputStream input, OutputStream output) throws IOException {
		byte[] buffer = new byte[4096];
        long count = 0;
        int n = 0;
        while (-1 != (n = input.read(buffer))) {
            output.write(buffer, 0, n);
            count += n;
        }
        if (count > Integer.MAX_VALUE) {
            return -1;
        }
        return (int) count;
	}
	
    public static byte[] toByteArray(InputStream input) throws IOException {
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        copy(input, output);
        return output.toByteArray();
    }
	
    public static String toString(InputStream input) throws IOException {
    	return new String(toByteArray(input));
    }
    
    public static void closeQuietly(InputStream c) {
    	try {
    		c.close();
    	} catch (Exception e) {}
    }
}
