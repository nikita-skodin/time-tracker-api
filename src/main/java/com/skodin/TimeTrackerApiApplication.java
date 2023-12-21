package com.skodin;

import org.modelmapper.ModelMapper;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.beans.BeanProperty;

@SpringBootApplication
public class TimeTrackerApiApplication {

    public static void main(String[] args) {
        SpringApplication.run(TimeTrackerApiApplication.class, args);
    }

}
