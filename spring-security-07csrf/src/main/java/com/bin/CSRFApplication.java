package com.bin;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class CSRFApplication {
    public static void main(String[] args) {
        SpringApplication.run(CSRFApplication.class,args);
        System.out.println("启动成功！");
    }
}
