package com.example.proyectoestructurasdedatos.estructuras;

import android.os.Build;

import androidx.annotation.RequiresApi;

import java.time.LocalTime;

public class ServicesPriorityQueue {
    User[] heap;
    int size = 0, maxSize;

    public ServicesPriorityQueue(int maxSize, LocalTime initialHour) {
        this.heap = new User[maxSize];
        heap[0] = new User(initialHour, "0", 0.0);
    }

    public boolean isEmpty() {
        return size == 0;
    }

    public int size() {
        return size;
    }

    public void insert(User user) throws Exception {
        if (size == maxSize) {
            throw new Exception("La cola está llena.");
        }

        heap[size] = user;
        sinkUp(size);
        size++;
    }

    public User extractMax() {
        User result = heap[0];
        heap[0] = heap[size - 1];
        sinkDown(0);
        size--;
        return result;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void remove(String uid) {
        int position = find(uid);

        heap[position].setAttentionHour(LocalTime.MIN);
        sinkUp(position);
        extractMax();
    }

    public void changeHour(LocalTime newHour, String uid) throws Exception {
        int position = find(uid);

        if (position < 0) {
            throw new Exception("El usuario no se encuentra en la cola.");
        }

        LocalTime oldHour = heap[position].getAttentionHour();
        heap[position].setAttentionHour(newHour);

        if (newHour.compareTo(oldHour) < 0) {
            sinkUp(position);
        } else {
            sinkDown(position);
        }
    }

    private int find(String uid) {
        for (int i = 0; i < size; i++) {
            if (heap[i].getUid().equals(uid)) {
                return i;
            }
        }

        return -1;
    }

    private void sinkUp(int position) {
        int parent = (position - 1) / 2;
        int current = position;

        LocalTime parentAttentionHour = heap[parent].getAttentionHour();
        LocalTime currentAttentionHour = heap[current].getAttentionHour();

        while (current > 0 && parentAttentionHour.compareTo(currentAttentionHour) > 0) {
            swap(current, parent);
            current = parent;
            currentAttentionHour = heap[current].getAttentionHour();

            parent = (parent - 1) / 2;
            parentAttentionHour = heap[parent].getAttentionHour();
        }
    }

    private void sinkDown(int position) {
        int maxIndex = position;
        int leftChild = 2 * position + 1;
        int rightChild = 2 * position + 2;

        LocalTime leftChildAttentionHour = heap[leftChild].getAttentionHour();
        LocalTime rightChildAttentionHour = heap[leftChild].getAttentionHour();
        LocalTime maxIndexAttentionHour = heap[maxIndex].getAttentionHour();

        if (leftChild <= size && leftChildAttentionHour.compareTo(maxIndexAttentionHour) < 0) {
            maxIndex = leftChild;
        } else if (rightChild <= size && rightChildAttentionHour.compareTo(maxIndexAttentionHour) < 0) {
            maxIndex = rightChild;
        }

        if (position != maxIndex) {
            swap(position, maxIndex);
            sinkDown(maxIndex);
        }
    }

    private void swap(int a, int b) {
        User temp = heap[a];
        heap[a] = heap[b];
        heap[b] = temp;
    }
}
