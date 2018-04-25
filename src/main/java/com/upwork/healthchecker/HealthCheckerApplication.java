package com.upwork.healthchecker;

import com.upwork.healthchecker.domain.ServiceRegistry;
import com.upwork.healthchecker.domain.ServiceType;
import com.upwork.healthchecker.mappers.ServiceRegistryMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class HealthCheckerApplication {

    private static final Logger _logger = LoggerFactory.getLogger(HealthCheckerApplication.class);

    public static void main(String[] args) {
        SpringApplication.run(HealthCheckerApplication.class, args);
    }

}
