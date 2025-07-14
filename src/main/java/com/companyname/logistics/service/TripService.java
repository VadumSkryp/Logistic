package com.companyname.logistics.service;

import com.companyname.logistics.interfaces.TripManager;
import com.companyname.logistics.trip.Trip;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class TripService {

    private static final Logger LOGGER = LogManager.getLogger(TransportService.class);


    public void manageTrips(TripManager manager, Trip<?, ?> trip) {
        manager.addTrip(trip);
        LOGGER.info("Current trips: {}", manager.getAllTrips());

    }
}
