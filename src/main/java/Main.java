import com.companyname.logistics.company.LogisticsCompany;
import com.companyname.logistics.enums.TripPriority;
import com.companyname.logistics.exceptions.ScheduleConflictException;
import com.companyname.logistics.exceptions.TransportNotReadyException;

import com.companyname.logistics.functionalInterfaces.SimpleCostCalculator;
import com.companyname.logistics.functionalInterfaces.ResourceWeightChecker;
import com.companyname.logistics.functionalInterfaces.TransportSpeedEvaluator;
import com.companyname.logistics.location.City;
import com.companyname.logistics.location.Warehouse;
import com.companyname.logistics.records.SummaryReport;
import com.companyname.logistics.resource.*;
import com.companyname.logistics.service.*;
import com.companyname.logistics.transport.*;
import com.companyname.logistics.trip.Trip;

import com.companyname.logistics.utils.ReportUtils;

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

        company.addTransport(new Truck("Truck A", 1500, 80));
        company.addTransport(new Plane("Plane A", 600, 900));
        company.addTransport(new Train("Train A", 2500, 120));
        company.addTransport(new Ship("Ship A", 2000, 40));

        TransportService transportService = new TransportService();
        SchedulingService schedulingService = new SchedulingService();
        ResourcePrinter resourcePrinter = new ResourcePrinter();
        TransportStatusService transportStatusService = new TransportStatusService();

        // Calculates delivery cost based on weight, distance and priority
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

        // Execute delivery trips
        planAndExecuteTrip(company, schedulingService, transportStatusService, resourcePrinter,
                Truck.class, apples, kyivWarehouse, lviv, 540, TripPriority.MEDIUM,
                scheduledTrips, transportedResources, costCalculator, isHeavyChecker, speedEvaluator);

        planAndExecuteTrip(company, schedulingService, transportStatusService, resourcePrinter,
                Plane.class, meds, kyivWarehouse, odessa, 450, TripPriority.CRITICAL,
                scheduledTrips, transportedResources, costCalculator, isHeavyChecker, speedEvaluator);

        planAndExecuteTrip(company, schedulingService, transportStatusService, resourcePrinter,
                Train.class, wheat, odessaWarehouse, kyiv, 480, TripPriority.LOW,
                scheduledTrips, transportedResources, costCalculator, isHeavyChecker, speedEvaluator);

        planAndExecuteTrip(company, schedulingService, transportStatusService, resourcePrinter,
                Ship.class, oil, odessaWarehouse, lviv, 680, TripPriority.MEDIUM,
                scheduledTrips, transportedResources, costCalculator, isHeavyChecker, speedEvaluator);

        // Summarize trip data
        int totalTrips = scheduledTrips.size();
        double totalWeight = transportedResources.getItems().stream()
                .mapToDouble(Resource::getWeight)
                .sum();
        double totalDistance = scheduledTrips.stream()
                .mapToDouble(Trip::getTotalDistance)
                .sum();

        SummaryReport report = new SummaryReport(totalTrips, totalWeight, totalDistance);
        ReportUtils.printReportDetails(report);
    }

    /**
     * Plans and executes a delivery trip.
     */
    public static <T extends Transport<R>, R extends Resource> void planAndExecuteTrip(
            LogisticsCompany company,
            SchedulingService schedulingService,
            TransportStatusService statusService,
            ResourcePrinter printer,
            Class<T> transportClass,
            R resource,
            Warehouse from,
            City to,
            int distance,
            TripPriority priority,
            List<Trip<?, ?>> scheduledTrips,
            Container<Resource> transportedResources,
            SimpleCostCalculator costCalculator,
            ResourceWeightChecker weightChecker,
            TransportSpeedEvaluator speedEvaluator
    ) {
        T transport = company.dispatchTransport(transportClass);
        if (transport == null) {
            LOGGER.error("No {}s available for dispatch.", transportClass.getSimpleName());
            return;
        }

        try {
            statusService.checkTransportReady(transport);

            Trip<T, R> trip = new Trip<>(transport, resource, from, to, distance, priority);
            schedulingService.planTrip(trip, new ArrayList<>(scheduledTrips));
            scheduledTrips.add(trip);
            company.organizeTrip(trip);
            statusService.startTrip(transport);

            if (speedEvaluator.isFast(transport)) {
                LOGGER.info("{} is fast.", transport.getName());
            } else {
                LOGGER.info("{} is slow.", transport.getName());
            }

            double cost = costCalculator.calculate(resource.getWeight(), distance, priority);
            LOGGER.info("Cost of delivery: {} UAH", cost);

            if (weightChecker.isHeavy(resource)) {
                LOGGER.info("Resource is heavy.");
            } else {
                LOGGER.info("Resource is light.");
            }

            printer.printResourceInfo(resource);
            transportedResources.addItem(resource);
            statusService.completeTrip(transport);

        } catch (ScheduleConflictException | TransportNotReadyException e) {
            LOGGER.error("Trip failed: {}", e.getMessage());
        }
    }
}
