package com.learnkafka.config;

import com.learnkafka.service.FailureRecordService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.common.TopicPartition;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.kafka.ConcurrentKafkaListenerContainerFactoryConfigurer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.dao.RecoverableDataAccessException;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.listener.ConsumerRecordRecoverer;
import org.springframework.kafka.listener.DeadLetterPublishingRecoverer;
import org.springframework.kafka.listener.DefaultErrorHandler;
import org.springframework.kafka.support.ExponentialBackOffWithMaxRetries;
import org.springframework.util.backoff.FixedBackOff;

import java.util.List;

@Configuration
@Slf4j
@RequiredArgsConstructor
public class WikiConsumerConfig {
    public static final String RETRY = "RETRY";
    public static final String SUCCESS = "SUCCESS";
    public static final String DEAD = "DEAD";

    @Value("${topics.retry}")
    private String retryTopic;

    @Value("${topics.dlt}")
    private String deadLetterTopic;

    //    private final KafkaProperties kafkaProperties;
    private final KafkaTemplate kafkaTemplate;
    private final FailureRecordService failureRecordService;

    @Bean
    @ConditionalOnMissingBean(name = "kafkaListenerContainerFactory")
    ConcurrentKafkaListenerContainerFactory<?, ?> kafkaListenerContainerFactory(
            ConcurrentKafkaListenerContainerFactoryConfigurer configurer,
            ConsumerFactory<Object, Object> kafkaConsumerFactory) {

        ConcurrentKafkaListenerContainerFactory<Object, Object> factory = new ConcurrentKafkaListenerContainerFactory<>();
        configurer.configure(factory, kafkaConsumerFactory);

        factory.setCommonErrorHandler(errorHandler());
        return factory;
    }

    public DefaultErrorHandler errorHandler() {
        // Use FixedBackOff
        var fixedBackOff = new FixedBackOff(1000L, 3L);

        // Use ExponentialBackOff
        var expBackOff = new ExponentialBackOffWithMaxRetries(6);
        expBackOff.setInitialInterval(1_000L);
        expBackOff.setMultiplier(2.0);
        expBackOff.setMaxInterval(10_000L);

        var errorHandler = new DefaultErrorHandler(
                publishDbRecover(),
//                publishingRecoverer(),
                expBackOff);

        var exceptionToIgnoreList = List.of(IllegalArgumentException.class);
        exceptionToIgnoreList.forEach(errorHandler::addNotRetryableExceptions);
//        var exceptionToRetryList = List.of(RecoverableDataAccessException.class);
//        exceptionToRetryList.forEach(defaultErrorHandler::addRetryableExceptions);

        return errorHandler;
    }

    public DeadLetterPublishingRecoverer publishingRecoverer() {
        return new DeadLetterPublishingRecoverer(kafkaTemplate, (r, e) -> {
            log.error("Exception in publishingRecoverer : {} ", e.getMessage(), e);

            if (e.getCause() instanceof RecoverableDataAccessException) {
                return new TopicPartition(retryTopic, r.partition());
            }
            return new TopicPartition(deadLetterTopic, r.partition());
        });
    }

    public ConsumerRecordRecoverer publishDbRecover() {
        return (record, exception) -> {
            log.error("Exception is : {} Failed Record : {} ", exception, record);
            if (exception.getCause() instanceof RecoverableDataAccessException) {
                log.info("Inside the recoverable logic");
                failureRecordService.saveFailedRecord((ConsumerRecord<String, String>) record, exception, RETRY);
            } else {
                log.info("Inside the non recoverable logic and skipping the record : {}", record);
                failureRecordService.saveFailedRecord((ConsumerRecord<String, String>) record, exception, DEAD);
            }
        };
    }

//    ConsumerRecordRecoverer consumerRecordRecoverer = (record, exception) -> {
//        log.error("Exception is : {} Failed Record : {} ", exception, record);
//        if (exception.getCause() instanceof RecoverableDataAccessException) {
//            log.info("Inside the recoverable logic");
//            failureService.saveFailedRecord((ConsumerRecord<Integer, String>) record, exception, RETRY);
//        } else {
//            log.info("Inside the non recoverable logic and skipping the record : {}", record);
//            failureService.saveFailedRecord((ConsumerRecord<Integer, String>) record, exception, DEAD);
//        }
//    };

}