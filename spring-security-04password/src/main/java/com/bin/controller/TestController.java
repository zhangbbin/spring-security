package com.bin.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {

    @GetMapping("/test")
    public String Test(){
        System.out.println("test ok!");
        return "test ok!";
    }

}
