package com.dp.homework.films.MVC.controller;

import com.dp.homework.films.dto.AddDirectorDto;
import com.dp.homework.films.dto.DirectorDto;
import com.dp.homework.films.dto.FilmDto;
import com.dp.homework.films.dto.FilmSearchDto;
import com.dp.homework.films.mapper.DirectorMapper;
import com.dp.homework.films.mapper.FilmMapper;
import com.dp.homework.films.model.Genre;
import com.dp.homework.films.service.DirectorService;
import com.dp.homework.films.service.FilmService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;

import java.util.HashSet;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
@Slf4j
class MVCFilmControllerTest extends CommonTestMVC {

    private final FilmDto filmDto = new FilmDto("MVC_TEST", "TestYear", "Russia", Genre.FANTASY, new HashSet<>());
    private final FilmDto FilmDtoUpdated = new FilmDto("MVC_TEST_Updated", "TestYear", "Russia", Genre.FANTASY, new HashSet<>());


    @Autowired
    private FilmService filmService;

    @Autowired
    private FilmMapper mapper;

    @Test
    void getAll() throws Exception {
        mvc.perform(get("/films")
                        .param("page", "1")
                        .param("size", "5")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                )
                .andExpect(status().is2xxSuccessful())
                .andExpect(view().name("films/viewAllFilms"))
                .andExpect(model().attributeExists("films"))
                .andReturn();
    }

    @Test
    void addFilm() throws Exception {
        mvc.perform(get("/films/add")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)

                )
                .andExpect(status().is2xxSuccessful())
                .andExpect(view().name("films/addFilm"))
                .andReturn();
    }

    @Test
    void testAddFilm() throws Exception {
        mvc.perform(post("/films/add")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .flashAttr("filmForm", filmDto)
                        .with(csrf()))
                .andDo(print())
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/films"))
                .andExpect(redirectedUrlTemplate("/films"));
    }

    @Test
    @WithMockUser(username = "admin", roles = "ADMIN", password = "admin")
    void addDirector() throws Exception{
        mvc.perform(get("/films/add-director/{filmId}", "1")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)

                )
                .andExpect(status().is2xxSuccessful())
                .andExpect(model().attributeExists("directors"))
                .andExpect(model().attributeExists("film"))
                .andExpect(model().attributeExists("filmId"))
                .andExpect(view().name("films/addFilmDirector"))
                .andReturn();
    }

    @Test
    void testAddDirector() throws Exception {
        AddDirectorDto addDirectorDto = new AddDirectorDto(1L,52L);
        mvc.perform(post("/films/add-director")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .flashAttr("filmDirectorForm", addDirectorDto)
                        .with(csrf()))
                .andDo(print())
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/films"))
                .andExpect(redirectedUrlTemplate("/films"));
    }

    @Test
    @WithMockUser(username = "admin", roles = "ADMIN", password = "admin")
    void update() throws Exception {
        mvc.perform(get("/films/update/{id}", "1")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .with(csrf())
                )
                .andExpect(status().is2xxSuccessful())
                .andExpect(view().name("films/updateFilm"))
                .andExpect(model().attributeExists("film"))
                .andReturn();
    }

    @Test
    @WithMockUser(username = "admin", roles = "ADMIN", password = "admin")
    void testUpdate() throws Exception {

        log.info("Тест по обновлению режиссёра через MVC начат успешно");
        FilmDto filmDto1 = mapper.toDto(filmService.getOne(1L));
        filmDto1.setTitle("newUpdated");
        mvc.perform(post("/films/update")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .flashAttr("filmForm", filmDto1)
                        .with(csrf())
                )
                .andDo(print())
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/films"))
                .andExpect(redirectedUrl("/films"));
        log.info("Тест по обновлению режиссёра через MVC закончен успешно");
    }

    @Test
    @WithMockUser(username = "admin", roles = "ADMIN", password = "admin")
    void searchFilms() throws Exception{
    FilmSearchDto filmSearchDto = new FilmSearchDto("newUpdated", "UPDATED", Genre.FANTASY);
        mvc.perform(post("/films/search")
                        .param("page", "1")
                        .param("size", "5")
                        .flashAttr("filmSearchForm", filmSearchDto)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .with(csrf()))
                .andDo(print())
                .andExpect(status().is2xxSuccessful())
                .andExpect(model().attributeExists("films"))
                .andExpect(view().name("films/viewAllFilms"));

    }

    @Test
    void viewOneFilm() throws Exception {
        mvc.perform(get("/films/{id}", "1")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .with(csrf())
                )
                .andExpect(status().is2xxSuccessful())
                .andExpect(view().name("/films/viewFilm"))
                .andExpect(model().attributeExists("film"))
                .andReturn();
    }

    @Test
    @WithMockUser(username = "admin", roles = "ADMIN", password = "admin")
    void delete() throws Exception {
        mvc.perform(get("/films/delete/{filmId}", "1")

                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                )
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrlTemplate("/films"))
                .andReturn();
    }

    @Test
    @WithMockUser(username = "admin", roles = "ADMIN", password = "admin")
    void restore() throws Exception {
        mvc.perform(get("/films/restore/{id}", "1")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                )
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrlTemplate("/films"))
                .andReturn();
    }

    @Test
    @WithMockUser(username = "admin", roles = "ADMIN", password = "admin")
    void searchDirector() throws Exception {
        DirectorDto directorDto = new DirectorDto("MVC_TestDirectorFio", "TestPosition", new HashSet<>());
        mvc.perform(post("/films/search/director")
                        .param("page", "1")
                        .param("size", "5")
                        .flashAttr("directorSearchForm",directorDto)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .with(csrf()))
                .andDo(print())
                .andExpect(status().is2xxSuccessful());
    }
}