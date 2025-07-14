import com.companyname.logistics.company.LogisticsCompany;
import com.companyname.logistics.enums.TripPriority;
import com.companyname.logistics.interfaces.Schedulable;
import com.companyname.logistics.location.City;
import com.companyname.logistics.location.Warehouse;
import com.companyname.logistics.resource.*;
import com.companyname.logistics.service.*;
import com.companyname.logistics.transport.*;
import com.companyname.logistics.trip.Trip;
import com.companyname.logistics.exceptions.*;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.List;

public class Main {

    private static final Logger LOGGER = LogManager.getLogger(Main.class);

    public static void main(String[] args) {

        LogisticsCompany company = new LogisticsCompany("Global Logistics");

        Warehouse kyivWarehouse = new Warehouse("Kyiv Warehouse");
        Warehouse odessaWarehouse = new Warehouse("Odessa Warehouse");
        City kyiv = new City("Kyiv");
        City odessa = new City("Odessa");
        City lviv = new City("Lviv");

        Food apples = new Food(1000);
        Medications meds = new Medications(500);
        Grain wheat = new Grain(2000);
        Oil oil = new Oil(1500);

        kyivWarehouse.addResource(apples, 100);
        kyivWarehouse.addResource(meds, 50);
        odessaWarehouse.addResource(wheat, 150);
        odessaWarehouse.addResource(oil, 75);

        company.addTruck(new Truck("Truck A", 1500, 80));
        company.addPlane(new Plane("Plane A", 600, 900));
        company.addTrain(new Train("Train A", 2500, 120));
        company.addShip(new Ship("Ship A", 2000, 40));

        TransportService transportService = new TransportService();
        SchedulingService schedulingService = new SchedulingService();
        ResourcePrinter resourcePrinter = new ResourcePrinter();
        TransportStatusService transportStatusService = new TransportStatusService();

        List<Trip<?, ?>> scheduledTrips = new ArrayList<>();
        Container<Resource> transportedResources = new Container<>();

        // Trip 1 — Truck + Food
        Truck truck = company.dispatchTruck();
        if (truck == null) {
            LOGGER.error("No trucks available for dispatch.");
        } else {
            try {
                transportStatusService.checkTransportReady(truck);

                Trip<Truck, Food> trip1 = new Trip<>(truck, apples, kyivWarehouse, lviv, 540, TripPriority.MEDIUM);

                List<Schedulable> schedulableTrips = new ArrayList<>(scheduledTrips);
                schedulingService.planTrip(trip1, schedulableTrips);
                scheduledTrips.add(trip1);
                company.organizeTrip(trip1);

                transportStatusService.startTrip(truck);

                resourcePrinter.printResourceInfo(apples);
                transportedResources.addItem(apples);

                transportStatusService.completeTrip(truck);

            } catch (ScheduleConflictException | TransportNotReadyException e) {
                LOGGER.error("Trip 1 failed: {}", e.getMessage());
            }
        }

        // Trip 2 — Plane + Medications
        Plane plane = company.dispatchPlane();
        if (plane == null) {
            LOGGER.error("No planes available for dispatch.");
        } else {
            try {
                transportStatusService.checkTransportReady(plane);

                Trip<Plane, Medications> trip2 = new Trip<>(plane, meds, kyivWarehouse, odessa, 450, TripPriority.CRITICAL);

                List<Schedulable> schedulableTrips = new ArrayList<>(scheduledTrips);
                schedulingService.planTrip(trip2, schedulableTrips);
                scheduledTrips.add(trip2);
                company.organizeTrip(trip2);

                transportStatusService.startTrip(plane);

                resourcePrinter.printResourceInfo(meds);
                transportedResources.addItem(meds);

                transportStatusService.completeTrip(plane);

            } catch (ScheduleConflictException | TransportNotReadyException e) {
                LOGGER.error("Trip 2 failed: {}", e.getMessage());
            }
        }

        // Trip 3 — Train + Grain
        Train train = company.dispatchTrain();
        if (train == null) {
            LOGGER.error("No trains available for dispatch.");
        } else {
            try {
                transportStatusService.checkTransportReady(train);

                Trip<Train, Grain> trip3 = new Trip<>(train, wheat, odessaWarehouse, kyiv, 480, TripPriority.LOW);

                List<Schedulable> schedulableTrips = new ArrayList<>(scheduledTrips);
                schedulingService.planTrip(trip3, schedulableTrips);
                scheduledTrips.add(trip3);
                company.organizeTrip(trip3);

                transportStatusService.startTrip(train);

                resourcePrinter.printResourceInfo(wheat);
                transportedResources.addItem(wheat);

                transportStatusService.completeTrip(train);

            } catch (ScheduleConflictException | TransportNotReadyException e) {
                LOGGER.error("Trip 3 failed: {}", e.getMessage());
            }
        }

        // Trip 4 — Ship + Oil
        Ship ship = company.dispatchShip();
        if (ship == null) {
            LOGGER.error("No ships available for dispatch.");
        } else {
            try {
                transportStatusService.checkTransportReady(ship);

                Trip<Ship, Oil> trip4 = new Trip<>(ship, oil, odessaWarehouse, lviv, 680, TripPriority.MEDIUM);

                List<Schedulable> schedulableTrips = new ArrayList<>(scheduledTrips);
                schedulingService.planTrip(trip4, schedulableTrips);
                scheduledTrips.add(trip4);
                company.organizeTrip(trip4);

                transportStatusService.startTrip(ship);

                resourcePrinter.printResourceInfo(oil);
                transportedResources.addItem(oil);

                transportStatusService.completeTrip(ship);

            } catch (ScheduleConflictException | TransportNotReadyException e) {
                LOGGER.error("Trip 4 failed: {}", e.getMessage());
            }
        }

        printTransportedResources(transportedResources);
    }

    public static void printTransportedResources(Container<Resource> container) {
        LOGGER.info("\n=== Transported Resources ===");
        if (container.getItems().isEmpty()) {
            LOGGER.info("No resources were transported.");
        } else {
            for (Resource resource : container.getItems()) {
                String type = resource.getClass().getSimpleName();
                double weight = resource.getWeight();
                LOGGER.info("- {} ({} kg)", type, weight);
            }
        }
        LOGGER.info("==============================\n");
    }
}
