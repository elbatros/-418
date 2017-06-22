package com.sortable.similarity.tree;

import com.sortable.similarity.api.MetricStringDistance;


/**
 * A <a href="http://en.wikipedia.org/wiki/BK-tree">BK-tree</a>.
 *
 * @param <E> type of elements in this tree
 */
public interface BkTree {

    /** Returns the metric for elements in this tree. */
	MetricStringDistance getMetric();

    /** Returns the root node of this tree. */
    Node<String> getRoot();

    /**
     * A node in a {@link BkTree}.
     *
     * @param <E> type of elements in the tree to which this node belongs
     */
    interface Node<E> {
        /** Returns the element in this node. */
        E getElement();

        /** Returns the child node at the given distance, if any. */
        Node<E> getChildNode(double distance);
    }
}
