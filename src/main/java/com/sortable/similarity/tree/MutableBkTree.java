package com.sortable.similarity.tree;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import com.sortable.similarity.api.MetricStringDistance;

public final class MutableBkTree implements BkTree {

    private final MetricStringDistance metric;
    MutableNode<String> root;

    public MutableBkTree(MetricStringDistance metric) {
        if (metric == null) throw new NullPointerException();
        this.metric = metric;
    }

    /**
     * Adds the given element to this tree, if it's not already present.
     *
     * @param element element
     */
    public void add(String element) {
        if (element == null) throw new NullPointerException();

        if (root == null) {
            root = new MutableNode<>(element);
        } else {
            MutableNode<String> node = root;
            while (!node.getElement().equals(element)) {
                double distance = distance(node.getElement(), element);

                MutableNode<String> parent = node;
                node = parent.childrenByDistance.get(distance);
                if (node == null) {
                    node = new MutableNode<>(element);
                    parent.childrenByDistance.put(distance, node);
                    break;
                }
            }
        }
    }

    private int distance(String x, String y) {
        int distance = (int)metric.distance(x, y);
        if (distance < 0) {
            throw new IllegalMetricException(
                String.format("negative distance (%d) defined between elements `%s` and `%s`", distance, x, y));
        }
        return distance;
    }

    /**
     * Adds all of the given elements to this tree.
     *
     * @param elements elements
     */
    public void addAll(Iterable<String> elements) {
        if (elements == null) throw new NullPointerException();
        for (String element : elements) {
            add(element);
        }
    }

    /**
     * Adds all of the given elements to this tree.
     *
     * @param elements elements
     */
    @SafeVarargs
    public final void addAll(String... elements) {
        if (elements == null) throw new NullPointerException();
        addAll(Arrays.asList(elements));
    }

    @Override
    public MetricStringDistance getMetric() {
        return metric;
    }

    @Override
    public  Node<String> getRoot() {
        return root;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        MutableBkTree that = (MutableBkTree) o;

        if (!metric.equals(that.metric)) return false;
        if (root != null ? !root.equals(that.root) : that.root != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = metric.hashCode();
        result = 31 * result + (root != null ? root.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("MutableBkTree{");
        sb.append("metric=").append(metric);
        sb.append(", root=").append(root);
        sb.append('}');
        return sb.toString();
    }

    static final class MutableNode<E> implements Node<E> {
        final E element;
        final Map<Double, MutableNode<E>> childrenByDistance = new HashMap<>();

        MutableNode(E element) {
            if (element == null) throw new NullPointerException();
            this.element = element;
        }

        @Override
        public E getElement() {
            return element;
        }

        @Override
        public  Node<E> getChildNode(double distance) {
            return childrenByDistance.get(distance);
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            MutableNode that = (MutableNode) o;

            if (!childrenByDistance.equals(that.childrenByDistance)) return false;
            if (!element.equals(that.element)) return false;

            return true;
        }

        @Override
        public int hashCode() {
            int result = element.hashCode();
            result = 31 * result + childrenByDistance.hashCode();
            return result;
        }

        @Override
        public String toString() {
            StringBuilder sb = new StringBuilder("MutableNode{");
            sb.append("element=").append(element);
            sb.append(", childrenByDistance=").append(childrenByDistance);
            sb.append('}');
            return sb.toString();
        }
    }
}