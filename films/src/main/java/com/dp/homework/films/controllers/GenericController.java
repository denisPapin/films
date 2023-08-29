package com.dp.homework.films.controllers;

import com.dp.homework.films.dto.GenericDto;
import com.dp.homework.films.mapper.GenericMapper;
import com.dp.homework.films.model.GenericModel;
import com.dp.homework.films.service.GenericService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.http.HttpStatus.OK;

@Slf4j
public abstract class GenericController<T extends GenericModel, N extends GenericDto> {

    private final GenericService<T> service;
    private final GenericMapper<T, N> mapper;

    public GenericController(GenericService<T> service, GenericMapper<T, N> mapper) {
        this.service = service;
        this.mapper = mapper;
    }

    @Operation(description = "Получить запись по ID", method = "getById")
    @RequestMapping(value = "/getById", method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<N> getById(@RequestParam(value = "id") Long id) {
        return ResponseEntity.status(OK).body(mapper.toDto(service.getOne(id)));
    }

    @RequestMapping(value = "/getByCreator", method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<T>> getByCreator(@RequestParam(value = "creator") String creator) {
        return ResponseEntity.status(OK)
                .body(service.findByCreatedBy(creator));
    }

    @Operation(description = "Получить все записи", method = "getAll")
    @RequestMapping(value = "/getAll", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<N>> getAll() {
        return ResponseEntity.ok(mapper.toDtos(service.listAll()));
    }

    @Operation(description = "Создать новую запись", method = "create")
    @RequestMapping(value = "/add", method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<N> create(@RequestBody N newEntity) {
        if (newEntity.getId() != null && service.existsById(newEntity.getId())) {
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(mapper.toDto(service.create(mapper.toEntity(newEntity))));
    }

    @Operation(description = "Обновить запись", method = "update")
    @RequestMapping(value = "/update", method = RequestMethod.PUT,
            produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<N> update(@RequestBody N updatedEntity,
                                    @RequestParam(value = "id") Long id) {
        updatedEntity.setId(id);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(mapper.toDto(service.update(mapper.toEntity(updatedEntity))));
    }

    @Operation(description = "Удалить запись по ID", method = "delete")
    @RequestMapping(value = "/delete/{id}", method = RequestMethod.DELETE)
    public void delete(@PathVariable(value = "id") Long id) {
        service.delete(id);
    }

    @Operation(description = "Софт удаление записи по ID", method = "soft delete")
    @RequestMapping(value = "/soft-delete/{id}", method = RequestMethod.DELETE)
    public void softDelete(@PathVariable(value = "id") Long id) {
        log.error("CONTROLLER SOFT");
        service.softDelete(id);
    }

    @Operation(description = "Восстановление записи по ID", method = "restore")
    @RequestMapping(value = "/restore/{id}", method = RequestMethod.PUT)
    public void restore(@PathVariable(value = "id") Long id) {
        service.restore(id);
    }

}