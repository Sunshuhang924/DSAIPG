package com.phasmidsoftware.dsaipg.benchmark;

import java.util.Arrays;
import java.util.NoSuchElementException;

/**
 * 4-ary Heap implementation with optional Floyd's Trick optimization.
 */
public class FourAry {
    private static final int DEFAULT_CAPACITY = 4096;
    private final boolean useFloydsTrick;
    private int[] heap;
    private int size;

    public FourAry(boolean useFloydsTrick) {
        this.heap = new int[DEFAULT_CAPACITY];
        this.size = 0;
        this.useFloydsTrick = useFloydsTrick;
    }

    public void offer(int value) {
        if (size >= heap.length) {
            grow();
        }
        heap[size] = value;
        if (useFloydsTrick) {
            heapifyUpFloyd(size);
        } else {
            heapifyUp(size);
        }
        size++;
    }

    public int poll() {
        if (size == 0) throw new NoSuchElementException();
        int min = heap[0];
        heap[0] = heap[--size];
        heapifyDown(0);
        return min;
    }

    public int peek() {
        if (size == 0) throw new NoSuchElementException();
        return heap[0];
    }

    public int size() {
        return size;
    }

    public void clear() {
        size = 0;
    }

    private void grow() {
        heap = Arrays.copyOf(heap, heap.length * 2);
    }

    private void heapifyUp(int index) {
        int parent = (index - 1) / 4;
        while (index > 0 && heap[index] < heap[parent]) {
            swap(index, parent);
            index = parent;
            parent = (index - 1) / 4;
        }
    }

    private void heapifyUpFloyd(int index) {
        int value = heap[index];
        while (index > 0) {
            int parent = (index - 1) / 4;
            if (heap[parent] <= value) break;
            heap[index] = heap[parent];
            index = parent;
        }
        heap[index] = value;
    }

    private void heapifyDown(int index) {
        while (true) {
            int minIndex = index;
            for (int i = 1; i <= 4; i++) {
                int child = 4 * index + i;
                if (child < size && heap[child] < heap[minIndex]) {
                    minIndex = child;
                }
            }
            if (minIndex == index) break;
            swap(index, minIndex);
            index = minIndex;
        }
    }

    private void swap(int i, int j) {
        int temp = heap[i];
        heap[i] = heap[j];
        heap[j] = temp;
    }
}
