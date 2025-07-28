package com.companyname.logistics.records;

import com.companyname.logistics.annotations.ReportField;

public record SummaryReport(

        @ReportField(description = "Total number of trips planned")
        int totalTrips,

        @ReportField(description = "Total weight of all transported resources (kg)")
        double totalWeight,

        @ReportField(description = "Total distance covered (km)")
        double totalDistance

) {}
