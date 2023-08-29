package com.dp.homework.films.mapper;

import com.dp.homework.films.dto.UserDto;
import com.dp.homework.films.model.User;
import com.dp.homework.films.model.GenericModel;
import com.dp.homework.films.repo.OrderRepository;
import jakarta.annotation.PostConstruct;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class UserMapper extends GenericMapper<User, UserDto> {

    private final OrderRepository OrderRepository;

    protected UserMapper(ModelMapper modelMapper,
                         OrderRepository OrderRepository) {
        super(modelMapper, User.class, UserDto.class);
        this.OrderRepository = OrderRepository;
    }

    @PostConstruct
    protected void setupMapper() {
        modelMapper.createTypeMap(User.class, UserDto.class)
                .addMappings(m -> m.skip(UserDto::setOrdersId)).setPostConverter(toDtoConverter());
        modelMapper.createTypeMap(UserDto.class, User.class)
                .addMappings(m -> m.skip(User::setOrders)).setPostConverter(toEntityConverter());
    }

    @Override
    protected void mapSpecificFields(UserDto source, User destination) {
        if (!Objects.isNull(source.getOrdersId())) {
            destination.setOrders(new HashSet<>(OrderRepository.findAllById(source.getOrdersId())));
        } else {
            destination.setOrders(Collections.emptySet());
        }
    }

    @Override
    protected void mapSpecificFields(User source, UserDto destination) {
        destination.setOrdersId(getIds(source));
    }

    protected Set<Long> getIds(User entity) {
        return Objects.isNull(entity) || Objects.isNull(entity.getOrders())
                ? null
                : entity.getOrders().stream()
                .map(GenericModel::getId)
                .collect(Collectors.toSet());
    }
}