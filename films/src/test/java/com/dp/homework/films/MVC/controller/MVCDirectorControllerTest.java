package com.dp.homework.films.MVC.controller;

import com.dp.homework.films.dto.DirectorDto;
import com.dp.homework.films.mapper.DirectorMapper;
import com.dp.homework.films.service.DirectorService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@Slf4j
@Transactional
@Rollback(value = false)
class MVCDirectorControllerTest extends CommonTestMVC {

    private final DirectorDto directorDto = new DirectorDto("MVC_TestDirectorFio", "TestPosition", new HashSet<>());
    private final DirectorDto directorDtoUpdated = new DirectorDto("MVC_TestDirectorFio_Updated", "TestPosition", new HashSet<>());

    @Autowired
    private DirectorService directorService;

    @Autowired
    private DirectorMapper mapper;

    @Test
    void getAll() throws Exception {
        mvc.perform(get("/directors")
                        .param("page", "1")
                        .param("size", "5")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                )
                .andExpect(status().is2xxSuccessful())
                .andExpect(view().name("directors/viewAllDirectors"))
                .andExpect(model().attributeExists("directors"))
                .andReturn();
    }

    @Test
    void create() throws Exception {
        mvc.perform(get("/directors/add")
                        .flashAttr("directorForm", directorDto)
                .contentType(MediaType.APPLICATION_JSON_VALUE)

        )
                .andExpect(status().is2xxSuccessful())
                .andExpect(view().name("directors/addDirector"))
                .andExpect(model().attributeExists("directorForm"))
                .andReturn();
    }

    @Test
    void testCreate() throws Exception {
        mvc.perform(post("/directors/add")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .flashAttr("directorForm", directorDto)
                        .with(csrf()))
                .andDo(print())
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/directors"))
                .andExpect(redirectedUrlTemplate("/directors"));
    }

    @Test
    @WithMockUser(username = "manager", roles = "MANAGER", password = "manager")
    void safeDelete() throws Exception {
        mvc.perform(get("/directors/delete/{id}", "52")

                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                )
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrlTemplate("/directors"))
                .andReturn();
    }

    @Test
    @WithMockUser(username = "manager", roles = "MANAGER", password = "manager")
    void restore() throws Exception {
        mvc.perform(get("/directors/restore/{id}", "52")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                )
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrlTemplate("/directors"))
                .andReturn();
    }

    @Test
    @WithMockUser(username = "admin", roles = "ADMIN", password = "admin")
    void update() throws Exception {
        mvc.perform(get("/directors/update/{id}", "52")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .with(csrf())
                )
                .andExpect(status().is2xxSuccessful())
                .andExpect(view().name("directors/updateDirector"))
                .andExpect(model().attributeExists("director"))
                .andReturn();
    }

    @Test
    @WithMockUser(username = "manager", roles = "MANAGER", password = "manager")
    void testUpdate() throws Exception {
        log.info("Тест по обновлению режиссёра через MVC начат успешно");
        PageRequest pageRequest = PageRequest.of(0, 5, Sort.by(Sort.Direction.ASC, "directorFio"));
        DirectorDto foundDirectorForUpdate = mapper.toDto(directorService.searchDirectors("REST_TestDirectorFio", pageRequest).getContent().get(0));
        foundDirectorForUpdate.setDirectorFio(directorDtoUpdated.getDirectorFio());
        mvc.perform(post("/directors/update")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .flashAttr("directorForm", foundDirectorForUpdate)
                        .with(csrf())
                )
                .andDo(print())
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/directors"))
                .andExpect(redirectedUrl("/directors"));
        log.info("Тест по обновлению режиссёра через MVC закончен успешно");
    }

    @Test
    @WithMockUser(username = "admin", roles = "ADMIN", password = "admin")
    void search() throws Exception {
        DirectorDto directorDto = mapper.toDto(directorService.getOne(52L));
        mvc.perform(post("/directors/search")
                        .param("page", "1")
                        .param("size", "5")
                        .flashAttr("directorSearchForm", directorDto)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .with(csrf()))
                .andDo(print())
                .andExpect(status().is2xxSuccessful())
                .andExpect(model().attributeExists("directors"))
                .andExpect(view().name("directors/viewAllDirectors"));
    }

    @Test
    void getOne() throws Exception {
        mvc.perform(get("/directors/{id}", "52")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .with(csrf())
                )
                .andExpect(status().is2xxSuccessful())
                .andExpect(view().name("directors/viewDirector"))
                .andExpect(model().attributeExists("director"))
                .andReturn();
    }

    @Test
    @WithMockUser(username = "admin", roles = "ADMIN", password = "admin")
    protected void createObject() throws Exception {
        mvc.perform(post("/directors/add")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .flashAttr("directorForm", directorDto)
                        .with(csrf()))
                .andDo(print())
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/directors"))
                .andExpect(redirectedUrlTemplate("/directors"));
    }

    @Test
    @WithMockUser(username = "admin", roles = "ADMIN", password = "admin")
    protected void updateObject() throws Exception {
        log.info("Тест по обновлению автора через MVC начат успешно");
        PageRequest pageRequest = PageRequest.of(0, 5, Sort.by(Sort.Direction.ASC, "directorFio"));
        DirectorDto foundDirectorForUpdate = mapper.toDto(directorService.searchDirectors("REST_TestDirectorFio", pageRequest).getContent().get(0));
        foundDirectorForUpdate.setDirectorFio(directorDtoUpdated.getDirectorFio());
        mvc.perform(post("/directors/update")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .flashAttr("directorForm", foundDirectorForUpdate)
                        .with(csrf())
                )
                .andDo(print())
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/directors"))
                .andExpect(redirectedUrl("/directors"));
        log.info("Тест по обновлению автора через MVC закончен успешно");
    }



    @Test
    @DisplayName("Получение всех директоров")
    protected void listAll() throws Exception {
        mvc.perform(get("/directors")
                        .param("page", "1")
                        .param("size", "5")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                )
                .andExpect(status().is2xxSuccessful())
                .andExpect(view().name("directors/viewAllDirectors"))
                .andExpect(model().attributeExists("directors"))
                .andReturn();
    }
}