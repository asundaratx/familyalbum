package com.example.demo.service;

import com.example.demo.datamysql.dao.User;
import com.example.demo.datamysql.dao.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

import static org.mockito.Mockito.when;

public class UserServiceTest {
    private UserRepository userRepository = Mockito.mock(UserRepository.class);
    private UserService userService;

    @BeforeEach
    void setup(){
        userService = new UserService(userRepository);
    }

    @Test
    void findByEmailReturnsUser(){
        String email = "sundar.aparna@gmail.com";
        when(userRepository.findByEmail(email)).thenReturn(user());
        User user = userService.findByEmail(email);
        assertThat(user).isNotNull();
        assertThat(user.getEmail()).isEqualTo(email);
    }

    @Test
    void findAllUsersReturnsUser(){
        List<User> userList = new ArrayList<>();
        userList.add(user());
        when(userRepository.findAll()).thenReturn(userList);
        Iterable<User> users = userService.findAllUsers();
        assertThat(users).isNotNull();
    }

    private User user(){
        User user = new User();
        user.setEmail("sundar.aparna@gmail.com");
        user.setId(1);
        return user;
    }
}
