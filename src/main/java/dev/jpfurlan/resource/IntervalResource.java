package dev.jpfurlan.resource;

import dev.jpfurlan.service.ProducerIntervalService;
import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

import java.util.List;
import java.util.Map;

@Path("/producers/intervals")
@Produces(MediaType.APPLICATION_JSON)
public class IntervalResource {

    @Inject
    ProducerIntervalService service;

    @GET
    public Map<String, List<Map<String, Object>>> getIntervals() {
        return service.calculateIntervals();
    }
}
