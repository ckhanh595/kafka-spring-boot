package com.learnkafka.scheduler;


import com.learnkafka.config.WikiConsumerConfig;
import com.learnkafka.entity.FailureRecord;
import com.learnkafka.repository.FailureRecordRepository;
import com.learnkafka.service.WikiService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.scheduling.annotation.Scheduled;

@Slf4j
@RequiredArgsConstructor
public class RetryScheduler {
    private final WikiService wikiService;
    private final FailureRecordRepository failureRecordRepository;

    @Scheduled(fixedRate = 10000)
    public void retryFailedRecords() {
        log.info("Retrying Failed Records Started!");
        var status = WikiConsumerConfig.RETRY;
        failureRecordRepository.findAllByStatus(status).forEach(failureRecord -> {
            try {
                var consumerRecord = buildConsumerRecord(failureRecord);
                wikiService.processWikiEvent(consumerRecord);
                failureRecord.setStatus(WikiConsumerConfig.SUCCESS);
            } catch (Exception e) {
                log.error("Exception in retryFailedRecords : ", e);
            }
        });
    }

    private ConsumerRecord<String, String> buildConsumerRecord(FailureRecord failureRecord) {
        return new ConsumerRecord<>(failureRecord.getTopic(),
                failureRecord.getRecordPartition(),
                failureRecord.getRecordOffset(),
                failureRecord.getRecordKey(),
                failureRecord.getErrorRecord());
    }
}
