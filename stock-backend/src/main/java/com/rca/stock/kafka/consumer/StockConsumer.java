package com.rca.stock.kafka.consumer;

import com.rca.stock.dto.notification.NotificationMessage;
import com.rca.stock.mapper.NotificationMapper;
import com.rca.stock.repository.NotifcationRepository;
import com.rca.stock.services.EmailService;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class StockConsumer {

    private final NotifcationRepository repository;
    private final NotificationMapper mapper;
    private final EmailService service;

    /*
    @KafkaListener(topics = "stock-topic")
    public void consumeStockLevelNotifications(NotificationMessage message) throws MessagingException{

       try{
           repository.save(mapper.toNotification(message));

           service.sendStockLevelEmail(
                   message.recipientEmail(),
                   message.name(),
                   message.category(),
                   message.quantity(),
                   message.message()
           );
           System.out.println("Here is the stock: " +  message.message());
       }catch (Exception e) {
           log.error("Error processing message: ", e);
       }
    }

     */
}
