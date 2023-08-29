package com.dp.homework.films.MVC.controller;

import com.dp.homework.films.dto.RentFilmDto;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class MVCOrderControllerTest extends CommonTestMVC {

    @Test
    @WithMockUser(username = "admin", roles = "ADMIN", password = "admin")
    void getFilm() throws Exception {
        mvc.perform(get("/rent/get-film/{filmId}", "1")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .with(csrf())
                )
                .andExpect(status().is2xxSuccessful())
                .andExpect(view().name("userFilms/getFilm"))
                .andExpect(model().attributeExists("film"))
                .andReturn();
    }

    @Test
    @WithMockUser(username = "asd", roles = "USER", password = "asd")
    void testGetFilm() throws Exception {
        RentFilmDto rentFilmDto = new RentFilmDto(1L, 152L, 1);
        mvc.perform(post("/rent/get-film")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .flashAttr("publishForm", rentFilmDto)
                        .with(csrf())
                )
                .andDo(print())
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/films"))
                .andExpect(redirectedUrl("/films"));
    }

    @Test
    @WithMockUser(username = "UPDATED", roles = "ADMIN", password = "admin")
    void getUserFilms() throws Exception{
        mvc.perform(get("/rent/user-films")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .with(csrf())
                )
                .andExpect(status().is2xxSuccessful())
                .andExpect(view().name("userFilms/viewAllUserFilms"))
                .andExpect(model().attributeExists("rentFilms"))
                .andReturn();
    }

    @Test
    @WithMockUser(username = "asd", roles = "USER", password = "asd")
    void returnFilm() throws Exception {
        mvc.perform(get("/rent/return-film/{id}", "1052")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .with(csrf())
                )
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/rent/user-films"))
                .andExpect(redirectedUrl("/rent/user-films"));
    }
}