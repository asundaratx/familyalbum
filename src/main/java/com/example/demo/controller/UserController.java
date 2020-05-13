package com.example.demo.controller;

import com.example.demo.datamysql.dao.User;
import com.example.demo.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.Map;

@Controller
@Slf4j
public class UserController {
    @Autowired
    private UserService userService;

    @GetMapping(path="/user/all")
    public @ResponseBody
    Iterable<User> getAllUsers() {
        return userService.findAllUsers();
    }

    @GetMapping("/user")
    public @ResponseBody
    Map<String, Object> user(@AuthenticationPrincipal OAuth2User principal) {
        Map<String,Object> dataMap = new HashMap<>();
        String email = principal.getAttribute("email");
        dataMap.put("name", principal.getAttribute("name"));
        dataMap.put("email", principal.getAttribute("email"));
        if(email!=null) {
            User user = userService.findByEmail(email);
            if(user!=null) {
                dataMap.put("authorized", Boolean.TRUE);
            } else {
                dataMap.put("unauthorized", Boolean.TRUE);
            }
        }
        return dataMap;
    }
}
