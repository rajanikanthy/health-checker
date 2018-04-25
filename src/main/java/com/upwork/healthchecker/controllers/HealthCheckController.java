package com.upwork.healthchecker.controllers;

import com.fasterxml.jackson.databind.JsonNode;
import com.upwork.healthchecker.HealthCheckerApplication;
import com.upwork.healthchecker.domain.ServiceRegistry;
import com.upwork.healthchecker.domain.ServiceType;
import com.upwork.healthchecker.domain.dto.HealthResponse;
import com.upwork.healthchecker.domain.dto.PlatformHealthResponse;
import com.upwork.healthchecker.mappers.ServiceRegistryMapper;
import com.upwork.healthchecker.tasks.DatabaseHealthChecker;
import com.upwork.healthchecker.tasks.HTTPHealthChecker;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * Created by rajaniy on 4/24/18.
 */
@RestController
@RequestMapping("/api/v1")
public class HealthCheckController {
    private static final Logger _logger = LoggerFactory.getLogger(HealthCheckerApplication.class);

    private ServiceRegistryMapper serviceRegistryMapper;

    @Autowired
    public HealthCheckController(ServiceRegistryMapper serviceRegistryMapper) {
        this.serviceRegistryMapper = serviceRegistryMapper;
    }

    @GetMapping(value = "/run", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<JsonNode> runHealthCheck() throws Exception {
        PlatformHealthResponse platformHealthResponse = new PlatformHealthResponse();
        for(ServiceRegistry serviceRegistry : serviceRegistryMapper.findAll()) {
            if (serviceRegistry.getServicetype() == ServiceType.HTTP) {
                ExecutorService executorService = Executors.newSingleThreadExecutor();
                try {
                    HTTPHealthChecker httpHealthChecker = new HTTPHealthChecker(serviceRegistry);
                    Future<String> future = executorService.submit(httpHealthChecker);
                    String response = future.get();
                    platformHealthResponse.addHealthResponse(new HealthResponse(serviceRegistry.getName(), true, "OK"));
                    executorService.shutdown();
                } catch (Throwable t) {
                    platformHealthResponse.addHealthResponse(new HealthResponse(serviceRegistry.getName(), false, t.getMessage()));
                } finally {
                    executorService.shutdown();
                }

            } else if (serviceRegistry.getServicetype() == ServiceType.SQL) {
                ExecutorService executorService = Executors.newSingleThreadExecutor();
                try {
                    DatabaseHealthChecker dbHealthChecker = new DatabaseHealthChecker(serviceRegistry);
                    Future<String> future = executorService.submit(dbHealthChecker);
                    String response = future.get();
                    platformHealthResponse.addHealthResponse(new HealthResponse(serviceRegistry.getName(), true, "OK"));
                } catch (Throwable t) {
                    platformHealthResponse.addHealthResponse(new HealthResponse(serviceRegistry.getName(), false, t.getMessage()));
                }
                executorService.shutdown();
            }
        }
        return ResponseEntity.ok(platformHealthResponse.generateResponse());
    }
}
