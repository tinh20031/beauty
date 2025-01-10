package com.beautytouch.beautytouch.services;

import com.beautytouch.beautytouch.entity.User;
import com.beautytouch.beautytouch.repositories.UsersRepositories;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    @Autowired
    private UsersRepositories userRepository;
    public User getUserById(Integer id) {
        return userRepository.findById(id).orElse(null);
    }
    public User getUserByName(String userName) {
        return userRepository.findByName(userName);
    }
    public User getUserByEmail(String email){
        return userRepository.findByEmail(email);
    }
}
