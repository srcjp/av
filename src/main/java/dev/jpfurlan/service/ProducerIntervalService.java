package dev.jpfurlan.service;

import dev.jpfurlan.entity.Movie;
import dev.jpfurlan.repository.MovieRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.util.List;
import java.util.Map;

@ApplicationScoped
public class ProducerIntervalService {

    @Inject
    MovieRepository repo;

    @Inject
    IntervalsExtractor extractor;

    @Inject
    IntervalsSummary summary;

    public Map<String, List<Map<String, Object>>> calculateIntervals() {
        List<Movie> wins = repo.findWinnersOrdered();
        List<Map<String, Object>> intervals = extractor.extract(wins);
        return summary.summarize(intervals);
    }
}
