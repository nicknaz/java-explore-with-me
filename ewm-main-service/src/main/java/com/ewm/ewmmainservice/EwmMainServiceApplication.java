package com.ewm.ewmmainservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

@SpringBootApplication(scanBasePackages = {"ru.practicum", "com.ewm.ewmmainservice"})
public class EwmMainServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(EwmMainServiceApplication.class, args);
    }

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

}
