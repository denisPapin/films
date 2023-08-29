package com.dp.homework.films.model;

import jakarta.persistence.*;

import java.util.Set;

@Entity
@Table(name = "films")
public class Film extends GenericModel {
    @Id
    @Column(name = "id", nullable = false)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "default_gen")
    private Long id;
    @Column(name = "film_title", nullable = false)
    private String title;
    @Column(name = "date_of_year", nullable = false)
    private String premierYear;
    @Column(name = "region")
    private String country;
    @Column(name = "genre", nullable = false)
    @Enumerated
    private Genre genre;
    @OneToMany(mappedBy = "film", fetch = FetchType.EAGER)
    private Set<Order> orders;

    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.REMOVE)
    @JoinTable(name = "directors_films",
    joinColumns = @JoinColumn(name = "film_id"), foreignKey = @ForeignKey(name = "FK_FILMS_DIRECTORS"),
    inverseJoinColumns = @JoinColumn(name = "director_id"), inverseForeignKey = @ForeignKey(name = "FK_DIRECTORS_filmS"))
    private Set<Director> directors;

    public Film() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPremierYear() {
        return premierYear;
    }

    public void setPremierYear(String premierYear) {
        this.premierYear = premierYear;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public Genre getGenre() {
        return genre;
    }

    public void setGenre(Genre genre) {
        this.genre = genre;
    }

    public Set<Order> getOrders() {
        return orders;
    }

    public void setOrders(Set<Order> orders) {
        this.orders = orders;
    }

    public Set<Director> getDirectors() {
        return directors;
    }

    public void setDirectors(Set<Director> directors) {
        this.directors = directors;
    }
}
