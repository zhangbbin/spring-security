package com.bin.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloController {

    @GetMapping("/index")
    public String index(){
        System.out.println("index ok");
        return "index ok";
    }

    @PostMapping("/withdraw")
    public String withdraw(){
        System.out.println("执行一次转账操作");
        return "执行一次转账操作";
    }
}
