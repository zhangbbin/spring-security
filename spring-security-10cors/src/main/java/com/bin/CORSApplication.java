package com.bin;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class CORSApplication {
    public static void main(String[] args) {
        SpringApplication.run(CORSApplication.class,args);
        System.out.println("启动成功！");
    }
}
