package com.example.kafkaavro.service;

import com.example.kafkaavro.model.MessageEntity;
import com.example.kafkaavro.model.PlutusFinacleData;
import com.example.kafkaavro.repository.MessageRepository;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicBoolean;

@Service
public class ConsumerService {
    private static final Logger logger = LoggerFactory.getLogger(ConsumerService.class);
    private final List<PlutusFinacleData> receivedMessages = new ArrayList<>();
    private final ConcurrentHashMap<String, Boolean> processedMessages = new ConcurrentHashMap<>();
    private final AtomicBoolean isProcessing = new AtomicBoolean(false);
    
    @Autowired
    private MessageRepository messageRepository;

    @KafkaListener(topics = "plutus-finacle-topic", groupId = "avro-group", containerFactory = "kafkaListenerContainerFactory")
    public void consume(ConsumerRecord<String, PlutusFinacleData> record, Acknowledgment ack) {
        if (isProcessing.get()) {
            logger.debug("Already processing a message, skipping...");
            return;
        }

        String messageKey = record.topic() + "-" + record.partition() + "-" + record.offset();
        if (processedMessages.containsKey(messageKey)) {
            logger.debug("Message already processed: {}", messageKey);
            return;
        }

        try {
            isProcessing.set(true);
            logger.info("Received message - Topic: {}, Partition: {}, Offset: {}", 
                record.topic(), record.partition(), record.offset());
            
            PlutusFinacleData message = record.value();
            if (message != null) {
                logger.info("Message data: foracid={}, tranId={}, tranType={}", 
                    message.getForacid(), message.getTranId(), message.getTranType());
                
                // Store message in database
                MessageEntity messageEntity = new MessageEntity();
                messageEntity.setForacid(message.getForacid());
                messageEntity.setAcctName(message.getAcctName());
                messageEntity.setTranId(message.getTranId());
                messageEntity.setTranAmt(message.getTranAmt());
                messageEntity.setTranDate(message.getTranDate());
                messageEntity.setTranType(message.getTranType());
                messageEntity.setTranParticular(message.getTranParticular());
                messageEntity.setTopic(record.topic());
                messageEntity.setPartition(record.partition());
                messageEntity.setOffset(record.offset());
                messageEntity.setTimestamp(record.timestamp());
                
                messageRepository.save(messageEntity);
                logger.info("Message saved to database with ID: {}", messageEntity.getId());
                
                // Also keep in memory for API access
                receivedMessages.add(message);
                logger.info("Message added to list. Current size: {}", receivedMessages.size());
                
                // Mark message as processed
                processedMessages.put(messageKey, true);
                
                // Acknowledge the message
                ack.acknowledge();
            } else {
                logger.warn("Received null message");
            }
        } catch (Exception e) {
            logger.error("Error processing message: {}", e.getMessage(), e);
        } finally {
            isProcessing.set(false);
        }
    }

    public List<PlutusFinacleData> getReceivedMessages() {
        logger.info("Returning {} received messages", receivedMessages.size());
        return new ArrayList<>(receivedMessages);
    }

    public void clearMessages() {
        logger.info("Clearing {} messages", receivedMessages.size());
        receivedMessages.clear();
        processedMessages.clear();
        messageRepository.deleteAll();
        logger.info("All messages cleared from memory and database");
    }
} 