package com.dp.homework.films.service;

import com.dp.homework.films.model.Role;
import com.dp.homework.films.repo.RoleRepository;
import org.springframework.stereotype.Service;

@Service
public class RoleService {

    private final RoleRepository repository;

    public RoleService(RoleRepository repository) {
        this.repository = repository;
    }

    public Role getByTitle(String title) {
        return repository.getRoleByTitle(title);
    }

}
