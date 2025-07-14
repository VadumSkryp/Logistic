package com.companyname.logistics.service;

import com.companyname.logistics.exceptions.TransportUnavailableException;
import com.companyname.logistics.interfaces.AvailabilityCheckable;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class TransportService {

    private static final Logger LOGGER = LogManager.getLogger(TransportService.class);

    public void checkAvailability(AvailabilityCheckable transport) throws TransportUnavailableException {
        if (!transport.isAvailable()) {
            throw new TransportUnavailableException("Transport is currently unavailable.");
        }
        LOGGER.info("Transport is available for delivery.");
    }
}
