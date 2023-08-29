package com.dp.homework.films.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AddDirectorDto {
    Long filmId;
    Long directorId;
}
