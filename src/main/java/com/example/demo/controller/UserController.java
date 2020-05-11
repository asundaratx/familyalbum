package com.example.demo.controller;

import com.example.demo.datamysql.dao.User;
import com.example.demo.datamysql.dao.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping(path="/user")
@Slf4j
public class UserController {
    @Autowired
    private UserRepository userRepository;

    @GetMapping(path="/all")
    public @ResponseBody
    Iterable<User> getAllUsers() {
        log.info("Getting all user");
        return userRepository.findAll();
    }
}
