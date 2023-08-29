package com.dp.homework.films.service;

import com.dp.homework.films.dto.RentFilmDto;
import com.dp.homework.films.model.Film;
import com.dp.homework.films.model.Order;
import com.dp.homework.films.model.User;
import com.dp.homework.films.repo.GenericRepository;
import com.dp.homework.films.repo.OrderRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class OrderService extends GenericService<Order>{
    private final UserService userService;
    private final FilmService filmService;
    private final OrderRepository repository;

    protected OrderService(OrderRepository repository, UserService userService, FilmService filmService) {
        super(repository);
        this.userService = userService;
        this.filmService = filmService;
        this.repository = repository;
    }

    public List<Order> getUserFilmRentInfo(Long id) {
        return userService.getOne(id).getOrders().stream().toList();
    }
    @Override
    public Order create(Order object){
        return super.create(object);
    }

    public Order rentFilm(RentFilmDto rentFilmDto) {
        User user = userService.getOne(rentFilmDto.getUserId());
        Film film = filmService.getOne(rentFilmDto.getFilmId());
        filmService.update(film);
        Order order = Order.builder()
                .rentDate(LocalDate.now())
                .purchase(true)
                .rentDate(LocalDate.now().plusDays(rentFilmDto.getRentPeriod()))
                .rentPeriod(rentFilmDto.getRentPeriod())
                .user(user)
                .film(film)
                .build();
        return repository.save(order);

    }

    public void returnFilm(Long id) {
        Order order = getOne(id);
        order.setPurchase(false);
        Film film = order.getFilm();
        update(order);
        filmService.update(film);
    }

}
