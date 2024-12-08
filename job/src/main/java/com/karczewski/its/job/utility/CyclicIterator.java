package com.karczewski.its.job.utility;

import java.util.Iterator;
import java.util.List;

public class CyclicIterator<T> implements Iterator<T> {
    private final List<T> list;
    private int currentIndex = 0;

    public CyclicIterator(List<T> list) {
        this.list = list;
    }

    @Override
    public boolean hasNext() {
        return !list.isEmpty();
    }

    @Override
    public T next() {
        if (list.isEmpty()) {
            throw new IllegalStateException("The list is empty");
        }
        T item = list.get(currentIndex);
        currentIndex = (currentIndex + 1) % list.size();
        return item;
    }
}

