package com.companyname.logistics.transport;

import com.companyname.logistics.enums.TransportStatus;
import com.companyname.logistics.location.Location;
import com.companyname.logistics.resource.Resource;

import java.time.LocalDateTime;

public abstract class Transport<T extends Resource> {
    private String name;
    private double capacity;
    private double speed;
    private TransportStatus status = TransportStatus.AVAILABLE;
    private int tripCount = 0;
    private LocalDateTime maintenanceUntil;

    public Transport(String name, double capacity, double speed) {
        this.name = name;
        this.capacity = capacity;
        this.speed = speed;
    }

    public String getName() {
        return name;
    }

    public double getCapacity() {
        return capacity;
    }

    public double getSpeed() {
        return speed;
    }

    public TransportStatus getStatus() {
        return status;
    }

    public void setStatus(TransportStatus status) {
        this.status = status;
    }

    public int getTripCount() {
        return tripCount;
    }

    public void incrementTripCount() {
        this.tripCount++;
    }

    public LocalDateTime getMaintenanceUntil() {
        return maintenanceUntil;
    }

    public void setMaintenanceUntil(LocalDateTime maintenanceUntil) {
        this.maintenanceUntil = maintenanceUntil;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setCapacity(double capacity) {
        this.capacity = capacity;
    }

    public void setSpeed(double speed) {
        this.speed = speed;
    }

    public abstract void deliver(T resource, Location from, Location to);

    @Override
    public String toString() {
        return "Transport{" +
                "id='" + name + '\'' +
                ", capacity=" + capacity +
                ", speed=" + speed +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Transport<?> transport)) return false;
        return name.equals(transport.name);
    }

    @Override
    public int hashCode() {
        return name.hashCode();
    }
}