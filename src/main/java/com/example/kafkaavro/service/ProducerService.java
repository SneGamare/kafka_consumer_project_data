package com.example.kafkaavro.service;

import com.example.kafkaavro.model.PlutusFinacleData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class ProducerService {

    private static final String TOPIC = "plutus-finacle-topic";

    @Autowired
    private KafkaTemplate<String, PlutusFinacleData> kafkaTemplate;

    public void sendMessage(PlutusFinacleData data) {
        System.out.println("Producing message: " + data);
        kafkaTemplate.send(TOPIC, data.getTranId().toString(), data);
    }
} 