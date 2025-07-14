package com.companyname.logistics.service;

import com.companyname.logistics.interfaces.ClassifiableResource;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class ResourcePrinter {

    private static final Logger LOGGER = LogManager.getLogger(ResourcePrinter.class);

    public void printResourceInfo(ClassifiableResource resource) {
        LOGGER.info("Type: {}", resource.getResourceType());
        LOGGER.info("Info: {}", resource.getDescription());
    }

}
