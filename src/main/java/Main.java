import com.companyname.logistics.company.LogisticsCompany;
import com.companyname.logistics.enums.TripPriority;
import com.companyname.logistics.exceptions.ScheduleConflictException;
import com.companyname.logistics.exceptions.TransportNotReadyException;

import com.companyname.logistics.functionalInterfaces.SimpleCostCalculator;
import com.companyname.logistics.functionalInterfaces.ResourceWeightChecker;
import com.companyname.logistics.functionalInterfaces.TransportSpeedEvaluator;
import com.companyname.logistics.location.City;
import com.companyname.logistics.location.Warehouse;
import com.companyname.logistics.resource.*;
import com.companyname.logistics.service.*;
import com.companyname.logistics.transport.*;
import com.companyname.logistics.trip.Trip;

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

        SimpleCostCalculator costCalculator = (weight, distance, priority) -> {
            double baseCost = weight * distance * 0.5;
            double priorityMultiplier = switch (priority) {
                case CRITICAL -> 4.0;
                case HIGH -> 3.5;
                case MEDIUM -> 2.0;
                case LOW -> 1.5;
            };
            return baseCost * priorityMultiplier;
        };

        ResourceWeightChecker isHeavyChecker = resource -> resource.getWeight() > 1000;

        TransportSpeedEvaluator speedEvaluator = transport -> transport.getSpeed() > 100;

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

                schedulingService.planTrip(trip1, new ArrayList<>(scheduledTrips));
                scheduledTrips.add(trip1);
                company.organizeTrip(trip1);

                transportStatusService.startTrip(truck);

                if (speedEvaluator.isFast(truck)) {
                    LOGGER.info("Trip 1: Truck is fast.");
                } else {
                    LOGGER.info("Trip 1: Truck is slow.");
                }

                double costTrip1 = costCalculator.calculate(apples.getWeight(), 540, TripPriority.MEDIUM);
                LOGGER.info("Cost of delivery for Trip 1: {} UAH", costTrip1);

                if (isHeavyChecker.isHeavy(apples)) {
                    LOGGER.info("Trip 1: Resource is heavy.");
                } else {
                    LOGGER.info("Trip 1: Resource is light.");
                }

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

                schedulingService.planTrip(trip2, new ArrayList<>(scheduledTrips));
                scheduledTrips.add(trip2);
                company.organizeTrip(trip2);

                transportStatusService.startTrip(plane);

                if (speedEvaluator.isFast(plane)) {
                    LOGGER.info("Trip 2: Plane is fast.");
                } else {
                    LOGGER.info("Trip 2: Plane is slow.");
                }

                double costTrip2 = costCalculator.calculate(meds.getWeight(), 450, TripPriority.CRITICAL);
                LOGGER.info("Cost of delivery for Trip 2: {} UAH", costTrip2);

                if (isHeavyChecker.isHeavy(meds)) {
                    LOGGER.info("Trip 2: Resource is heavy.");
                } else {
                    LOGGER.info("Trip 2: Resource is light.");
                }

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

                schedulingService.planTrip(trip3, new ArrayList<>(scheduledTrips));
                scheduledTrips.add(trip3);
                company.organizeTrip(trip3);

                transportStatusService.startTrip(train);

                if (speedEvaluator.isFast(train)) {
                    LOGGER.info("Trip 3: Train is fast.");
                } else {
                    LOGGER.info("Trip 3: Train is slow.");
                }

                double costTrip3 = costCalculator.calculate(wheat.getWeight(), 480, TripPriority.LOW);
                LOGGER.info("Cost of delivery for Trip 3: {} UAH", costTrip3);

                if (isHeavyChecker.isHeavy(wheat)) {
                    LOGGER.info("Trip 3: Resource is heavy.");
                } else {
                    LOGGER.info("Trip 3: Resource is light.");
                }

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

                schedulingService.planTrip(trip4, new ArrayList<>(scheduledTrips));
                scheduledTrips.add(trip4);
                company.organizeTrip(trip4);

                transportStatusService.startTrip(ship);

                if (speedEvaluator.isFast(ship)) {
                    LOGGER.info("Trip 4: Ship is fast.");
                } else {
                    LOGGER.info("Trip 4: Ship is slow.");
                }

                double costTrip4 = costCalculator.calculate(oil.getWeight(), 680, TripPriority.MEDIUM);
                LOGGER.info("Cost of delivery for Trip 4: {} UAH", costTrip4);

                if (isHeavyChecker.isHeavy(oil)) {
                    LOGGER.info("Trip 4: Resource is heavy.");
                } else {
                    LOGGER.info("Trip 4: Resource is light.");
                }

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
            container.getItems().stream()
                    .map(resource -> {
                        String type = resource.getClass().getSimpleName();
                        double weight = resource.getWeight();
                        String unit;

                        if (resource instanceof Medications) {
                            unit = "ml";
                        } else if (resource instanceof Oil) {
                            unit = "liters";
                        } else {
                            unit = "kg";
                        }

                        return String.format("- %s (%.2f %s)", type, weight, unit);
                    })
                    .forEach(LOGGER::info);
        }
        LOGGER.info("==============================\n");
    }
}
