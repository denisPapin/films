package com.dp.homework.films.controllers;

import com.dp.homework.films.dto.FilmDto;
import com.dp.homework.films.mapper.FilmMapper;
import com.dp.homework.films.model.Director;
import com.dp.homework.films.model.Film;
import com.dp.homework.films.service.DirectorService;
import com.dp.homework.films.service.FilmService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.webjars.NotFoundException;
@RestController
@RequestMapping("/rest/films")
@Tag(name = "Фильмы", description = "Контроллер для работы с фильмами")
@SecurityRequirement(name = "Bearer Authentication")
public class FilmController extends GenericController<Film, FilmDto> {

    private final DirectorService directorService;
    private final FilmService service;
    private final FilmMapper mapper;

    public FilmController(DirectorService directorService, FilmService service, FilmMapper mapper){
        super(service, mapper);
        this.directorService = directorService;
        this.mapper = mapper;
        this.service = service;
    }

    @Operation(description = "Добавить создателя к фильму", method = "addDirector")
    @RequestMapping(value = "/addDirector", method = RequestMethod.PUT, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<FilmDto> addDirector(@RequestParam(value = "directorId") Long directorId,
                                          @RequestParam(value = "filmId") Long filmId) {
        try {
            Film film = service.getOne(filmId);
            Director director = directorService.getOne(directorId);
            film.getDirectors().add(director);
            service.update(film);
            return ResponseEntity.status(HttpStatus.OK).body(mapper.toDto(film));
        } catch (NotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

}
