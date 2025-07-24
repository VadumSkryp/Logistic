package com.companyname.logistics.service;

import com.companyname.logistics.exceptions.ScheduleConflictException;
import com.companyname.logistics.interfaces.Schedulable;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.time.LocalDateTime;
import java.util.List;

public class SchedulingService {

    private static final Logger LOGGER = LogManager.getLogger(Schedulable.class);


    public void planTrip(Schedulable schedulable, List<Schedulable> existingTrips) throws ScheduleConflictException {
        LocalDateTime plannedTime = LocalDateTime.now().plusHours(3);


        if (existingTrips.stream()
                .anyMatch(trip -> trip.getScheduledStartTime() != null &&
                        trip.getScheduledStartTime().equals(plannedTime))) {
            throw new ScheduleConflictException("There is already a trip scheduled at this time: " + plannedTime);
        }


        schedulable.schedule(plannedTime);
        LOGGER.info("Trip scheduled for: {}", schedulable.getScheduledStartTime());
    }
}
