package com.dp.homework.films.MVC.controller;

import com.dp.homework.films.dto.DirectorDto;
import com.dp.homework.films.dto.UserDto;
import com.dp.homework.films.mapper.UserMapper;
import com.dp.homework.films.model.Role;
import com.dp.homework.films.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;

import java.time.LocalDate;
import java.util.HashSet;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class MVCUserControllerTest extends CommonTestMVC {

    @Autowired
    UserService service;

    @Autowired
    UserMapper mapper;

    @Test
    @WithMockUser(username = "admin", roles = "ADMIN", password = "admin")
    void registration() throws Exception {
        mvc.perform(get("/users/registration")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)

                )
                .andExpect(status().is2xxSuccessful())
                .andExpect(view().name("registration"))
                .andReturn();
    }

    @Test
    @WithMockUser(username = "admin", roles = "ADMIN", password = "admin")
    void addManager() throws Exception {
        mvc.perform(get("/users/add-manager")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)

                )
                .andExpect(status().is2xxSuccessful())
                .andExpect(view().name("users/addManager"))
                .andReturn();
    }

    @Test
    @WithMockUser(username = "admin", roles = "ADMIN", password = "admin")
    void testAddManager() throws Exception {
        UserDto userDto = new UserDto("denis", "Denis", "Denis", "Papin", "Alex", LocalDate.now(), "222", "123", "dddd", new Role(), new HashSet<>());
        mvc.perform(post("/users/add-manager")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .flashAttr("userForm", userDto)
                        .with(csrf())
                )
                .andDo(print())
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/users"))
                .andExpect(redirectedUrl("/users"));
    }

    @Test
    @WithMockUser(username = "admin", roles = "ADMIN", password = "admin")
    void testRegistration() throws Exception {
        UserDto userDto = new UserDto("denis", "Denis", "Denis", "Papin", "Alex", LocalDate.now(), "222", "123", "dddd", new Role(), new HashSet<>());
        mvc.perform(post("/users/registration")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .flashAttr("userForm", userDto)
                        .with(csrf())
                )
                .andDo(print())
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/login"))
                .andExpect(redirectedUrl("/login"));
    }

    @Test
    @WithMockUser(username = "asd", roles = "ADMIN", password = "asd")
    void getAll() throws Exception{
        mvc.perform(get("/users")
                        .param("page", "1")
                        .param("size", "5")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                )
                .andExpect(status().is2xxSuccessful())
                .andExpect(view().name("users/viewAllUsers"))
                .andExpect(model().attributeExists("users"))
                .andReturn();
    }

    @Test
    @WithMockUser(username = "manager", roles = "MANAGER", password = "manager")
    void viewProfile() throws Exception {
        mvc.perform(get("/users/profile/{id}", "352")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                )
                .andExpect(status().is2xxSuccessful())
                .andExpect(view().name("profile/viewProfile"))
                .andExpect(model().attributeExists("user"))
                .andReturn();
    }

    @Test
    @WithMockUser(username = "admin", roles = "ADMIN", password = "admin")
    void updateProfile() throws Exception {
        mvc.perform(get("/users/profile/update/{id}", 452)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                )
                .andExpect(status().is2xxSuccessful())
                .andExpect(view().name("profile/updateProfile"))
                .andExpect(model().attributeExists("user"))
                .andReturn();

    }

    @Test
    @WithMockUser(username = "admin", roles = "ADMIN", password = "admin")
    void testUpdateProfile() throws Exception {

        UserDto userDto = mapper.toDto(service.getOne(452L));
        userDto.setFirstName("newName");
        mvc.perform(post("/users/profile/update")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .flashAttr("userForm", userDto)
                        .with(csrf())
                )
                .andDo(print())
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/users/profile/" + userDto.getId()));


    }

    @Test
    @WithMockUser(username = "admin", roles = "ADMIN", password = "admin")
    void rememberPassword() throws Exception {
        mvc.perform(get("/users/remember-password")

                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                )
                .andExpect(status().is2xxSuccessful())
                .andExpect(view().name("users/rememberPassword"))
                .andReturn();

    }

    @Test
    @WithMockUser(username = "admin", roles = "ADMIN", password = "admin")
    void testRememberPassword() throws Exception {
        UserDto userDto = mapper.toDto(service.getOne(452L));
        userDto.setPassword("password");
        mvc.perform(post("/users/remember-password")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .flashAttr("changePasswordForm", userDto)
                        .with(csrf())
                )
                .andDo(print())
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/login"));
    }

    @Test
    @WithMockUser(username = "admin", roles = "ADMIN", password = "admin")
    void changePassword() throws Exception {
        mvc.perform(get("/users/change-password")
                        .param("uuid", "111")

                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                )
                .andExpect(status().is2xxSuccessful())
                .andExpect(model().attributeExists("uuid"))
                .andExpect(view().name("users/changePassword"))
                .andReturn();

    }

    @Test
    void testChangePassword() throws Exception {
        UserDto userDto = mapper.toDto(service.getOne(452L));
        userDto.setPassword("password");
        mvc.perform(post("/users/change-password")
                        .param("uuid", "111")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .flashAttr("changePasswordForm", userDto)
                        .with(csrf())
                )
                .andDo(print())
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/login"));
    }
}