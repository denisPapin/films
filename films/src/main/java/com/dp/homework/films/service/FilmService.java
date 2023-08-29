package com.dp.homework.films.service;

import com.dp.homework.films.dto.AddDirectorDto;
import com.dp.homework.films.dto.FilmSearchDto;
import com.dp.homework.films.dto.FilmWithDirectorsDto;
import com.dp.homework.films.mapper.FilmWithDirectorsMapper;
import com.dp.homework.films.model.Director;
import com.dp.homework.films.model.Film;
import com.dp.homework.films.repo.FilmRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class FilmService extends GenericService<Film>{

    private final FilmRepository repository;
    private final DirectorService directorService;
    private final FilmWithDirectorsMapper filmWithDirectorsMapper;

    protected FilmService(FilmRepository repository, FilmRepository repository1, DirectorService directorService, FilmWithDirectorsMapper filmWithDirectorsMapper) {
        super(repository);
        this.repository = repository1;
        this.directorService = directorService;
        this.filmWithDirectorsMapper = filmWithDirectorsMapper;
    }

    public void addDirector(AddDirectorDto addDirectorDto){
        Film film = getOne(addDirectorDto.getFilmId());
        film.getDirectors().add(directorService.getOne(addDirectorDto.getDirectorId()));
    }

    public Page<FilmWithDirectorsDto> findFilms(FilmSearchDto filmSearchDto,
                                                Pageable pageable) {
        String genre = filmSearchDto.getGenre() != null ? String.valueOf(filmSearchDto.getGenre().ordinal()) : "%";
        Page<Film> filmPage = repository.searchFilms(
                genre,
                filmSearchDto.getTitle(),
                filmSearchDto.getDirectorFio(),
                pageable
        );
        List<FilmWithDirectorsDto> result = filmWithDirectorsMapper.toDtos(filmPage.getContent());
        return new PageImpl<>(result, pageable, filmPage.getTotalElements());
    }

    public Page<Film> searchFilms(final String title, Pageable pageable) {
        return repository.searchAllByTitleContainsIgnoreCaseAndIsDeletedFalse(title, pageable);
    }

    @Override
    public Film update(Film object) {
        Film film = getOne(object.getId());
        film.setTitle(object.getTitle() != null ? object.getTitle() : film.getTitle());
        film.setPremierYear(object.getPremierYear() != null ? object.getPremierYear() : film.getPremierYear());
        film.setCountry(object.getCountry() != null ? object.getCountry() : film.getCountry());
        film.setGenre(object.getGenre() != null ? object.getGenre() : film.getGenre());
        film.setDirectors(object.getDirectors() != null ? object.getDirectors() : film.getDirectors());
        film.setUpdatedBy(SecurityContextHolder.getContext().getAuthentication().getName());
        film.setUpdatedWhen(LocalDateTime.now());
        return super.update(film);
    }
}
