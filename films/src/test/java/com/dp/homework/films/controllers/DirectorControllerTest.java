package com.dp.homework.films.controllers;

import com.dp.homework.films.dto.AddFilmDto;
import com.dp.homework.films.dto.DirectorDto;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
@Slf4j
public class DirectorControllerTest extends  GenericControllerTest {

    private static final String BASE_URL = "/rest/directors";

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
        List<DirectorDto> directorDtoList = objectMapper.readValue(result, new TypeReference<List<DirectorDto>>() {
        });
        directorDtoList.forEach(directorDto -> log.info(directorDto.getDirectorFio()));
    }

    @Test
    void create() throws Exception {
        DirectorDto directorDto = new DirectorDto("REST_TestDirectorFio", "Test Position", new HashSet<>());
        String result = mvc.perform(post(BASE_URL + "/add")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .headers(headers)
                        .content(asJsonString(directorDto))
                        .accept(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().is2xxSuccessful())
                .andReturn()
                .getResponse()
                .getContentAsString();
        DirectorDto created = objectMapper.readValue(result, DirectorDto.class);
        log.info(String.valueOf(created.getId()));
    }

    @Test
    void update() throws Exception {
        DirectorDto existingDirector = objectMapper.readValue(mvc.perform(get(BASE_URL + "/getById")
                                .contentType(MediaType.APPLICATION_JSON_VALUE)
                                .headers(headers)
                                .param("id", String.valueOf(52L))
                                .accept(MediaType.APPLICATION_JSON_VALUE))
                        .andExpect(status().is2xxSuccessful())
                        .andExpect(MockMvcResultMatchers.jsonPath("$.id").exists())
                        .andReturn()
                        .getResponse()
                        .getContentAsString(),
                DirectorDto.class);

        existingDirector.setDirectorFio("UPDATED");
        mvc.perform(put("/rest/directors/update")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .headers(headers)
                        .param("id", String.valueOf(existingDirector.getId()))
                        .content(asJsonString(existingDirector))
                        .accept(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().is2xxSuccessful());
    }

    @Override
    void deleteObject() {

    }


    @Test
    void softDelete() throws Exception {
        mvc.perform(delete(BASE_URL + "/soft-delete/{id}", 52L)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .headers(headers)
                        .accept(MediaType.APPLICATION_JSON_VALUE))
                .andDo(print())
                .andExpect(status().is2xxSuccessful());

        DirectorDto existingDirector = objectMapper.readValue(
                mvc.perform(get(BASE_URL + "/getById")
                                .accept(MediaType.APPLICATION_JSON_VALUE)
                                .headers(headers)
                                .param("id", String.valueOf(52L))
                        )
                        .andExpect(status().is2xxSuccessful())
                        .andReturn()
                        .getResponse()
                        .getContentAsString(), DirectorDto.class);
        assertTrue(existingDirector.isDeleted());
    }

    @Test
    void restore() throws Exception {
        mvc.perform(put(BASE_URL + "/restore/{id}", 52L)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .headers(headers)
                        .accept(MediaType.APPLICATION_JSON_VALUE))
                .andDo(print())
                .andExpect(status().is2xxSuccessful());

        DirectorDto existingAuthor = objectMapper.readValue(
                mvc.perform(get(BASE_URL + "/getById")
                                .accept(MediaType.APPLICATION_JSON_VALUE)
                                .headers(headers)
                                .param("id", String.valueOf(52L))
                        )
                        .andExpect(status().is2xxSuccessful())
                        .andReturn()
                        .getResponse()
                        .getContentAsString(), DirectorDto.class);
        assertFalse(existingAuthor.isDeleted());
    }

    @Test
    void addFilm() throws Exception{
        AddFilmDto addFilmDto = new AddFilmDto(1L, 52L);
        String result = mvc.perform(put(BASE_URL + "/addFilm")
                        .accept(MediaType.APPLICATION_JSON_VALUE)
                        .headers(headers)
                        .content(asJsonString(addFilmDto))
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().is2xxSuccessful())
                .andReturn()
                .getResponse()
                .getContentAsString();
        DirectorDto directorDto = objectMapper.readValue(result, DirectorDto.class);
        assertTrue(directorDto.getFilmsId().contains(1L));
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
