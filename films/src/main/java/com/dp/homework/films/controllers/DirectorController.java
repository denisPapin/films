package com.dp.homework.films.controllers;

import com.dp.homework.films.dto.AddFilmDto;
import com.dp.homework.films.dto.DirectorDto;
import com.dp.homework.films.mapper.DirectorMapper;
import com.dp.homework.films.model.Director;
import com.dp.homework.films.model.Film;
import com.dp.homework.films.repo.FilmRepository;
import com.dp.homework.films.service.DirectorService;
import com.dp.homework.films.service.FilmService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.webjars.NotFoundException;




@RestController
@RequestMapping("/rest/directors")
@Tag(name = "Режиссеры и тд.", description = "Контроллер для работы с создателями фильмов")
@SecurityRequirement(name = "Bearer Authentication")
public class DirectorController extends GenericController<Director, DirectorDto>{

    private final DirectorService service;
    private final DirectorMapper mapper;
    private final FilmRepository filmRepository;

    public DirectorController(DirectorService service, DirectorMapper mapper, FilmRepository filmRepository) {
        super(service, mapper);
        this.service = service;
        this.mapper = mapper;
        this.filmRepository = filmRepository;
    }

    @Operation(description = "Добавить фильм к создателю", method = "addFilm")
    @RequestMapping(value = "/addFilm", method = RequestMethod.PUT, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<DirectorDto> addFilm(@RequestBody AddFilmDto addFilmDto) {
        try {
            Film film = filmRepository.findById(addFilmDto.getFilmId()).orElseThrow(() -> new NotFoundException("Фильм с переданным ID не найден"));
            Director director = service.getOne(addFilmDto.getDirectorId());
            director.getFilms().add(film);
            return ResponseEntity.status(HttpStatus.OK).body(mapper.toDto(service.update(director)));
        } catch (NotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

}
