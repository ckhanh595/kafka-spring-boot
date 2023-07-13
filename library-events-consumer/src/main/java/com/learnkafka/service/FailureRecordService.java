package com.learnkafka.service;

import com.learnkafka.entity.FailureRecord;
import com.learnkafka.repository.FailureRecordRepository;
import lombok.RequiredArgsConstructor;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class FailureRecordService {
    private final FailureRecordRepository failureRecordRepository;

    public void saveFailedRecord(ConsumerRecord<String, String> record, Exception exception, String status) {
        var failureRecord = FailureRecord.builder()
                .topic(record.topic())
                .recordKey(record.key())
                .errorRecord(record.value())
                .recordPartition(record.partition())
                .recordOffset(record.offset())
                .exceptionDetail(exception.getCause().getMessage())
                .status(status)
                .build();
        failureRecordRepository.save(failureRecord);
    }
}
