package com.companyname.logistics.functionalInterfaces;


import com.companyname.logistics.transport.Transport;

@FunctionalInterface
public interface TransportSpeedEvaluator {

    boolean isFast(Transport<?> transport);


}
