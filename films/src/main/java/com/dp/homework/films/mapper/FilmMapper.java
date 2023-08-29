package com.dp.homework.films.mapper;


import com.dp.homework.films.dto.FilmDto;
import com.dp.homework.films.model.Film;
import com.dp.homework.films.model.GenericModel;
import com.dp.homework.films.repo.DirectorRepository;
import com.dp.homework.films.repo.FilmRepository;
import jakarta.annotation.PostConstruct;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class FilmMapper extends GenericMapper<Film, FilmDto> {

    private final DirectorRepository directorRepository;
    private final FilmRepository filmRepository;

    protected FilmMapper(ModelMapper modelMapper, DirectorRepository directorRepository, FilmRepository filmRepository) {
        super(modelMapper, Film.class, FilmDto.class);
        this.directorRepository = directorRepository;
        this.filmRepository = filmRepository;
    }

    @PostConstruct
    protected void setupMapper() {
        modelMapper.createTypeMap(Film.class, FilmDto.class)
                .addMappings(m -> m.skip(FilmDto::setDirectorsIds)).setPostConverter(toDtoConverter());
        modelMapper.createTypeMap(FilmDto.class, Film.class)
                .addMappings(m -> m.skip(Film::setDirectors)).setPostConverter(toEntityConverter());
    }

    @Override
    protected void mapSpecificFields(FilmDto source, Film destination) {
        if (!Objects.isNull(source.getDirectorsIds())) {
            destination.setDirectors(new HashSet<>(directorRepository.findAllById(source.getDirectorsIds())));
        }
    }

    @Override
    protected void mapSpecificFields(Film source, FilmDto destination) {
        destination.setDirectorsIds(getIds(source));
    }


    protected Set<Long> getIds(Film entity) {
        return Objects.isNull(entity) || Objects.isNull(entity.getDirectors())
                ? null
                : entity.getDirectors()
                .stream()
                .map(GenericModel::getId)
                .collect(Collectors.toSet());
    }


}
