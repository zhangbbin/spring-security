package com.bin;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class JWT01Application {
    public static void main(String[] args) {
        SpringApplication.run(JWT01Application.class,args);
        System.out.println("启动成功！");
    }
}
