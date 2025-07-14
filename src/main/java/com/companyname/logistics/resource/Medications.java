package com.companyname.logistics.resource;

import com.companyname.logistics.enums.CargoCondition;

public class Medications extends Resource {
    public Medications(double volume) {
        super(volume, "ml", CargoCondition.REFRIGERATED);
    }

    @Override
    public String toString() {
        return "Medications{" + getWeight() + " " + getUnit() + '}';
    }

    @Override
    public String getResourceType() {
        return "Medications";
    }

    @Override
    public String getDescription() {
        return "Medical supplies suitable for safe and temperature-controlled delivery.";
    }
}
