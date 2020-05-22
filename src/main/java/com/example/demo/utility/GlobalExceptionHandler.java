package com.example.demo.utility;

import lombok.extern.slf4j.Slf4j;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.servlet.http.HttpServletRequest;
import java.security.InvalidParameterException;

@ControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler({InvalidParameterException.class})
    public String handleInvalidParameterException(HttpServletRequest httpServletRequest, Exception exception, Model model){
        model.addAttribute("message", exception.getMessage());
        return "error.html";
    }
}
