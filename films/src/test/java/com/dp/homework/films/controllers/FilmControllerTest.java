package com.dp.homework.films.controllers;

import com.dp.homework.films.dto.*;
import com.dp.homework.films.dto.FilmDto;
import com.dp.homework.films.model.Genre;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.nio.charset.StandardCharsets;
import java.util.HashSet;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
@Slf4j
public class FilmControllerTest extends GenericControllerTest{

    private static final String BASE_URL = "/rest/films";
    @Override
    void getById() {

    }

    @Override
    void getByCreator() {

    }

    @Test
    void getAll() throws Exception {
        String result = super.mvc.perform(get(BASE_URL + "/getAll")
                        .headers(headers)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().is2xxSuccessful())
                .andReturn()
                .getResponse()
                .getContentAsString(StandardCharsets.UTF_8);

        log.info(result);
        List<FilmDto> filmDtoList = objectMapper.readValue(result, new TypeReference<List<FilmDto>>() {
        });
        filmDtoList.forEach(filmDto -> log.info(filmDto.getTitle()));
    }

    @Test
    void create() throws Exception {
        FilmDto filmDto = new FilmDto("REST_TestTitle", "Test", "Test", Genre.FANTASY, new HashSet<>());
        String result = mvc.perform(post(BASE_URL + "/add")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .headers(headers)
                        .content(asJsonString(filmDto))
                        .accept(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().is2xxSuccessful())
                .andReturn()
                .getResponse()
                .getContentAsString();
        FilmDto created = objectMapper.readValue(result, FilmDto.class);
        log.info(String.valueOf(created.getId()));
    }

    @Test
    void update() throws Exception {
        FilmDto existingFilm = objectMapper.readValue(mvc.perform(get(BASE_URL + "/getById")
                                .contentType(MediaType.APPLICATION_JSON_VALUE)
                                .headers(headers)
                                .param("id", String.valueOf(1L))
                                .accept(MediaType.APPLICATION_JSON_VALUE))
                        .andExpect(status().is2xxSuccessful())
                        .andExpect(MockMvcResultMatchers.jsonPath("$.id").exists())
                        .andReturn()
                        .getResponse()
                        .getContentAsString(),
                FilmDto.class);

        existingFilm.setTitle("UPDATED");
        mvc.perform(put("/rest/films/update")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .headers(headers)
                        .param("id", String.valueOf(existingFilm.getId()))
                        .content(asJsonString(existingFilm))
                        .accept(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().is2xxSuccessful());
    }

    @Override
    void deleteObject() {

    }

    @Test
    void softDelete() throws Exception {
        mvc.perform(delete(BASE_URL + "/soft-delete/{id}", 1L)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .headers(headers)
                        .accept(MediaType.APPLICATION_JSON_VALUE))
                .andDo(print())
                .andExpect(status().is2xxSuccessful());

        FilmDto existingFilm = objectMapper.readValue(
                mvc.perform(get(BASE_URL + "/getById")
                                .accept(MediaType.APPLICATION_JSON_VALUE)
                                .headers(headers)
                                .param("id", String.valueOf(1L))
                        )
                        .andExpect(status().is2xxSuccessful())
                        .andReturn()
                        .getResponse()
                        .getContentAsString(), FilmDto.class);
        assertTrue(existingFilm.isDeleted());
    }

    @Test
    void restore() throws Exception {
        mvc.perform(put(BASE_URL + "/restore/{id}", 1L)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .headers(headers)
                        .accept(MediaType.APPLICATION_JSON_VALUE))
                .andDo(print())
                .andExpect(status().is2xxSuccessful());

        FilmDto existingAuthor = objectMapper.readValue(
                mvc.perform(get(BASE_URL + "/getById")
                                .accept(MediaType.APPLICATION_JSON_VALUE)
                                .headers(headers)
                                .param("id", String.valueOf(1L))
                        )
                        .andExpect(status().is2xxSuccessful())
                        .andReturn()
                        .getResponse()
                        .getContentAsString(), FilmDto.class);
        assertFalse(existingAuthor.isDeleted());
    }
    @Test
    void addDirector() throws Exception{
        AddDirectorDto addDirectorDto = new AddDirectorDto(1L, 52L);
        String result = mvc.perform(put(BASE_URL + "/addDirector")
                        .accept(MediaType.APPLICATION_JSON_VALUE)
                        .headers(headers)
                        .param("directorId", String.valueOf(52L))
                        .param("filmId", String.valueOf(1L)))
                .andExpect(status().is2xxSuccessful())
                .andReturn()
                .getResponse()
                .getContentAsString();
        FilmDto filmDto = objectMapper.readValue(result, FilmDto.class);
        assertTrue(filmDto.getDirectorsIds().contains(52L));
    }

    protected String asJsonString(final Object obj) {
        try {
            return objectMapper.writeValueAsString(obj);
        } catch (JsonProcessingException e) {
            log.error(e.getMessage());
            return null;
        }
    }
}
