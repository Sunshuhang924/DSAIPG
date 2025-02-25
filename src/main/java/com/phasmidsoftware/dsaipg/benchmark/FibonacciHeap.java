package com.phasmidsoftware.dsaipg.benchmark;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class FibonacciHeap {
    private Node minNode;
    private int size;

    private class Node {
        int value;
        Node prev;
        Node next;
        Node child;
        int degree;
        boolean mark;

        Node(int value) {
            this.value = value;
            this.prev = this;
            this.next = this;
            this.child = null;
            this.degree = 0;
            this.mark = false;
        }
    }

    public FibonacciHeap() {
        this.minNode = null;
        this.size = 0;
    }

    public void insert(int value) {
        Node newNode = new Node(value);
        if (minNode == null) {
            minNode = newNode;
        } else {
            // Insert new node into the root list
            minNode.prev.next = newNode;
            newNode.prev = minNode.prev;
            minNode.prev = newNode;
            newNode.next = minNode;
            if (value < minNode.value) {
                minNode = newNode;
            }
        }
        size++;
    }

    public int removeMin() {
        Node min = minNode;
        if (min != null) {
            if (min.child != null) {
                Node child = min.child;
                do {
                    child.prev.next = minNode;
                    child.prev = minNode.prev;
                    minNode.prev.next = child;
                    minNode.prev = child;
                    child = child.next;
                } while (child != min.child);
            }
            min.prev.next = min.next;
            min.next.prev = min.prev;
            if (min == min.next) {
                minNode = null;
            } else {
                minNode = min.next;
                consolidate();
            }
            size--;
            return min.value;
        }
        return Integer.MIN_VALUE; // Indicates the heap is empty
    }

    private void consolidate() {
        List<Node> trees = new ArrayList<>();
        Node current = minNode;
        do {
            Node x = current;
            int degree = x.degree;
            while (trees.size() <= degree) {
                trees.add(null);
            }

            while (trees.get(degree) != null) {
                Node y = trees.get(degree);
                if (x.value > y.value) {
                    Node temp = x;
                    x = y;
                    y = temp;
                }
                link(y, x);
                trees.set(degree, null);
                degree++;
            }
            trees.set(degree, x);
            current = current.next;
        } while (current != minNode);

        minNode = null;
        for (Node tree : trees) {
            if (tree != null) {
                if (minNode == null) {
                    minNode = tree;
                } else {
                    minNode.prev.next = tree;
                    tree.prev = minNode.prev;
                    minNode.prev = tree;
                    tree.next = minNode;
                    if (tree.value < minNode.value) {
                        minNode = tree;
                    }
                }
            }
        }
    }

    private void link(Node y, Node x) {
        y.prev.next = y.next;
        y.next.prev = y.prev;
        y.prev = x;
        y.next = x.child;
        if (x.child != null) {
            x.child.prev = y;
        }
        x.child = y;
        x.degree++;
        y.mark = false;
    }

    public boolean isEmpty() {
        return minNode == null;
    }

    public int size() {
        return size;
    }
}
