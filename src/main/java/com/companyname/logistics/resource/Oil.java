package com.companyname.logistics.resource;


import com.companyname.logistics.enums.CargoCondition;

public class Oil extends Resource {
    public Oil(double volume) {
        super(volume, "liters",CargoCondition.DRY);
    }


    @Override
    public String toString() {
        return "Oil{" + getWeight() + " " + getUnit() + '}';
    }


    @Override
    public String getResourceType() {
        return "Oil";
    }

    @Override
    public String getDescription() {
        return "Industrial-grade oil used for fuel or lubrication.";
    }
}