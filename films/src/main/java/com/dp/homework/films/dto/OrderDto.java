package com.dp.homework.films.dto;

import com.dp.homework.films.model.Film;
import com.dp.homework.films.model.User;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class OrderDto extends GenericDto {
    private Long userId;
    private Long filmId;
    private LocalDate rentDate;
    private Integer rentPeriod;
    private boolean purchase;
}
