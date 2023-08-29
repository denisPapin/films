package com.dp.homework.films.controllers;

import com.dp.homework.films.dto.DirectorDto;
import com.dp.homework.films.dto.OrderDto;
import com.dp.homework.films.dto.UserDto;
import com.dp.homework.films.model.Role;
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
public class OrderControllerTest extends GenericControllerTest{


    private static final String BASE_URL = "/rest/orders";
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
        List<OrderDto> orderDtoList = objectMapper.readValue(result, new TypeReference<List<OrderDto>>() {
        });
        orderDtoList.forEach(orderDto -> log.info(String.valueOf(orderDto.getRentDate())));
    }

    @Test
    void create() throws Exception {
        OrderDto orderDto = new OrderDto(152L, 1L, LocalDate.now(), 1, true);
        String result = mvc.perform(post(BASE_URL + "/add")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .headers(headers)
                        .content(asJsonString(orderDto))
                        .accept(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().is2xxSuccessful())
                .andReturn()
                .getResponse()
                .getContentAsString();
        OrderDto created = objectMapper.readValue(result, OrderDto.class);
        log.info(String.valueOf(created.getId()));
    }

    @Test
    void update() throws Exception {
        OrderDto existingOrder = objectMapper.readValue(mvc.perform(get(BASE_URL + "/getById")
                                .contentType(MediaType.APPLICATION_JSON_VALUE)
                                .headers(headers)
                                .param("id", String.valueOf(202L))
                                .accept(MediaType.APPLICATION_JSON_VALUE))
                        .andExpect(status().is2xxSuccessful())
                        .andExpect(MockMvcResultMatchers.jsonPath("$.id").exists())
                        .andReturn()
                        .getResponse()
                        .getContentAsString(),
                OrderDto.class);

        existingOrder.setRentPeriod(1);
        mvc.perform(put("/rest/orders/update")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .headers(headers)
                        .param("id", String.valueOf(existingOrder.getId()))
                        .content(asJsonString(existingOrder))
                        .accept(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().is2xxSuccessful());
    }

    @Override
    void deleteObject() {

    }


    @Test
    void softDelete() throws Exception {
        mvc.perform(delete(BASE_URL + "/soft-delete/{id}", 202L)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .headers(headers)
                        .accept(MediaType.APPLICATION_JSON_VALUE))
                .andDo(print())
                .andExpect(status().is2xxSuccessful());

        OrderDto existingOrder = objectMapper.readValue(
                mvc.perform(get(BASE_URL + "/getById")
                                .accept(MediaType.APPLICATION_JSON_VALUE)
                                .headers(headers)
                                .param("id", String.valueOf(202L))
                        )
                        .andExpect(status().is2xxSuccessful())
                        .andReturn()
                        .getResponse()
                        .getContentAsString(), OrderDto.class);
        assertTrue(existingOrder.isDeleted());
    }

    @Test
    void restore() throws Exception {
        mvc.perform(put(BASE_URL + "/restore/{id}", 202L)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .headers(headers)
                        .accept(MediaType.APPLICATION_JSON_VALUE))
                .andDo(print())
                .andExpect(status().is2xxSuccessful());

        OrderDto existingOrder = objectMapper.readValue(
                mvc.perform(get(BASE_URL + "/getById")
                                .accept(MediaType.APPLICATION_JSON_VALUE)
                                .headers(headers)
                                .param("id", String.valueOf(202L))
                        )
                        .andExpect(status().is2xxSuccessful())
                        .andReturn()
                        .getResponse()
                        .getContentAsString(), OrderDto.class);
        assertFalse(existingOrder.isDeleted());
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
    void getUserRentFilmInfo() throws Exception {
        mvc.perform(get(BASE_URL + "/get-order/{userId}", "152")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .headers(headers)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().is2xxSuccessful())
                .andReturn()
                .getResponse()
                .getContentAsString(StandardCharsets.UTF_8);

    }

    @Test
    void createOrder() throws Exception{
        OrderDto orderDto = new OrderDto(352L, 1L, LocalDate.now(), 30, true);
        String result = mvc.perform(post(BASE_URL + "/create-order")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .headers(headers)
                        .content(asJsonString(orderDto))
                        .accept(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().is2xxSuccessful())
                .andReturn()
                .getResponse()
                .getContentAsString();
        OrderDto created = objectMapper.readValue(result, OrderDto.class);
        log.info(String.valueOf(created.getId()));




    }

}
