package com.hikvision.hikvisionmanage;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

@SpringBootApplication
@ServletComponentScan
public class HikvisionmanageApplication extends SpringBootServletInitializer {

    public static void main(String[] args) {
        SpringApplication.run(HikvisionmanageApplication.class, args);
    }

    protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
        return builder.sources(HikvisionmanageApplication.class);
    }
}
