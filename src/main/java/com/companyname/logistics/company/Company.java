package com.companyname.logistics.company;

import com.companyname.logistics.trip.Trip;

public abstract class Company {
    private String name;

    public Company(String name) {
        this.name = name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }


    public abstract void organizeTrip(Trip<?, ?> trip);

    @Override
    public String toString() {
        return "Company{" +
                "name='" + name + '\'' +
                '}';
    }
}
