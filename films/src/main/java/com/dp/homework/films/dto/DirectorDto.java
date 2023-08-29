package com.dp.homework.films.dto;

import com.dp.homework.films.model.Film;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DirectorDto extends GenericDto {
    @NotBlank(message = "Поле не должно быть пустым")
    private String directorFio;
    private String position;
    private Set<Long> filmsId;
}
