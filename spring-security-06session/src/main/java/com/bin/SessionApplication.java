package com.bin;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class SessionApplication {
    public static void main(String[] args) {
        SpringApplication.run(SessionApplication.class,args);
        System.out.println("启动成功！");
    }
}
