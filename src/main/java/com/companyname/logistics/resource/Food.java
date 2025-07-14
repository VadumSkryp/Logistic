package com.companyname.logistics.resource;


import com.companyname.logistics.enums.CargoCondition;

public class Food extends Resource {
    public Food(double volume) {
        super(volume, "kg", CargoCondition.NORMAL);
    }

    @Override
    public String toString() {
        return "Food{" + getWeight() + " " + getUnit() + '}';
    }


    @Override
    public String getResourceType() {
        return "Food";
    }

    @Override
    public String getDescription() {
        return "Perishable food products suitable for transportation.";
    }
}


