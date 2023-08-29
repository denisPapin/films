package com.dp.homework.films.controllers;

import com.dp.homework.films.config.jwt.JWTTokenUtil;
import com.dp.homework.films.service.userdetails.CustomUserDetailsService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.test.web.servlet.MockMvc;

import static org.junit.jupiter.api.Assertions.*;
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@TestInstance(Lifecycle.PER_CLASS)
public abstract class GenericControllerTest {

    @Autowired
    protected MockMvc mvc;

    @Autowired
    protected JWTTokenUtil jwtTokenUtil;

    @Autowired
    protected CustomUserDetailsService userDetailsService;

    protected String token = "";

    @Autowired
    protected ObjectMapper objectMapper;

    protected HttpHeaders headers = new HttpHeaders();

    protected String generateToken(String username) {
        return jwtTokenUtil.generateToken(userDetailsService.loadUserByUsername(username));
    }

    @BeforeAll
    public void prepare() {
        token = generateToken("manager");
        headers.add("Authorization", "Bearer " + token);
    }

    abstract void getById();

    abstract void getByCreator();

    abstract  void getAll() throws Exception;

    abstract void create() throws Exception;

    abstract void update() throws Exception;

    abstract void deleteObject();

    abstract void softDelete() throws Exception;

    abstract void restore() throws Exception;


}