package com.sortable.similarity.api;

import java.io.Serializable;
import java.util.Map;

public interface StringSimilarity extends Serializable {
    /**
     * Compute and return a measure of similarity between 2 strings.
     * @param s1
     * @param s2
     * @return similarity (0 means both strings are completely different)
     */
    double similarity(String s1, String s2);
    
    double similarity( Map<String, Integer> profile1, Map<String, Integer> profile2);
}