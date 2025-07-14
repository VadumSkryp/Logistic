package com.companyname.logistics.trip;

import com.companyname.logistics.enums.TransportStatus;
import com.companyname.logistics.enums.TripPriority;
import com.companyname.logistics.exceptions.InvalidRouteException;
import com.companyname.logistics.exceptions.InvalidSpeedException;
import com.companyname.logistics.exceptions.LoadCapacityExceededException;
import com.companyname.logistics.interfaces.Schedulable;
import com.companyname.logistics.location.Location;
import com.companyname.logistics.transport.Transport;
import com.companyname.logistics.resource.Resource;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Objects;
import java.time.LocalDateTime;

public class Trip<T extends Transport<R>, R extends Resource> implements Schedulable {

    private static final Logger LOGGER = LogManager.getLogger(Trip.class);


    private T transport;
    private R resource;
    private Location from;
    private Location to;
    private double totalDistance;
    private TripPriority priority;
    private LocalDateTime scheduledStartTime;

    public Trip(T transport, R resource, Location from, Location to, double totalDistance, TripPriority priority) {
        this.transport = transport;
        this.resource = resource;
        this.from = from;
        this.to = to;
        this.totalDistance = totalDistance;
        this.priority = priority;
    }

    public double getTotalDistance() {
        return totalDistance;
    }

    public void setTotalDistance(double totalDistance) {
        this.totalDistance = totalDistance;
    }

    public T getTransport() {
        return transport;
    }

    public R getResource() {
        return resource;
    }

    public Location getFrom() {
        return from;
    }

    public Location getTo() {
        return to;
    }

    public void setTransport(T transport) {
        this.transport = transport;
    }

    public void setResource(R resource) {
        this.resource = resource;
    }

    public void setFrom(Location from) {
        this.from = from;
    }

    public void setTo(Location to) {
        this.to = to;
    }

    public TripPriority getPriority() {
        return priority;
    }

    public void setPriority(TripPriority priority) {
        this.priority = priority;
    }

    @Override
    public void schedule(LocalDateTime startTime) {
        this.scheduledStartTime = startTime;
        LOGGER.info("Trip scheduled for: {}", startTime);
    }

    @Override
    public LocalDateTime getScheduledStartTime() {
        return scheduledStartTime;
    }

    public void checkRoute() throws InvalidRouteException {
        if (from == null || to == null) {
            throw new InvalidRouteException("Route is not defined: origin or destination is missing.");
        }
        if (Objects.equals(from.getName(), to.getName())) {
            throw new InvalidRouteException("Starting location and destination cannot be the same.");
        }
    }

    public void validateLoad() throws LoadCapacityExceededException {
        if (resource.getWeight() > transport.getCapacity()) {
            throw new LoadCapacityExceededException("Load exceeds transport capacity");
        }
    }

    public double calculateDrivingTime() throws InvalidSpeedException {
        if (transport.getSpeed() <= 0) {
            throw new InvalidSpeedException("Speed cannot be less than or equal to zero.");
        }
        return totalDistance / transport.getSpeed();
    }

    public void start() {
        LOGGER.info("Starting trip");
        try {
            checkRoute();
            validateLoad();

            double time = calculateDrivingTime();
            transport.setStatus(TransportStatus.IN_TRANSIT);
            transport.deliver(resource, from, to);
            LOGGER.info("Estimated delivery time: {} hours", String.format("%.2f", time));

        } catch (LoadCapacityExceededException | InvalidSpeedException | InvalidRouteException e) {
            LOGGER.error("Cannot start trip: {}", e.getMessage());
        }
    }

    @Override
    public String toString() {
        return "Trip{" +
                "transport=" + transport +
                ", resource=" + resource +
                ", from=" + from +
                ", to=" + to +
                ", totalDistance=" + totalDistance +
                '}';
    }
}
