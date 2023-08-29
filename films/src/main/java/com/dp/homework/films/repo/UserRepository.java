package com.dp.homework.films.repo;

import com.dp.homework.films.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends GenericRepository<User> {

    User findUserByLogin(String login);
    User findUserByEmail(String email);
    User findUserByChangePasswordToken(String uuid);
}
