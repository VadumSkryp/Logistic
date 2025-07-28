package com.companyname.logistics.utils;

import com.companyname.logistics.annotations.ReportField;

import java.lang.reflect.RecordComponent;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class ReportUtils {

    private static final Logger LOGGER = LogManager.getLogger(ReportUtils.class);

    public static void printReportDetails(Object record) {
        Class<?> clazz = record.getClass();

        if (!clazz.isRecord()) {
            LOGGER.warn("The object is not a record.");
            return;
        }

        LOGGER.info("=== Report Details ===");
        for (RecordComponent component : clazz.getRecordComponents()) {
            ReportField annotation = component.getAnnotation(ReportField.class);

            if (annotation != null) {
                try {
                    Object value = component.getAccessor().invoke(record);
                    LOGGER.info("{}: {} ({})",
                            component.getName(),
                            value,
                            annotation.description());
                } catch (Exception e) {
                    LOGGER.error("Error accessing record component: {}", component.getName(), e);
                }
            }
        }
        LOGGER.info("======================");
    }
}
