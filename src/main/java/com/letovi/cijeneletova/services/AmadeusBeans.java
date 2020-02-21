package com.letovi.cijeneletova.services;

import com.amadeus.Amadeus;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AmadeusBeans {

    @Bean
    public Amadeus getAmadeus() {
        //here is my secret :p
        Amadeus amadeus = Amadeus.builder("AlFQAWjfh36vJuGrPfqeNwz7aArIzY2l", "KDRoUNXWAyhv5eNK").build();
        return amadeus;
    }
}
