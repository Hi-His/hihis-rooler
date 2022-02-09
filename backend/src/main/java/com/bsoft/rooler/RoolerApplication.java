package com.bsoft.rooler;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
//@ImportResource(locations = "classpath:spring-service.xml")
public class RoolerApplication {

    public static void main(String[] args) {


        SpringApplication.run(RoolerApplication.class, args);
    }

}
