package dev.jpfurlan.service;

import jakarta.enterprise.context.ApplicationScoped;
import org.jboss.logging.Logger;

import java.util.*;

@ApplicationScoped
public class IntervalsSummary {

    private static final Logger LOG = Logger.getLogger(IntervalsSummary.class);

    public Map<String, List<Map<String, Object>>> summarize(List<Map<String, Object>> allIntervals) {
        long minInterval = allIntervals.stream()
                .mapToLong(e -> ((Number) e.get("interval")).longValue())
                .min()
                .orElse(0L);
        long maxInterval = allIntervals.stream()
                .mapToLong(e -> ((Number) e.get("interval")).longValue())
                .max()
                .orElse(0L);
        LOG.debugf("minInterval = %d | maxInterval = %d", minInterval, maxInterval);

        List<Map<String, Object>> minList = new ArrayList<>();
        List<Map<String, Object>> maxList = new ArrayList<>();
        for (Map<String, Object> rec : allIntervals) {
            long iv = ((Number) rec.get("interval")).longValue();
            if (iv == minInterval) {
                minList.add(rec);
                LOG.debugf("min: %s", rec);
            }
            if (iv == maxInterval) {
                maxList.add(rec);
                LOG.debugf("max: %s", rec);
            }
        }

        Map<String, List<Map<String, Object>>> response = Map.of(
                "min", minList,
                "max", maxList
        );
        LOG.debugf("summarize(): %s", response);
        return response;
    }
}
