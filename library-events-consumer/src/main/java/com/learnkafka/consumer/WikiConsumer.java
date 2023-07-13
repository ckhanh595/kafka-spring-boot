package com.learnkafka.consumer;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.learnkafka.service.WikiService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@RequiredArgsConstructor
public class WikiConsumer {
    private final WikiService wikiService;

    @KafkaListener(topics = {"wiki-demo"}, groupId = "wiki-db-group")
    public void onMessage(ConsumerRecord<String, String> consumerRecord) throws JsonProcessingException {
        log.info("ConsumerRecord : {} ", consumerRecord);
        wikiService.processWikiEvent(consumerRecord);
    }
}
