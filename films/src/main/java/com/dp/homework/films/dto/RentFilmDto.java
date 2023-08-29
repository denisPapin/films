package com.dp.homework.films.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class RentFilmDto {
    Long filmId;
    Long userId;
    Integer rentPeriod;
}
