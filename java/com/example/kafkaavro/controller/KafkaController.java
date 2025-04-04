package com.example.kafkaavro.controller;

import com.example.kafkaavro.model.PlutusFinacleData;
import com.example.kafkaavro.service.ConsumerService;
import com.example.kafkaavro.service.ProducerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class KafkaController {

    @Autowired
    private ProducerService producerService;

    @Autowired
    private ConsumerService consumerService;

    @PostMapping("/messages")
    public ResponseEntity<String> sendMessage(@RequestBody Map<String, Object> messageMap) {
        try {
            PlutusFinacleData.Builder builder = PlutusFinacleData.newBuilder();
            
            if (messageMap.containsKey("foracid")) {
                builder.setForacid((String) messageMap.get("foracid"));
            }
            if (messageMap.containsKey("acctName")) {
                builder.setAcctName((String) messageMap.get("acctName"));
            }
            if (messageMap.containsKey("tranId")) {
                builder.setTranId((String) messageMap.get("tranId"));
            }
            if (messageMap.containsKey("tranAmt")) {
                builder.setTranAmt(((Number) messageMap.get("tranAmt")).doubleValue());
            }
            if (messageMap.containsKey("tranDate")) {
                builder.setTranDate((String) messageMap.get("tranDate"));
            }
            if (messageMap.containsKey("tranType")) {
                builder.setTranType((String) messageMap.get("tranType"));
            }
            if (messageMap.containsKey("tranParticular")) {
                builder.setTranParticular((String) messageMap.get("tranParticular"));
            }
            
            PlutusFinacleData message = builder.build();
            producerService.sendMessage(message);
            return ResponseEntity.ok("Message sent successfully");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error processing message: " + e.getMessage());
        }
    }

    @GetMapping("/messages")
    public ResponseEntity<List<Map<String, Object>>> getMessages() {
        List<PlutusFinacleData> messages = consumerService.getReceivedMessages();
        List<Map<String, Object>> serializedMessages = new ArrayList<>();
        
        for (PlutusFinacleData message : messages) {
            Map<String, Object> messageMap = new HashMap<>();
            messageMap.put("foracid", message.getForacid());
            messageMap.put("acctName", message.getAcctName());
            messageMap.put("tranId", message.getTranId());
            messageMap.put("tranAmt", message.getTranAmt());
            messageMap.put("tranDate", message.getTranDate());
            messageMap.put("tranType", message.getTranType());
            messageMap.put("tranParticular", message.getTranParticular());
            serializedMessages.add(messageMap);
        }
        
        return ResponseEntity.ok(serializedMessages);
    }

    @DeleteMapping("/messages")
    public ResponseEntity<String> clearMessages() {
        consumerService.clearMessages();
        return ResponseEntity.ok("Messages cleared successfully");
    }
} 