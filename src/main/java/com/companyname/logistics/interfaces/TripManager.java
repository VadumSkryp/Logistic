package com.companyname.logistics.interfaces;

import com.companyname.logistics.trip.Trip;

import java.util.List;

public interface TripManager {
    void organizeTrip(Trip<?, ?> trip);

    void addTrip(Trip<?, ?> trip);

    void removeTrip(Trip<?, ?> trip);

    List<Trip<?, ?>> getAllTrips();
}
