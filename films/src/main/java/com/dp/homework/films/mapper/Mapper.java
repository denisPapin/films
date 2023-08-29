package com.dp.homework.films.mapper;

import com.dp.homework.films.dto.GenericDto;
import com.dp.homework.films.model.GenericModel;

import java.util.List;

public interface Mapper<E extends GenericModel, D extends GenericDto>{
    E toEntity(D dto);

    List<E> toEntities(List<D> dtos);

    D toDto(E entity);

    List<D> toDtos(List<E> entities);
}
