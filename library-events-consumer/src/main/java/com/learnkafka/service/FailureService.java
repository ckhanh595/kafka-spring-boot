package com.learnkafka.service;

import com.learnkafka.entity.FailureRecord;
import com.learnkafka.jpa.FailureRecordRepository;
import lombok.RequiredArgsConstructor;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class FailureService {
    private final FailureRecordRepository failureRecordRepository;

    public void saveFailedRecord(ConsumerRecord<Integer, String> record, Exception exception, String recordStatus) {
        var failureRecord = new FailureRecord(null, record.topic(), record.key(), record.value(),
                record.partition(), record.offset(), exception.getCause().getMessage(), recordStatus);
        failureRecordRepository.save(failureRecord);
    }
}
