package com.itelg.docker.cawandu;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class Application
{
    public static final void main(String[] args) throws Exception
    {
        SpringApplication.run(Application.class, args);
    }
}