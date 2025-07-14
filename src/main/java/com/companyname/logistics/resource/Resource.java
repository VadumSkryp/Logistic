package com.companyname.logistics.resource;

import com.companyname.logistics.enums.CargoCondition;
import com.companyname.logistics.interfaces.ClassifiableResource;

public abstract class Resource implements ClassifiableResource {
    private double weight;
    private String unit;
    private CargoCondition cargoCondition;

    public Resource(double weight, String unit,CargoCondition cargoCondition) {
        this.weight = weight;
        this.unit = unit;
        this.cargoCondition = cargoCondition;
    }

    public CargoCondition getCargoCondition() {
        return cargoCondition;
    }

    public void setCargoCondition(CargoCondition cargoCondition) {
        this.cargoCondition = cargoCondition;
    }

    public double getWeight() {
        return weight;
    }

    public String getUnit() {
        return unit;
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    @Override
    public abstract String toString();


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Resource resource)) return false;
        return Double.compare(resource.weight, weight) == 0 &&
                unit.equals(resource.unit) &&
                this.getClass().equals(resource.getClass());
    }

    @Override
    public int hashCode() {
        int result = Double.hashCode(weight);
        result = 31 * result + unit.hashCode();
        result = 31 * result + getClass().hashCode();
        return result;
    }
}