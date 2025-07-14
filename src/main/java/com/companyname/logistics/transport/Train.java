package com.companyname.logistics.transport;

import com.companyname.logistics.enums.TransportStatus;
import com.companyname.logistics.location.Location;
import com.companyname.logistics.resource.Grain;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


public class Train extends Transport<Grain> {
    private TransportStatus status = TransportStatus.AVAILABLE;
    private static final Logger LOGGER = LogManager.getLogger(Train.class);



    public Train(String name, double capacity, double speed) {
        super(name, capacity, speed);
    }


    @Override
    public void deliver(Grain resource, Location from, Location to) {
        LOGGER.info("Transport {} delivers {} from {} to {}",
                getName(), resource, from.getName(), to.getName());


    }

    public boolean isAvailable() {
        return status == TransportStatus.AVAILABLE;
    }

    public void setAvailable(boolean available) {
        this.status = available ? TransportStatus.AVAILABLE : TransportStatus.UNAVAILABLE;
    }


}
