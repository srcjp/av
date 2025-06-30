package dev.jpfurlan.repository;

import dev.jpfurlan.entity.Movie;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;
import java.util.List;

@ApplicationScoped
public class MovieRepository implements PanacheRepository<Movie> {
    public List<Movie> findWinnersOrdered() {
        return list("winner = true order by releaseYear asc");
    }
}