package com.upwork.healthchecker.domain.dto;

/**
 * Created by rajaniy on 4/25/18.
 */
public class HealthResponse {
    private final String serviceName;
    private final boolean healthy;
    private final String message;

    public HealthResponse(String serviceName) {
        this(serviceName, true, "OK");
    }

    public HealthResponse(String serviceName, boolean healthy, String message) {
        this.serviceName = serviceName;
        this.healthy = healthy;
        this.message = message;
    }

    public String getServiceName() {
        return serviceName;
    }

    public boolean isHealthy() {
        return healthy;
    }

    public String getMessage() {
        return message;
    }
}
