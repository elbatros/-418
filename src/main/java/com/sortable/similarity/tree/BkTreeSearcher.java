package com.sortable.similarity.tree;

import java.util.ArrayDeque;
import java.util.HashSet;
import java.util.Queue;
import java.util.Set;

import com.sortable.similarity.api.MetricStringDistance;
import com.sortable.similarity.tree.BkTree.Node;

public class BkTreeSearcher {
	 private final BkTree tree;

	    /**
	     * Constructs a searcher that orders matches in increasing order of
	     * distance from the query.
	     *
	     * @param tree tree to search
	     */
	    public BkTreeSearcher(BkTree tree) {
	        if (tree == null) throw new NullPointerException();
	        this.tree = tree;
	    }

	    /**
	     * Searches the tree for elements whose distance from the given query
	     * is less than or equal to the given maximum distance.
	     *
	     * @param query query against which to match tree elements
	     * @param maxDistance non-negative maximum distance of matching elements from query
	     * @return matching elements in no particular order
	     */
	    public Set<Match<String>> search(String query, int maxDistance) {
	        if (query == null) throw new NullPointerException();
	        if (maxDistance < 0) throw new IllegalArgumentException("maxDistance must be non-negative");

	      MetricStringDistance metric = tree.getMetric();

	        Set<Match<String>> matches = new HashSet<>();

	        Queue<Node<String>> queue = new ArrayDeque<>();
	        queue.add(tree.getRoot());

	        while (!queue.isEmpty()) {
	            Node<String> node = queue.remove();
	            String element = node.getElement();

	            int distance = (int)metric.distance(element, query);
	            if (distance < 0) {
	                throw new IllegalMetricException(
	                    String.format("negative distance (%d) defined between element `%s` and query `%s`",
	                        distance, element, query));
	            }

	            if (distance <= maxDistance) {
	                matches.add(new Match<String>(element, distance));
	            }

	            int minSearchDistance = java.lang.Math.max(distance - maxDistance, 0);
	           
	            int maxSearchDistance = distance + maxDistance;

	            for (int searchDistance = minSearchDistance; searchDistance <= maxSearchDistance; searchDistance+=1) {
	                Node<String> childNode = node.getChildNode(searchDistance);
	                if (childNode != null) {
	                    queue.add(childNode);
	                }
	            }
	        }

	        return matches;
	    }

	    /** Returns the tree searched by this searcher. */
	    public BkTree getTree() {
	        return tree;
	    }

	    /**
	     * An element matching a query.
	     *
	     * @param <E> type of matching element
	     */
	    public static final class Match<E> {

	        private final E match;
	        private final int distance;

	        /**
	         * @param match matching element
	         * @param distance distance of the matching element from the search query
	         */
	        public Match(E match, int distance) {
	            if (match == null) throw new NullPointerException();
	            if (distance < 0) throw new IllegalArgumentException("distance must be non-negative");

	            this.match = match;
	            this.distance = distance;
	        }

	        /** Returns the matching element. */
	        public E getMatch() {
	            return match;
	        }

	        /** Returns the matching element's distance from the search query. */
	        public int getDistance() {
	            return distance;
	        }

	        @Override
	        public boolean equals(Object o) {
	            if (this == o) return true;
	            if (o == null || getClass() != o.getClass()) return false;

	            Match that = (Match) o;

	            if (distance != that.distance) return false;
	            if (!match.equals(that.match)) return false;

	            return true;
	        }

	        @Override
	        public int hashCode() {
	            int result = match.hashCode();
	            result = (int) (31 * result + distance*100);
	            return result;
	        }

	        @Override
	        public String toString() {
	            final StringBuilder sb = new StringBuilder("Match{");
	            sb.append("match=").append(match);
	            sb.append(", distance=").append(distance);
	            sb.append('}');
	            return sb.toString();
	        }
	    }
}
