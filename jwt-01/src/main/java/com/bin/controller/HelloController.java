package com.bin.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
public class HelloController {

    @GetMapping("/test")
    public String hello(String username, HttpServletRequest request){
        request.getSession().setAttribute("username",username);
        return "login ok ~";
    }
}
