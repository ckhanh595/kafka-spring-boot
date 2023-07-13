package com.learnkafka.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.learnkafka.entity.WikiMedia;
import com.learnkafka.repository.WikiRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.dao.RecoverableDataAccessException;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class WikiService {
    private final WikiRepository wikiRepository;
    private final ObjectMapper objectMapper;
    private final KafkaTemplate<String, String> kafkaTemplate;

    public void processWikiEvent(ConsumerRecord<String, String> consumerRecord) throws JsonProcessingException {
        var wiki = objectMapper.readValue(consumerRecord.value(), WikiMedia.class);
        log.info("wiki : {} ", wiki);

        // Simulate error
        if (wiki.getId() != null && (wiki.getId() == 999)) {
            throw new RecoverableDataAccessException("Temporary Network Issue");
        }

        validate(wiki);
        wikiRepository.save(wiki);
        log.info("Successfully Persisted the library Event {} ", wiki);
    }

    private void validate(WikiMedia wikiMedia) {
        if (wikiMedia.getId() == null) {
            throw new IllegalArgumentException("Id is missing");
        }
        log.info("Validation is successful");
    }

//    public void handleRecovery(ConsumerRecord<Integer, String> record) {
//        var key = record.key();
//        var message = record.value();
//
//        ListenableFuture<SendResult<Integer, String>> listenableFuture = kafkaTemplate.sendDefault(key, message);
//        listenableFuture.addCallback(new ListenableFutureCallback<SendResult<Integer, String>>() {
//            @Override
//            public void onFailure(Throwable ex) {
//                handleFailure(key, message, ex);
//            }
//
//            @Override
//            public void onSuccess(SendResult<Integer, String> result) {
//                handleSuccess(key, message, result);
//            }
//        });
//    }

    private void handleFailure(Integer key, String value, Throwable ex) {
        log.error("Error Sending the Message and the exception is {}", ex.getMessage());
        try {
            throw ex;
        } catch (Throwable throwable) {
            log.error("Error in OnFailure: {}", throwable.getMessage());
        }
    }

    private void handleSuccess(Integer key, String value, SendResult<Integer, String> result) {
        log.info("Message Sent SuccessFully for the key : {} and the value is {} , partition is {}", key, value, result.getRecordMetadata().partition());
    }
}
