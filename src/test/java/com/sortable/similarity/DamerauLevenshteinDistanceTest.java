package com.sortable.similarity;

import org.junit.Test;
import static org.junit.Assert.*;

import com.sortable.similarity.impl.Damerau;

public class DamerauLevenshteinDistanceTest {
	
	@Test
    public void testDistance() throws Exception {
        Damerau metric = new Damerau();

        assertEquals(metric.distance("", ""), 0,0.0);

        assertEquals(metric.distance("a", ""), 1.0,0.0);
        assertEquals(metric.distance("", "a"), 1.0,0.0);

        assertEquals(metric.distance("a", "c"), 1.0,0.0);
        assertEquals(metric.distance("c", "a"), 1.0,0.0);

        assertEquals(metric.distance("abc", "bac"), 1.0,0.0);
        assertEquals(metric.distance("bac", "abc"), 1.0,0.0);

        assertEquals(metric.distance("ca", "abc"), 2.0,0.0);
        assertEquals(metric.distance("abc", "ca"), 2.0,0.0);

        assertEquals(metric.distance("abcdef", "hijklm"), 6.0,0.0);
        assertEquals(metric.distance("hijklm", "abcdef"), 6.0,0.0);

        assertEquals(metric.distance("book", ""), 4.0,0.0);

    }

}
