package com.sortable.similarity.api;

import java.io.Serializable;
import java.util.Map;

public interface StringDistance extends Serializable {

    /**
     * Compute and return a measure of distance.
     * Must be >= 0.
     * @param s1
     * @param s2
     * @return
     */
    double distance(String s1, String s2);
    double distance(
            final Map<String, Integer> profile1,
            final Map<String, Integer> profile2);
}