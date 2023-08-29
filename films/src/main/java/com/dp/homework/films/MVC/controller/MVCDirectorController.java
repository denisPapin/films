package com.dp.homework.films.MVC.controller;

import com.dp.homework.films.dto.AddFilmDto;
import com.dp.homework.films.dto.DirectorDto;
import com.dp.homework.films.mapper.DirectorMapper;
import com.dp.homework.films.model.Director;
import com.dp.homework.films.service.DirectorService;
import com.dp.homework.films.service.FilmService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/directors")
@Slf4j
public class MVCDirectorController {

    private final DirectorService service;
    private final DirectorMapper mapper;
    private final FilmService filmService;

    public MVCDirectorController(DirectorService service, DirectorMapper mapper, FilmService filmService) {
        this.service = service;
        this.mapper = mapper;
        this.filmService = filmService;
    }


    @GetMapping("")
    public String getAll(
            @RequestParam(value = "page", defaultValue = "1") int page,
            @RequestParam(value = "size", defaultValue = "5") int pageSize,
            Model model
    ) {
        PageRequest pageRequest = PageRequest.of(page - 1, pageSize, Sort.by(Sort.Direction.ASC, "directorFio"));
        Page<Director> directorPage = service.listAll(pageRequest);
        Page<DirectorDto> directorDtoPage = new PageImpl<>(mapper.toDtos(directorPage.getContent()), pageRequest, directorPage.getTotalElements());
        model.addAttribute("directors", directorDtoPage);
        return "directors/viewAllDirectors";
    }

    @GetMapping("/add")
    public String create(@ModelAttribute("directorForm") DirectorDto directorDto) {
        return "directors/addDirector";
    }

    @PostMapping("/add")
    public String create(
            @ModelAttribute("directorForm") @Valid DirectorDto directorDto,
            BindingResult result
    ) {
        if(result.hasErrors()) {
            return "/directors/addDirector";
        } else {
            service.create(mapper.toEntity(directorDto));
            return "redirect:/directors";
        }
    }

    @GetMapping("/delete/{id}")
    public String safeDelete(@PathVariable Long id) {
        service.softDelete(id);
        return "redirect:/directors";
    }

    @GetMapping("/restore/{id}")
    public String restore(@PathVariable Long id) {
        service.restore(id);
        return "redirect:/directors";
    }

   @GetMapping("/update/{id}")
   public String update(Model model, @PathVariable Long id) {
       model.addAttribute("director", service.getOne(id));
       return "directors/updateDirector";
   }

    @PostMapping("/update")
    public String update(@ModelAttribute("directorForm") DirectorDto directorDto) {
        service.update(mapper.toEntity(directorDto));
        return "redirect:/directors";
    }

    @PostMapping("/search")
    public String search(
            @RequestParam(value = "page", defaultValue = "1") int page,
            @RequestParam(value = "size", defaultValue = "5") int pageSize,
            @ModelAttribute("directorSearchForm") DirectorDto directorDto,
            Model model
    ) {
        if (!StringUtils.hasText(directorDto.getDirectorFio()) || !StringUtils.hasLength(directorDto.getDirectorFio())) {
            return "redirect:/directors";
        } else {
            PageRequest pageRequest = PageRequest.of(page - 1, pageSize, Sort.by(Sort.Direction.ASC, "directorFio"));
            Page<Director> directorPage = service.searchDirectors(directorDto.getDirectorFio().trim(), pageRequest);
            Page<DirectorDto> directorDtoPage = new PageImpl<>(mapper.toDtos(directorPage.getContent()), pageRequest, directorPage.getTotalElements());
            model.addAttribute("directors", directorDtoPage);
            return "directors/viewAllDirectors";
        }
    }

    @GetMapping("/{id}")
    public String getOne(@PathVariable Long id, Model model) {
        model.addAttribute("director", mapper.toDto(service.getOne(id)));
        return "directors/viewDirector";
    }

//    @GetMapping("/add-film/{directorId}")
//    public String addFilm(@PathVariable Long directorId,
//                          Model model) {
//        model.addAttribute("films", filmService.listAll());
//        model.addAttribute("directorId", directorId);
//        model.addAttribute("director", service.getOne(directorId).getDirectorFio());
//        return "directors/addDirectorFilm";
//    }
//
//    @PostMapping("/add-film")
//    public String addFilm(@ModelAttribute("directorFilmForm") AddFilmDto addFilmDto) {
//        service.addFilm(addFilmDto);
//        return "redirect:/directors";
//    }




}



