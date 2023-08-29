package com.dp.homework.films.mapper;

import com.dp.homework.films.dto.FilmWithDirectorsDto;
import com.dp.homework.films.model.Director;
import com.dp.homework.films.model.Film;
import com.dp.homework.films.repo.DirectorRepository;
import com.dp.homework.films.repo.FilmRepository;
import jakarta.annotation.PostConstruct;
import org.modelmapper.ModelMapper;
import com.dp.homework.films.model.GenericModel;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class FilmWithDirectorsMapper extends GenericMapper<Film, FilmWithDirectorsDto> {

    private final ModelMapper mapper;
    private final DirectorRepository repository;

    protected FilmWithDirectorsMapper(ModelMapper modelMapper, ModelMapper mapper, DirectorRepository repository) {
        super(modelMapper, Film.class, FilmWithDirectorsDto.class);
        this.mapper = mapper;
        this.repository = repository;
    }

    @PostConstruct
    public void setupMapper() {
        mapper.createTypeMap(Film.class, FilmWithDirectorsDto.class)
                .addMappings(m -> m.skip(FilmWithDirectorsDto::setDirectorsIds)).setPostConverter(toDtoConverter());
        mapper.createTypeMap(FilmWithDirectorsDto.class, Film.class)
                .addMappings(m -> m.skip(Film::setDirectors)).setPostConverter(toEntityConverter());
    }

    @Override
    protected void mapSpecificFields(FilmWithDirectorsDto source, Film destination) {
        destination.setDirectors(new HashSet<>(repository.findAllById(source.getDirectorsIds())));
    }

    @Override
    protected void mapSpecificFields(Film source, FilmWithDirectorsDto destination) {
        destination.setDirectorsIds(getIds(source));
    }

    protected Set<Long> getIds(Film Film) {
        return Objects.isNull(Film) || Objects.isNull(Film.getId())
                ? null
                : Film.getDirectors().stream()
                .map(GenericModel::getId)
                .collect(Collectors.toSet());
    }
}
