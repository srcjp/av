package dev.jpfurlan.service;

import dev.jpfurlan.entity.Movie;
import dev.jpfurlan.repository.MovieRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.jboss.logging.Logger;

import java.util.*;

@ApplicationScoped
public class ProducerIntervalService {

    private static final Logger LOG = Logger.getLogger(ProducerIntervalService.class);

    @Inject
    MovieRepository repo;

    public Map<String, List<Map<String, Object>>> calculateIntervals() {
        List<Movie> wins = repo.findWinnersOrdered();
        LOG.debugf("Filmes vencedores: %d - %s", wins.size(), wins);

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

        List<Map<String, Object>> allIntervals = new ArrayList<>();
        for (var entry : yearsByProducer.entrySet()) {
            String producer = entry.getKey();
            List<Integer> years = entry.getValue();
            if (years.size() < 2) {
                continue;
            }
            Collections.sort(years);
            for (int i = 1; i < years.size(); i++) {
                int previousWin = years.get(i - 1);
                int followingWin = years.get(i);
                int interval = followingWin - previousWin;

                Map<String, Object> record = new HashMap<>();
                record.put("producer", producer);
                record.put("previousWin", previousWin);
                record.put("followingWin", followingWin);
                record.put("interval", interval);

                allIntervals.add(record);
                LOG.debugf("Intervalo calculado: %s: %d: %d = %d anos", producer, previousWin, followingWin, interval);
            }
        }
        LOG.debugf("Todos os intervalos calculados: %s", allIntervals);

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
        LOG.debugf("calculateIntervals(): %s", response);
        return response;
    }
}
