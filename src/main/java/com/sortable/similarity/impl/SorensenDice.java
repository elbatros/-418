package com.sortable.similarity.impl;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import com.sortable.similarity.api.NormalizedStringDistance;
import com.sortable.similarity.api.NormalizedStringSimilarity;

/**
 * Similar to Jaccard index, but this time the similarity is computed as 2 * |V1
 * inter V2| / (|V1| + |V2|). Distance is computed as 1 - cosine similarity.
 *
 * 
 */
public class SorensenDice extends BaseShingle implements
NormalizedStringDistance, NormalizedStringSimilarity {

/**
* Sorensen-Dice coefficient, aka Sørensen index, Dice's coefficient or
* Czekanowski's binary (non-quantitative) index.
*
* The strings are first converted to boolean sets of k-shingles (sequences
* of k characters), then the similarity is computed as 2 * |A inter B| /
* (|A| + |B|). Attention: Sorensen-Dice distance (and similarity) does not
* satisfy triangle inequality.
*
* @param k
*/
public SorensenDice(final int k) {
super(k);
}

/**
* Sorensen-Dice coefficient, aka Sørensen index, Dice's coefficient or
* Czekanowski's binary (non-quantitative) index.
*
* The strings are first converted to boolean sets of k-shingles (sequences
* of k characters), then the similarity is computed as 2 * |A inter B| /
* (|A| + |B|). Attention: Sorensen-Dice distance (and similarity) does not
* satisfy triangle inequality. Default k is 3.
*/
public SorensenDice() {
super();
}

/**
* Similarity is computed as 2 * |A inter B| / (|A| + |B|).
*
* @param s1 The first string to compare.
* @param s2 The second string to compare.
* @return The computed Sorensen-Dice similarity.
* @throws NullPointerException if s1 or s2 is null.
*/
public final double similarity(final String s1, final String s2) {
if (s1 == null) {
    throw new NullPointerException("s1 must not be null");
}

if (s2 == null) {
    throw new NullPointerException("s2 must not be null");
}

if (s1.equals(s2)) {
    return 1;
}

Map<String, Integer> profile1 = getProfile(s1);
Map<String, Integer> profile2 = getProfile(s2);

Set<String> union = new HashSet<String>();
union.addAll(profile1.keySet());
union.addAll(profile2.keySet());

int inter = 0;

for (String key : union) {
    if (profile1.containsKey(key) && profile2.containsKey(key)) {
        inter++;
    }
}

return 2.0 * inter / (profile1.size() + profile2.size());
}

/**
* Returns 1 - similarity.
*
* @param s1 The first string to compare.
* @param s2 The second string to compare.
* @return 1.0 - the computed similarity
* @throws NullPointerException if s1 or s2 is null.
*/
public final double distance(final String s1, final String s2) {
return 1 - similarity(s1, s2);
}

@Override
public double similarity(Map<String, Integer> profile1, Map<String, Integer> profile2) {
	// TODO Auto-generated method stub
	return 0;
}


}
