package com.dp.homework.films.model;

import jakarta.persistence.*;
import org.springframework.stereotype.Component;

import java.util.Set;
@Entity
@Table(name = "directors")
public class Director extends GenericModel{
    @Id
    @Column(name = "id", nullable = false)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "default_gen")
    private Long id;
    @Column(name = "fio", nullable = false)
    private String directorFio;
    @Column(name = "number_pos", nullable = false)
    private String position;
    @ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
    @JoinTable(
            name = "directors_films",
            joinColumns = @JoinColumn(name = "director_id"), foreignKey = @ForeignKey(name = "FK_directors_filmS"),
            inverseJoinColumns = @JoinColumn(name = "film_id"), inverseForeignKey = @ForeignKey(name = "FK_filmS_directors"))

    private Set<Film> films;

    public Director() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDirectorFio() {
        return directorFio;
    }

    public void setdirectorFio(String directorFio) {
        this.directorFio = directorFio;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public Set<Film> getFilms() {
        return films;
    }

    public void setFilms(Set<Film> films) {
        this.films = films;
    }
}
