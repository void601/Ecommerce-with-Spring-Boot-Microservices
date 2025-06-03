package com.example.UserServiceApplication.ProductMicroservice.serviceimpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

@Service
public class LogServiceImpl {

    @Autowired
    private KafkaTemplate<String, Object> kafkaTemplate; //here string is the key which we are not using here.
    //key is used in partitioning to determine where the data needs to be send which is of object type here.
    //if not specifying key, it will use random partition using round robin strategy.
    public void sendLog(String serviceName, String logLevel, String message) {
        Map<String, String> log = new HashMap<>();
        log.put("timestamp", Instant.now().toString());
        log.put("service", serviceName);
        log.put("level", logLevel);
        log.put("message", message);
        kafkaTemplate.send("application-logs", log);
        //kafkaTemplate.send("application-logs", key,log); let's say key is product service,
        //in this case all logs will be sent to the topic to partition with name product service
        //if nothing specified, then it will be a random process.
    }
}