package com.estudos.products.infra.kafka.listener;

import org.springframework.stereotype.Service;

import lombok.extern.log4j.Log4j2;

import org.springframework.kafka.annotation.KafkaListener;

@Service
@Log4j2
public class ProductKafkaListener {

    @KafkaListener(topics = "abc")
    public void listen(String message){
        log.info(message);
    }
}
