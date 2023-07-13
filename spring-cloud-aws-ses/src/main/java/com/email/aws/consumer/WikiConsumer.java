package com.email.aws.consumer;

import com.email.aws.service.EmailService;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@RequiredArgsConstructor
public class WikiConsumer {
    private final EmailService emailService;

    @KafkaListener(topics = {"wiki-demo"}, groupId = "wiki-email-group")
    public void onMessage(ConsumerRecord<String, String> consumerRecord) throws JsonProcessingException {
        log.info("ConsumerRecord : {} ", consumerRecord);
        emailService.sendWikiMail(consumerRecord);
    }
}
