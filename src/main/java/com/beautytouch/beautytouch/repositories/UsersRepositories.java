package com.beautytouch.beautytouch.repositories;

import com.beautytouch.beautytouch.entity.Studio;
import com.beautytouch.beautytouch.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UsersRepositories extends JpaRepository<User, Integer> {
    User findByName(String name);

    User findByEmail(String email);

    User getUsersByEmail(String email);
};

