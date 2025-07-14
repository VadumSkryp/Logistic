package com.companyname.logistics.service;

import com.companyname.logistics.enums.TransportStatus;
import com.companyname.logistics.exceptions.TransportNotReadyException;
import com.companyname.logistics.transport.Transport;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.time.LocalDateTime;

public class TransportStatusService {

    private static final Logger LOGGER = LogManager.getLogger(TransportStatusService.class);

    public void checkTransportReady(Transport<?> transport) throws TransportNotReadyException {
        TransportStatus status = transport.getStatus();

        if (status == TransportStatus.UNAVAILABLE) {
            throw new TransportNotReadyException("Transport " + transport.getName() + " is marked as UNAVAILABLE.");
        }

        if (status == TransportStatus.MAINTENANCE) {
            LocalDateTime now = LocalDateTime.now();
            if (transport.getMaintenanceUntil() != null && now.isBefore(transport.getMaintenanceUntil())) {
                throw new TransportNotReadyException("Transport " + transport.getName() + " is under maintenance until " + transport.getMaintenanceUntil());
            } else {
                transport.setStatus(TransportStatus.AVAILABLE);
                LOGGER.info("Transport {} is now AVAILABLE after maintenance", transport.getName());
            }
        }

        if (status != TransportStatus.AVAILABLE) {
            throw new TransportNotReadyException("Transport " + transport.getName() + " is not available. Current status: " + status);
        }
    }

    public void startTrip(Transport<?> transport) {
        transport.setStatus(TransportStatus.IN_TRANSIT);
        LOGGER.info("Transport {} status set to IN_TRANSIT", transport.getName());
    }

    public void completeTrip(Transport<?> transport) {
        transport.incrementTripCount();

        if (transport.getTripCount() >= 3) {
            transport.setStatus(TransportStatus.MAINTENANCE);
            transport.setMaintenanceUntil(LocalDateTime.now().plusHours(1));
            LOGGER.warn("Transport {} sent to MAINTENANCE for 1 hour", transport.getName());
        } else {
            transport.setStatus(TransportStatus.AVAILABLE);
            LOGGER.info("Transport {} status set to AVAILABLE after trip", transport.getName());
        }
    }
}
