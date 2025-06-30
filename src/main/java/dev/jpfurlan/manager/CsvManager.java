package dev.jpfurlan.manager;

import com.opencsv.CSVParser;
import com.opencsv.CSVParserBuilder;
import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;
import dev.jpfurlan.entity.Movie;
import io.quarkus.runtime.StartupEvent;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.event.Observes;
import jakarta.transaction.Transactional;

import java.io.InputStreamReader;

@ApplicationScoped
public class CsvManager {

    @Transactional
    void onStart(@Observes StartupEvent ev) throws Exception {
        CSVParser parser = new CSVParserBuilder()
                .withSeparator(';')
                .build();

        try (CSVReader reader = new CSVReaderBuilder(
                new InputStreamReader(getClass().getResourceAsStream("/movielist.csv")))
                .withCSVParser(parser)
                .build()) {

            String[] line;
            reader.readNext();
            while ((line = reader.readNext()) != null) {
                Movie m = new Movie();
                m.releaseYear = Integer.parseInt(line[0].trim());
                m.title       = line[1].trim();
                m.studios     = line[2].trim();
                m.producers   = line[3].trim();
                m.winner      = "yes".equalsIgnoreCase(line[4].trim());
                m.persist();
            }
        }
    }
}