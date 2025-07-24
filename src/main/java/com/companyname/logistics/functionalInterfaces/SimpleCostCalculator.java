package com.companyname.logistics.functionalInterfaces;

import com.companyname.logistics.enums.TripPriority;

@FunctionalInterface
public interface SimpleCostCalculator {
    double calculate(double weight, int distance, TripPriority priority);
}
