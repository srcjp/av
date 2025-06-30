package dev.jpfurlan.entity;

import io.quarkus.hibernate.orm.panache.PanacheEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

@Entity
@Table(name = "movie")
public class Movie extends PanacheEntity {

    @Column(name = "release_year")
    public Integer releaseYear;

    public String title;
    public String studios;
    public String producers;
    public Boolean winner;
}