package com.dp.homework.films.dto;

import com.dp.homework.films.model.Genre;
import lombok.*;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class FilmSearchDto {
    private String title;
    private String directorFio;
    private Genre genre;
}
