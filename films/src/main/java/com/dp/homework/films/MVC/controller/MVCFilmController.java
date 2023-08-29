package com.dp.homework.films.MVC.controller;

import com.dp.homework.films.dto.*;
import com.dp.homework.films.mapper.FilmMapper;
import com.dp.homework.films.mapper.FilmWithDirectorsMapper;
import com.dp.homework.films.model.Film;
import com.dp.homework.films.service.DirectorService;
import com.dp.homework.films.service.FilmService;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.repository.query.Param;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Controller
@RequestMapping("/films")
public class MVCFilmController {

    private final FilmService service;
    private final FilmMapper mapper;
    private final FilmWithDirectorsMapper filmWithDirectorsMapper;
    private final DirectorService directorService;

    public MVCFilmController(FilmService service, FilmMapper mapper, FilmWithDirectorsMapper filmWithDirectorsMapper, DirectorService directorService) {
        this.service = service;
        this.mapper = mapper;
        this.filmWithDirectorsMapper = filmWithDirectorsMapper;
        this.directorService = directorService;
    }

    @GetMapping("")
    public String getAll(
            @RequestParam(value = "page", defaultValue = "1") int page,
            @RequestParam(value = "size", defaultValue = "5") int pageSize,
            Model model
    ) {
        PageRequest pageRequest = PageRequest.of(page - 1, pageSize, Sort.by(Sort.Direction.ASC, "title"));
        Page<Film> FilmPage = service.listAll(pageRequest);
        Page<FilmWithDirectorsDto> directorDtoPage = new PageImpl<>(filmWithDirectorsMapper.toDtos(FilmPage.getContent()), pageRequest, FilmPage.getTotalElements());
        model.addAttribute("films", directorDtoPage);
        return "films/viewAllFilms";
    }


    @GetMapping("/add")
    public String addFilm() {
        return "films/addFilm";
    }

    @PostMapping("/add")
    public String addFilm(@ModelAttribute("filmForm") FilmDto filmDto) {
        service.create(mapper.toEntity(filmDto));
        return "redirect:/films";
    }

    @GetMapping("/add-director/{filmId}")
    public String addDirector(@PathVariable Long filmId,
                          Model model) {
        model.addAttribute("directors", directorService.listAll());
        model.addAttribute("filmId", filmId);
        model.addAttribute("film", service.getOne(filmId).getTitle());
        return "films/addFilmDirector";
    }

    @PostMapping("/add-director")
    public String addDirector(@ModelAttribute("filmDirectorForm") AddDirectorDto addDirectorDto) {
        service.addDirector(addDirectorDto);
        return "redirect:/films";
    }

    @GetMapping("/update/{id}")
    public String update(@PathVariable Long id, Model model) {
        model.addAttribute("film", mapper.toDto(service.getOne(id)));
        return "films/updateFilm";
    }

    @PostMapping("/update")
    public String update(@ModelAttribute("filmForm") FilmDto filmDto) {
        service.update(mapper.toEntity(filmDto));
        return "redirect:/films";
    }

    @PostMapping("/search")
    public String searchFilms(@RequestParam(value = "page", defaultValue = "1") int page,
                              @RequestParam(value = "size", defaultValue = "5") int size,
                              @ModelAttribute("filmSearchForm") FilmSearchDto filmSearchDto,
                              Model model) {
        PageRequest pageRequest = PageRequest.of(page - 1, size, Sort.by(Sort.Direction.ASC, "title"));
        model.addAttribute("films", service.searchFilms(filmSearchDto.getTitle(), pageRequest));
        return "films/viewAllFilms";
    }

    @GetMapping("/{filmId}")
    public String viewOneFilm(@PathVariable Long filmId, Model model) {
        model.addAttribute("film",filmWithDirectorsMapper.toDto(service.getOne(filmId)));
        return "/films/viewFilm";
    }

    @GetMapping("/delete/{filmId}")
    public String delete(@PathVariable Long filmId) {
        service.softDelete(filmId);
        return "redirect:/films";
    }

    @GetMapping("/restore/{id}")
    public String restore(@PathVariable Long id) {
        service.restore(id);
        return "redirect:/films";
    }

    @PostMapping("/search/director")
    public String searchDirector(
            @RequestParam(value = "page", defaultValue = "1") int page,
            @RequestParam(value = "size", defaultValue = "5") int size,
            @ModelAttribute("directorSearchForm") DirectorDto directorDto,
            Model model
    ) {
        FilmSearchDto filmSearchDTO = new FilmSearchDto();
        filmSearchDTO.setDirectorFio(directorDto.getDirectorFio());
        return searchFilms(page, size, filmSearchDTO, model);
    }

//    @GetMapping(value = "/download", produces = MediaType.MULTIPART_FORM_DATA_VALUE)
//    @ResponseBody
//    public ResponseEntity<Resource> downloadBook(@Param(value = "filmId") Long filmId) throws IOException {
//        Film film = service.getOne(filmId);
//        Path path = Paths.get(film.getOnlineCopyPath());
//        ByteArrayResource resource = new ByteArrayResource(Files.readAllBytes(path));
//        return ResponseEntity.ok().headers(headers(path.getFileName().toString()))
//                .contentLength(path.toFile().length())
//                .contentType(MediaType.parseMediaType("application/octet-stream"))
//                .body(resource);
//    }

//    private HttpHeaders headers(String name) {
//        HttpHeaders headers = new HttpHeaders();
//        headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + name);
//        headers.add("Cache-Control", "no-cache, no-store, must-revalidate");
//        headers.add("Pragma", "no-cache");
//        headers.add("Expires", "0");
//        return headers;
//    }
}

