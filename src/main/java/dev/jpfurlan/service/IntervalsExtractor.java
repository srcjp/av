package dev.jpfurlan.service;

import dev.jpfurlan.entity.Movie;
import jakarta.enterprise.context.ApplicationScoped;
import org.jboss.logging.Logger;

import java.util.*;

@ApplicationScoped
public class IntervalsExtractor {

    private static final Logger LOG = Logger.getLogger(IntervalsExtractor.class);

    public List<Map<String, Object>> extract(List<Movie> wins) {
        Map<String, List<Integer>> yearsByProducer = new HashMap<>();
        for (Movie m : wins) {
            String[] parts = m.producers.split("\\s+and\\s+|,");
            for (String raw : parts) {
                String producer = raw.trim();
                yearsByProducer
                        .computeIfAbsent(producer, k -> new ArrayList<>())
                        .add(m.releaseYear);
            }
        }
        LOG.debugf("yearsByProducer: %s", yearsByProducer);

        List<Map<String, Object>> intervals = new ArrayList<>();
        for (var entry : yearsByProducer.entrySet()) {
            String producer = entry.getKey();
            List<Integer> years = entry.getValue();
            if (years.size() < 2) {
                continue;
            }
            Collections.sort(years);
            for (int i = 1; i < years.size(); i++) {
                int previous = years.get(i - 1);
                int following = years.get(i);
                int interval = following - previous;

                Map<String, Object> rec = new HashMap<>();
                rec.put("producer", producer);
                rec.put("previousWin", previous);
                rec.put("followingWin", following);
                rec.put("interval", interval);

                intervals.add(rec);
                LOG.debugf("Intervalo calculado: %s: %d: %d = %d anos", producer, previous, following, interval);
            }
        }
        LOG.debugf("Todos os intervalos calculados: %s", intervals);
        return intervals;
    }
}
