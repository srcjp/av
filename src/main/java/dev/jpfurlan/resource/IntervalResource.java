package dev.jpfurlan.resource;

import dev.jpfurlan.entity.Movie;
import dev.jpfurlan.repository.MovieRepository;
import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

import java.util.*;

@Path("/producers/intervals")
@Produces(MediaType.APPLICATION_JSON)
public class IntervalResource {

    @Inject
    MovieRepository repo;

    @GET
    public Map<String, List<Map<String, Object>>> getIntervals() {

        List<Movie> wins = repo.findWinnersOrdered();
        System.out.println("Filmes vencedores:"
                + wins.size() + " - " + wins);

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
        System.out.println("yearsByProducer: " + yearsByProducer);

        List<Map<String, Object>> allIntervals = new ArrayList<>();
        System.out.println("allIntervals para registros de intervalo");

        for (var entry : yearsByProducer.entrySet()) {
            String producer = entry.getKey();
            List<Integer> years = entry.getValue();

            if (years.size() < 2) {
                continue;
            }

            Collections.sort(years);

            for (int i = 1; i < years.size(); i++) {
                int previousWin  = years.get(i - 1);
                int followingWin = years.get(i);
                int interval     = followingWin - previousWin;

                Map<String, Object> record = new HashMap<>();
                record.put("producer",     producer);
                record.put("previousWin",  previousWin);
                record.put("followingWin", followingWin);
                record.put("interval",     interval);

                allIntervals.add(record);
                System.out.println(" Intervalo calculado: "
                        + producer + ": " + previousWin + ": " + followingWin
                        + " = " + interval + " anos");
            }
        }
        System.out.println("Todos os intervalos calculados: " + allIntervals);

        long minInterval = allIntervals.stream()
                .mapToLong(e -> ((Number) e.get("interval")).longValue())
                .min()
                .orElse(0L);
        long maxInterval = allIntervals.stream()
                .mapToLong(e -> ((Number) e.get("interval")).longValue())
                .max()
                .orElse(0L);
        System.out.println("minInterval = " + minInterval
                + " | maxInterval = " + maxInterval);

        List<Map<String, Object>> minList = new ArrayList<>();
        List<Map<String, Object>> maxList = new ArrayList<>();
        System.out.println("Separando registros em minList e maxList");

        for (Map<String, Object> rec : allIntervals) {
            long iv = ((Number) rec.get("interval")).longValue();
            if (iv == minInterval) {
                minList.add(rec);
                System.out.println("min:" + rec);
            }
            if (iv == maxInterval) {
                maxList.add(rec);
                System.out.println("max:" + rec);
            }
        }

        Map<String, List<Map<String, Object>>> response = Map.of(
                "min", minList,
                "max", maxList
        );
        System.out.println("getIntervals(): " + response + "\n");

        return response;
    }
}
