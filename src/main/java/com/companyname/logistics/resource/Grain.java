package com.companyname.logistics.resource;

import com.companyname.logistics.enums.CargoCondition;

public class Grain extends Resource {


    public Grain(double weight) {
        super(weight, "kg", CargoCondition.FRAGILE);
    }

    @Override
    public String toString() {
        return "Grain{" + getWeight() + " " + getUnit() + '}';
    }

    @Override
    public String getResourceType() {
        return "Grain";
    }

    @Override
    public String getDescription() {
        return "Bulk grain products suitable for storage and long-distance transportation.";
    }
}
