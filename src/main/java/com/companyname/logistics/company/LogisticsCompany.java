package com.companyname.logistics.company;

import com.companyname.logistics.interfaces.DescribableCompany;
import com.companyname.logistics.interfaces.TripManager;
import com.companyname.logistics.resource.Resource;
import com.companyname.logistics.trip.Trip;
import com.companyname.logistics.transport.*;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.*;

public class LogisticsCompany extends Company implements TripManager, DescribableCompany {

    private static final Logger LOGGER = LogManager.getLogger(LogisticsCompany.class);

    private final List<Trip<?, ?>> trips = new ArrayList<>();
    private final Stack<Trip<?, ?>> tripHistoryStack = new Stack<>();

    private final Map<Class<?>, Queue<Transport<?>>> transportQueues = new HashMap<>();

    public LogisticsCompany(String name) {
        super(name);

        transportQueues.put(Truck.class, new LinkedList<>());
        transportQueues.put(Plane.class, new LinkedList<>());
        transportQueues.put(Train.class, new LinkedList<>());
        transportQueues.put(Ship.class, new LinkedList<>());
    }

    @Override
    public void organizeTrip(Trip<?, ?> trip) {
        addTrip(trip);
        tripHistoryStack.push(trip);
        LOGGER.info("Company {} organizes trip:", getName());
        trip.start();
    }

    @Override
    public void addTrip(Trip<?, ?> trip) {
        trips.add(trip);
        LOGGER.info("Trip added: {}", trip);
    }

    @Override
    public void removeTrip(Trip<?, ?> trip) {
        trips.remove(trip);
        LOGGER.info("Trip removed: {}", trip);
    }

    @Override
    public List<Trip<?, ?>> getAllTrips() {
        return trips;
    }

    @Override
    public int getNumberOfTrips() {
        return trips.size();
    }

    @Override
    public String getDescription() {
        return "Company '" + getName() + "' organized " + getNumberOfTrips() + " trips.";
    }

    public <R extends Resource> void addTransport(Transport<R> transport) {
        Queue<Transport<?>> queue = transportQueues.get(transport.getClass());
        if (queue != null) {
            queue.offer(transport);
            LOGGER.info("{} added to queue: {}", transport.getClass().getSimpleName(), transport);
        } else {
            LOGGER.warn("No queue for transport type: {}", transport.getClass().getSimpleName());
        }
    }

    public <T extends Transport<?>> T dispatchTransport(Class<T> transportType) {
        Queue<Transport<?>> queue = transportQueues.get(transportType);
        if (queue != null && !queue.isEmpty()) {
            Transport<?> transport = queue.poll();
            LOGGER.info("Dispatched {}: {}", transportType.getSimpleName(), transport);
            return transportType.cast(transport);
        } else {
            LOGGER.warn("No {}s in queue to dispatch.", transportType.getSimpleName());
            return null;
        }
    }

    public Trip<?, ?> getLastOrganizedTrip() {
        if (tripHistoryStack.isEmpty()) {
            throw new NoSuchElementException("No trips in history");
        }
        return tripHistoryStack.peek();
    }
}
