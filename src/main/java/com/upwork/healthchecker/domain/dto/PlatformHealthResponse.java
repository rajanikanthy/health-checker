package com.upwork.healthchecker.domain.dto;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

import java.util.Collection;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * Created by rajaniy on 4/25/18.
 */
public class PlatformHealthResponse {
    private boolean overAllHealth;
    private Queue<HealthResponse> healthResponses;

    public PlatformHealthResponse() {
        overAllHealth = true;
        healthResponses = new ConcurrentLinkedQueue<>();
    }

    public PlatformHealthResponse addHealthResponse(HealthResponse healthResponse) {
        if (!healthResponse.isHealthy() && overAllHealth) {
            overAllHealth = healthResponse.isHealthy();
        }
        healthResponses.add(healthResponse);
        return this;
    }

    public JsonNode generateResponse() {
        ObjectMapper mapper = new ObjectMapper();
        ObjectNode root = mapper.createObjectNode();
        root.put("overAllHealth", this.overAllHealth);
        ArrayNode healthResponsesNode = root.putArray("services");
        while (!healthResponses.isEmpty()) {
            HealthResponse healthResponse = healthResponses.poll();
            ObjectNode health = mapper.createObjectNode();
            health.put("service", healthResponse.getServiceName());
            health.put("healthy", healthResponse.isHealthy());
            health.put("message", healthResponse.getMessage());
            healthResponsesNode.add(health);
        }
        return root;
    }
}
