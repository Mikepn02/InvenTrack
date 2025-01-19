package com.rca.stock.kafka.producer;

import com.rca.stock.dto.notification.NotificationMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class StockProducer {
    private final KafkaTemplate<String , NotificationMessage> kafkaTemplate;
    /*
    public void sendStockLevel(NotificationMessage message){
        log.info("Sending Stock Level notification");

        Message<NotificationMessage> notifyMessage = MessageBuilder
                .withPayload(message)
                .setHeader(KafkaHeaders.TOPIC,"stock-topic")
                .build();


        kafkaTemplate.send(notifyMessage);
    }
     */
}
