package com.dp.homework.films.controllers;

import com.dp.homework.films.dto.OrderDto;
import com.dp.homework.films.mapper.OrderMapper;
import com.dp.homework.films.model.Order;
import com.dp.homework.films.repo.GenericRepository;
import com.dp.homework.films.repo.OrderRepository;
import com.dp.homework.films.service.OrderService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/rest/orders")
@Tag(name = "Просмотр фильма",
        description = "Контроллер для работы с предоставлением фильмов пользователям фильмотеки")
@SecurityRequirement(name = "Bearer Authentication")
public class OrderController extends GenericController<Order, OrderDto>{

    private final OrderService service;
    private final OrderMapper mapper;

    public OrderController(OrderService service, OrderMapper mapper) {
        super(service, mapper);
        this.service = service;
        this.mapper = mapper;
    }

    @Operation(description = "Просмотреть список арендованных пользователем фильмов", method = "getUserOrder")
    @RequestMapping(value = "/get-order/{userId}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<OrderDto>> getUserRentFilmInfo(@PathVariable Long userId) {
        return ResponseEntity.ok().body(mapper.toDtos(service.getUserFilmRentInfo(userId)));
    }

    @Operation(description = "Взять фильм в аренду", method = "createOrder")
    @PostMapping(value = "/create-order")
    public ResponseEntity<OrderDto> createOrder(@RequestBody OrderDto order){
        if (order.getId() != null && service.existsById(order.getId())) {
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(mapper.toDto(service.create(mapper.toEntity(order))));
    }
}
