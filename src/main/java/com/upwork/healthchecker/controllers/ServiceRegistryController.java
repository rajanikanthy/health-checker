package com.upwork.healthchecker.controllers;

import com.upwork.healthchecker.domain.ServiceRegistry;
import com.upwork.healthchecker.mappers.ServiceRegistryMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;

/**
 * Created by rajaniy on 4/24/18.
 */
@RestController
@RequestMapping("/api/v1")
public class ServiceRegistryController {

    private static final Logger _logger = LoggerFactory.getLogger(ServiceRegistryController.class);

    private ServiceRegistryMapper serviceRegistryMapper;

    public ServiceRegistryController(ServiceRegistryMapper serviceRegistryMapper) {
        this.serviceRegistryMapper = serviceRegistryMapper;
    }

    @GetMapping(value = "/services", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Collection<ServiceRegistry>> getAllServices() {
        return ResponseEntity.ok(serviceRegistryMapper.findAll());
    }

    @PostMapping(value = "/services", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity createService(@RequestBody  ServiceRegistry serviceRegistry) throws Exception {
        ServiceRegistry sr = serviceRegistryMapper.findOne(serviceRegistry.getName());
        if (sr != null) {
            throw new Exception("A service with name " + serviceRegistry.getName() + " exists already");
        }
        serviceRegistryMapper.insert(serviceRegistry);
        return ResponseEntity.ok("Created successfully");
    }

    @DeleteMapping(value ="/services/{name}")
    public ResponseEntity deleteService(@PathVariable(name = "name")String name) {
        if (serviceRegistryMapper.findOne(name) != null) {
            serviceRegistryMapper.delete(name);
            return ResponseEntity.ok("Delete successfully");
        }
        return ResponseEntity.noContent().build();
    }
}
