package com.companyname.logistics.service;

import com.companyname.logistics.interfaces.DescribableCompany;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class CompanyDescriptionService {

    private static final Logger LOGGER = LogManager.getLogger(CompanyDescriptionService.class);

    public void printDescription(DescribableCompany company) {
        LOGGER.info(company.getDescription());

    }
}
