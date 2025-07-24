package com.companyname.logistics.functionalInterfaces;

import com.companyname.logistics.resource.Resource;

@FunctionalInterface
public interface ResourceWeightChecker {
    boolean isHeavy(Resource resource);
}
