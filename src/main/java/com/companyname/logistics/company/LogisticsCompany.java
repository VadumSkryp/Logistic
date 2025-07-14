package com.companyname.logistics.company;

import com.companyname.logistics.interfaces.DescribableCompany;
import com.companyname.logistics.interfaces.TripManager;
import com.companyname.logistics.trip.Trip;
import com.companyname.logistics.transport.*;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.*;

public class LogisticsCompany extends Company implements TripManager, DescribableCompany {

    private static final Logger LOGGER = LogManager.getLogger(LogisticsCompany.class);

    private List<Trip<?, ?>> trips = new ArrayList<>();
    private Stack<Trip<?, ?>> tripHistoryStack = new Stack<>();


    private Queue<Truck> truckQueue = new LinkedList<>();
    private Queue<Plane> planeQueue = new LinkedList<>();
    private Queue<Train> trainQueue = new LinkedList<>();
    private Queue<Ship> shipQueue = new LinkedList<>();

    public LogisticsCompany(String name) {
        super(name);
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


    public void addTruck(Truck truck) {
        truckQueue.offer(truck);
        LOGGER.info("Truck added to queue: {}", truck);
    }

    public void addPlane(Plane plane) {
        planeQueue.offer(plane);
        LOGGER.info("Plane added to queue: {}", plane);
    }

    public void addTrain(Train train) {
        trainQueue.offer(train);
        LOGGER.info("Train added to queue: {}", train);
    }

    public void addShip(Ship ship) {
        shipQueue.offer(ship);
        LOGGER.info("Ship added to queue: {}", ship);
    }


    public Truck dispatchTruck() {
        Truck truck = truckQueue.poll();
        if (truck != null) {
            LOGGER.info("Dispatched truck: {}", truck);
        } else {
            LOGGER.warn("No trucks in queue to dispatch.");
        }
        return truck;
    }

    public Plane dispatchPlane() {
        Plane plane = planeQueue.poll();
        if (plane != null) {
            LOGGER.info("Dispatched plane: {}", plane);
        } else {
            LOGGER.warn("No planes in queue to dispatch.");
        }
        return plane;
    }

    public Train dispatchTrain() {
        Train train = trainQueue.poll();
        if (train != null) {
            LOGGER.info("Dispatched train: {}", train);
        } else {
            LOGGER.warn("No trains in queue to dispatch.");
        }
        return train;
    }

    public Ship dispatchShip() {
        Ship ship = shipQueue.poll();
        if (ship != null) {
            LOGGER.info("Dispatched ship: {}", ship);
        } else {
            LOGGER.warn("No ships in queue to dispatch.");
        }
        return ship;
    }

    public Trip<?, ?> getLastOrganizedTrip() {
        if (tripHistoryStack.isEmpty()) {
           throw new NoSuchElementException("No trips in history");
        }
        return tripHistoryStack.peek();
    }
}
