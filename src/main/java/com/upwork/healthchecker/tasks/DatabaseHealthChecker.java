package com.upwork.healthchecker.tasks;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.upwork.healthchecker.domain.ServiceRegistry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.concurrent.Callable;

/**
 * Created by rajaniy on 4/24/18.
 */
public class DatabaseHealthChecker implements Callable<String> {

    private ServiceRegistry serviceRegistry;

    private static final String DEFAULT_JDBC_DRIVER = "org.postgresql.Driver";

    @Autowired
    public DatabaseHealthChecker(ServiceRegistry serviceRegistry) {
        this.serviceRegistry = serviceRegistry;
    }

    @Override
    public String call() throws Exception {
        Class<?> driver  = Class.forName(DEFAULT_JDBC_DRIVER);
        if (StringUtils.hasText(serviceRegistry.getUri())) {
            Connection connection = DriverManager.getConnection(serviceRegistry.getUri());
            Statement st = connection.createStatement();
            ResultSet rs = st.executeQuery(serviceRegistry.getQuery());
            int rowCount = 0;
            while(rs.next()) {
                rowCount++;
            }
            return "Rows returned " + rowCount;
        }
        return "Rows returned zero";
    }
}
