package com.dp.homework.films.mapper;

import com.dp.homework.films.model.GenericModel;
import com.dp.homework.films.dto.DirectorDto;
import com.dp.homework.films.model.Director;
import com.dp.homework.films.repo.FilmRepository;
import jakarta.annotation.PostConstruct;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class DirectorMapper extends GenericMapper<Director, DirectorDto>{

    private final FilmRepository filmRepository;

    protected DirectorMapper(ModelMapper modelMapper, FilmRepository filmRepository) {
        super(modelMapper, Director.class, DirectorDto.class);
        this.filmRepository = filmRepository;
    }

    @PostConstruct
    protected void setupMapper() {
        modelMapper.createTypeMap(Director.class, DirectorDto.class)
                .addMappings(m -> m.skip(DirectorDto::setFilmsId)).setPostConverter(toDtoConverter());
        modelMapper.createTypeMap(DirectorDto.class, Director.class)
                .addMappings(m -> m.skip(Director::setFilms)).setPostConverter(toEntityConverter());
    }

    @Override
    protected void mapSpecificFields(DirectorDto source, Director destination) {
        if(!Objects.isNull(source.getFilmsId())) {
            destination.setFilms(new HashSet<>(filmRepository.findAllById(source.getFilmsId())));
        }
    }

    @Override
    protected void mapSpecificFields(Director source, DirectorDto destination) {
        destination.setFilmsId(getIds(source));
    }


    protected Set<Long> getIds(Director entity) {
        return Objects.isNull(entity) || Objects.isNull(entity.getFilms())
                ? null
                : entity.getFilms()
                .stream()
                .map(GenericModel::getId)
                .collect(Collectors.toSet());
    }
}
