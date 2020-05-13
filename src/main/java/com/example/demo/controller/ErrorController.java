package com.example.demo.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;

@Controller
@Slf4j
public class ErrorController {
    @GetMapping("/error")
    public @ResponseBody
    String error(HttpServletRequest request) {
        String message = (String) request.getSession().getAttribute("error.message");
        if(message!=null)
            message.concat(" To obtain authorization or check errors, use <a href=\"mailto:sundar.aparna@gmail.com?subject=AuthorizationRequest\">Contact admin </a>");
        request.getSession().removeAttribute("error.message");
        return message;
    }
}

