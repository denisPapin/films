package com.dp.homework.films.MVC.controller;

import com.dp.homework.films.dto.RentFilmDto;
import com.dp.homework.films.mapper.FilmMapper;
import com.dp.homework.films.service.FilmService;
import com.dp.homework.films.service.OrderService;
import com.dp.homework.films.service.UserService;
import com.dp.homework.films.service.userdetails.CustomUserDetails;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/rent")
public class MVCOrderController {

    private final FilmService filmService;
    private final FilmMapper filmMapper;
    private final OrderService service;
    private final UserService userService;

    public MVCOrderController(FilmService filmService, FilmMapper filmMapper, OrderService service, UserService userService) {
        this.filmService = filmService;
        this.filmMapper = filmMapper;
        this.service = service;
        this.userService = userService;
    }

    @GetMapping("/get-film/{filmId}")
    public String getFilm(@PathVariable Long filmId, Model model) {
        model.addAttribute("film", filmMapper.toDto(filmService.getOne(filmId)));
        return "userFilms/getFilm";
    }

    @PostMapping("/get-film")
    public String getFilm(@ModelAttribute("publishForm") RentFilmDto rentFilmDto) {
        //CustomUserDetails customUserDetails = (CustomUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String name = authentication.getName();
        rentFilmDto.setUserId(userService.getUserByLogin(name).getId());
        service.rentFilm(rentFilmDto);
        return "redirect:/films";
    }

    @GetMapping("/user-films")
    public String getUserFilms(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String name = authentication.getName();
        model.addAttribute("rentFilms", service.getUserFilmRentInfo(userService.getUserByLogin(name).getId()));
        return "userFilms/viewAllUserFilms";
    }

    @GetMapping("/return-film/{id}")
    public String returnFilm(@PathVariable Long id) {
        service.returnFilm(id);
        return "redirect:/rent/user-films";
    }

}
