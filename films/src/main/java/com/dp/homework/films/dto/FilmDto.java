package com.dp.homework.films.dto;

import com.dp.homework.films.model.Director;
import com.dp.homework.films.model.Genre;
import com.dp.homework.films.model.Order;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class FilmDto extends GenericDto {
    private String title;
    private String premierYear;
    private String country;
    private Genre genre;
    private Set<Long> directorsIds;
}
