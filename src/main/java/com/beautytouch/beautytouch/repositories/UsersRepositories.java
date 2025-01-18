package com.beautytouch.beautytouch.repositories;

import com.beautytouch.beautytouch.entity.Studio;
import com.beautytouch.beautytouch.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UsersRepositories extends JpaRepository<User, Integer> {
    User findByName(String name);

    Optional<User> findByEmail(String email);

    Optional<User> getUsersByEmail(String email);

    User findUserByEmail(String email);
    boolean existsByEmail(String email);

};

