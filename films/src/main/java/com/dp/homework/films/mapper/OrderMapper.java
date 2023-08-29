package com.dp.homework.films.mapper;

import com.dp.homework.films.dto.OrderDto;
import com.dp.homework.films.model.Order;
import com.dp.homework.films.repo.FilmRepository;
import com.dp.homework.films.repo.UserRepository;
import jakarta.annotation.PostConstruct;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;
import org.webjars.NotFoundException;

import java.util.Set;
@Component
public class OrderMapper extends GenericMapper<Order, OrderDto> {

    private final UserRepository userRepository;
    private final FilmRepository filmRepository;

    public OrderMapper(ModelMapper modelMapper, FilmRepository filmRepository, UserRepository userRepository, UserRepository userRepository1, FilmRepository filmRepository1) {
        super(modelMapper, Order.class, OrderDto.class);
        this.userRepository = userRepository1;
        this.filmRepository = filmRepository1;
    }

    @PostConstruct
    public void setupMapper() {
        super.modelMapper.createTypeMap(Order.class, OrderDto.class)
                .addMappings(m -> m.skip(OrderDto::setUserId)).setPostConverter(toDtoConverter())
                .addMappings(m -> m.skip(OrderDto::setFilmId)).setPostConverter(toDtoConverter());

        super.modelMapper.createTypeMap(OrderDto.class, Order.class)
                .addMappings(m -> m.skip(Order::setUser)).setPostConverter(toEntityConverter())
                .addMappings(m -> m.skip(Order::setFilm)).setPostConverter(toEntityConverter());
    }

    @Override
    protected void mapSpecificFields(OrderDto source, Order destination) {
        destination.setFilm(filmRepository.findById(source.getFilmId()).orElseThrow(() -> new NotFoundException("Книги не найдено")));
        destination.setUser(userRepository.findById(source.getUserId()).orElseThrow(() -> new NotFoundException("Пользователя не найдено")));
    }

    @Override
    protected void mapSpecificFields(Order source, OrderDto destination) {
        destination.setUserId(source.getUser().getId());
        destination.setFilmId(source.getFilm().getId());
    }

}
