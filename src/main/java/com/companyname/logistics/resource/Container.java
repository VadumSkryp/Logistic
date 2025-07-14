package com.companyname.logistics.resource;

import java.util.*;


public class Container<T extends Resource> {
    private Set<T> items = new HashSet<>();

    public void addItem(T item) {
        items.add(item);
    }

    public void removeItem(T item) {
        items.remove(item);
    }


    public Set<T> getItems() {
        return Collections.unmodifiableSet(items);
    }
}
