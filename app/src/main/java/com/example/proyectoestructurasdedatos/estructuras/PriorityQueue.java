package com.example.proyectoestructurasdedatos.estructuras;

public class PriorityQueue {
    int[] heap;
    int size = 0, maxSize;

    public PriorityQueue(int maxSize) {
        heap = new int[maxSize];
    }

    public boolean isEmpty() {
        return size == 0;
    }

    public int size() {
        return size;
    }

    public void insert(int value) throws Exception {
        if (size == maxSize) {
            throw new Exception("La cola está llena.");
        }

        heap[size] = value;
        sinkUp(size);
        size++;
    }

    public int extractMax() {
        int result = heap[0];
        heap[0] = heap[size - 1];
        sinkDown(0);
        size--;
        return result;
    }

    public void remove(int position) {
        heap[position] = Integer.MAX_VALUE;
        sinkUp(position);
        extractMax();
    }

    public void changePriority(int position, int newPriority) {
        int oldPriority = heap[position];
        heap[position] = newPriority;

        if (newPriority > oldPriority) {
            sinkUp(position);
        } else {
            sinkDown(position);
        }
    }

    private void sinkUp(int position) {
        int parent = (position - 1) / 2;
        int current = position;

        while (current > 0 && heap[parent] < heap[current]) {
            swap(current, parent);
            current = parent;
            parent = (parent - 1) / 2;
        }
    }

    private void sinkDown(int position) {
        int maxIndex = position;
        int leftChild = 2 * position + 1;
        int rightChild = 2 * position + 2;

        if (leftChild <= size && heap[leftChild] > heap[maxIndex]) {
            maxIndex = leftChild;
        } else if (rightChild <= size && heap[rightChild] > heap[maxIndex]) {
            maxIndex = rightChild;
        }

        if (position != maxIndex) {
            swap(position, maxIndex);
            sinkDown(maxIndex);
        }
    }

    private void swap(int a, int b) {
        int temp = heap[a];
        heap[a] = heap[b];
        heap[b] = temp;
    }
}