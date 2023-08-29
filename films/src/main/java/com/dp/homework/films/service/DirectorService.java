package com.dp.homework.films.service;

import com.dp.homework.films.annotation.MySecuredAnnotation;
import com.dp.homework.films.dto.AddFilmDto;
import com.dp.homework.films.model.Director;
import com.dp.homework.films.repo.DirectorRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class DirectorService extends GenericService<Director> {

    private final DirectorRepository repository;
//    private final FilmService filmService;


    protected DirectorService(DirectorRepository repository) {
        super(repository);
        this.repository = repository;
//        this.filmService = filmService;
    }

    public Page<Director> searchDirectors(final String fio, Pageable pageable) {
        return repository.findAllByDirectorFioContainsIgnoreCaseAndIsDeletedFalse(fio, pageable);
    }

    @Override
    @MySecuredAnnotation("ROLE_MANAGER")
    public void softDelete(Long id) {
        Director director = getOne(id);
        boolean directorCanBeDeleted = repository.checkDirectorForDeletion(id);
        if (directorCanBeDeleted) {
            director.setDeletedBy("ADMIN"); //TODO переделать с секурити
            director.setDeleted(true);
            director.setDeletedWhen(LocalDateTime.now());
            update(director);
        }
    }

//    public void addFilm(AddFilmDto addFilmDto) {
//        Director director = getOne(addFilmDto.getDirectorId());
//        director.getFilms().add(filmService.getOne(addFilmDto.getFilmId()));
//        update(director);
//    }
}
