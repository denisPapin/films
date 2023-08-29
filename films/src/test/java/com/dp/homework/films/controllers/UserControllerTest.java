package com.dp.homework.films.controllers;

import com.dp.homework.films.dto.LoginDto;
import com.dp.homework.films.dto.UserDto;
import com.dp.homework.films.model.Role;
import com.dp.homework.films.service.userdetails.CustomUserDetailsService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.core.userdetails.UserDetails;
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
public class UserControllerTest extends GenericControllerTest{

    CustomUserDetailsService customUserDetailsService;

    private static final String BASE_URL = "/rest/users";
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
        List<UserDto> userDtoList = objectMapper.readValue(result, new TypeReference<List<UserDto>>() {
        });
        userDtoList.forEach(userDto -> log.info(userDto.getLogin()));
    }

    @Test
    void create() throws Exception {
        UserDto userDto = new UserDto("test", "test", "test", "test", "test", LocalDate.now(), "tet", "tet", "tet", new Role(), new HashSet<>());
        String result = mvc.perform(post(BASE_URL + "/add")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .headers(headers)
                        .content(asJsonString(userDto))
                        .accept(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().is2xxSuccessful())
                .andReturn()
                .getResponse()
                .getContentAsString();
        UserDto created = objectMapper.readValue(result, UserDto.class);
        log.info(String.valueOf(created.getId()));
    }

    @Test
    void update() throws Exception {
        UserDto existingUser = objectMapper.readValue(mvc.perform(get(BASE_URL + "/getById")
                                .contentType(MediaType.APPLICATION_JSON_VALUE)
                                .headers(headers)
                                .param("id", String.valueOf(452L))
                                .accept(MediaType.APPLICATION_JSON_VALUE))
                        .andExpect(status().is2xxSuccessful())
                        .andExpect(MockMvcResultMatchers.jsonPath("$.id").exists())
                        .andReturn()
                        .getResponse()
                        .getContentAsString(),
                UserDto.class);

        existingUser.setLogin("UPDATED");
        mvc.perform(put("/rest/users/update")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .headers(headers)
                        .param("id", String.valueOf(existingUser.getId()))
                        .content(asJsonString(existingUser))
                        .accept(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().is2xxSuccessful());
    }

    @Override
    void deleteObject() {

    }


    @Test
    void softDelete() throws Exception {
        mvc.perform(delete(BASE_URL + "/soft-delete/{id}", 452L)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .headers(headers)
                        .accept(MediaType.APPLICATION_JSON_VALUE))
                .andDo(print())
                .andExpect(status().is2xxSuccessful());

        UserDto existingUser = objectMapper.readValue(
                mvc.perform(get(BASE_URL + "/getById")
                                .accept(MediaType.APPLICATION_JSON_VALUE)
                                .headers(headers)
                                .param("id", String.valueOf(452L))
                        )
                        .andExpect(status().is2xxSuccessful())
                        .andReturn()
                        .getResponse()
                        .getContentAsString(), UserDto.class);
        assertTrue(existingUser.isDeleted());
    }

    @Test
    void restore() throws Exception {
        mvc.perform(put(BASE_URL + "/restore/{id}", 452L)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .headers(headers)
                        .accept(MediaType.APPLICATION_JSON_VALUE))
                .andDo(print())
                .andExpect(status().is2xxSuccessful());

        UserDto existingAuthor = objectMapper.readValue(
                mvc.perform(get(BASE_URL + "/getById")
                                .accept(MediaType.APPLICATION_JSON_VALUE)
                                .headers(headers)
                                .param("id", String.valueOf(452L))
                        )
                        .andExpect(status().is2xxSuccessful())
                        .andReturn()
                        .getResponse()
                        .getContentAsString(), UserDto.class);
        assertFalse(existingAuthor.isDeleted());
    }

    protected String asJsonString(final Object obj) {
        try {
            return objectMapper.writeValueAsString(obj);
        } catch (JsonProcessingException e) {
            log.error(e.getMessage());
            return null;
        }
    }
    @Test
    void auth() throws Exception{

        UserDto existingUser = objectMapper.readValue(
                mvc.perform(get(BASE_URL + "/getById")
                                .accept(MediaType.APPLICATION_JSON_VALUE)
                                .headers(headers)
                                .param("id", String.valueOf(352L))
                        )
                        .andExpect(status().is2xxSuccessful())
                        .andReturn()
                        .getResponse()
                        .getContentAsString(), UserDto.class);
        LoginDto loginDto = new LoginDto(existingUser.getLogin(), "manager");

        mvc.perform(post(BASE_URL + "/auth")
                        .content(asJsonString(loginDto))
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .headers(headers)
                        .accept(MediaType.APPLICATION_JSON_VALUE))
                .andDo(print())
                .andExpect(status().is2xxSuccessful());



    }
}
