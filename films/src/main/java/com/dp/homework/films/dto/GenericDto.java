package com.dp.homework.films.dto;

import jakarta.persistence.Column;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public abstract class GenericDto {

    protected Long id;
    protected LocalDateTime createdWhen = LocalDateTime.now();
    protected String createdBy = "DEFAULT_USER";
    protected boolean isDeleted = false;
    protected String deletedBy;
    protected LocalDateTime deletedWhen;
    protected LocalDateTime updatedWhen;
    protected String updatedBy;
}
