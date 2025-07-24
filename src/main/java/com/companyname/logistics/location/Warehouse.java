package com.companyname.logistics.location;

import com.companyname.logistics.resource.Resource;

import java.util.HashMap;
import java.util.Map;

public class Warehouse extends Location {
    private Map<Resource, Integer> resourceStock = new HashMap<>();

    public Warehouse(String name) {
        super(name);
    }

    public void addResource(Resource resource, int quantity) {
        resourceStock.put(resource, quantity);
    }

    public void removeResource(Resource resource) {
        resourceStock.remove(resource);
    }

    public void printStock() {
        resourceStock.entrySet().stream()
                .forEach(entry -> System.out.println(
                        entry.getKey().getResourceType() + " — " + entry.getValue() + " pr."
                ));
    }

}
