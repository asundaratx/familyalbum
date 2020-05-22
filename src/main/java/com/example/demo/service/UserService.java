package com.example.demo.service;

import com.example.demo.datamysql.dao.User;
import com.example.demo.datamysql.dao.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.security.InvalidParameterException;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;

    public User findByEmail(String email){
        if(email == null) {
            throw new InvalidParameterException("Invalid paramter to look up user by email");
        }
        return userRepository.findByEmail(email);
    }

    public Iterable<User> findAllUsers(){
        return userRepository.findAll();
    }
}
