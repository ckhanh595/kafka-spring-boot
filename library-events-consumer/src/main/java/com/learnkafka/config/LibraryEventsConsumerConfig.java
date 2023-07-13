//package com.learnkafka.config;
//
//import com.learnkafka.service.FailureService;
////import com.learnkafka.service.LibraryEventsService;
//
//import lombok.RequiredArgsConstructor;
//import lombok.extern.slf4j.Slf4j;
//import org.apache.kafka.clients.consumer.ConsumerRecord;
//import org.apache.kafka.common.TopicPartition;
//import org.springframework.beans.factory.ObjectProvider;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
//import org.springframework.boot.autoconfigure.kafka.ConcurrentKafkaListenerContainerFactoryConfigurer;
//import org.springframework.boot.autoconfigure.kafka.KafkaProperties;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.dao.RecoverableDataAccessException;
//import org.springframework.kafka.annotation.EnableKafka;
//import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
//import org.springframework.kafka.core.ConsumerFactory;
//import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
//import org.springframework.kafka.core.KafkaTemplate;
//import org.springframework.kafka.listener.*;
//import org.springframework.kafka.support.ExponentialBackOffWithMaxRetries;
//import org.springframework.util.backoff.FixedBackOff;
//
//import java.util.List;
//
//@Configuration
//@EnableKafka
//@Slf4j
//@RequiredArgsConstructor
//public class LibraryEventsConsumerConfig {
//    public static final String RETRY = "RETRY";
//    public static final String SUCCESS = "SUCCESS";
//    public static final String DEAD = "DEAD";
//
//    @Value("${topics.retry}")
//    private String retryTopic;
//
//    @Value("${topics.dlt}")
//    private String deadLetterTopic;
//    //
////    private final LibraryEventsService libraryEventsService;
////    private final KafkaProperties kafkaProperties;
//    private final KafkaTemplate kafkaTemplate;
//    private final FailureService failureService;
//
//    @Bean
//    @ConditionalOnMissingBean(name = "kafkaListenerContainerFactory")
//    ConcurrentKafkaListenerContainerFactory<?, ?> kafkaListenerContainerFactory(
//            ConcurrentKafkaListenerContainerFactoryConfigurer configurer,
//            ConsumerFactory<Object, Object> kafkaConsumerFactory) {
//
//        ConcurrentKafkaListenerContainerFactory<Object, Object> factory = new ConcurrentKafkaListenerContainerFactory<>();
//        configurer.configure(factory, kafkaConsumerFactory);
//
////        factory.getContainerProperties().setAckMode(ContainerProperties.AckMode.MANUAL);
////        configurer.configure(factory, kafkaConsumerFactory);
////                .getIfAvailable(() -> new DefaultKafkaConsumerFactory<>(this.kafkaProperties.buildConsumerProperties())));
//        factory.setConcurrency(3);
//        factory.setCommonErrorHandler(errorHandler());
//        return factory;
//    }
//
//
//    public DefaultErrorHandler errorHandler() {
//        // Use FixedBackoff
//        var fixedBackOff = new FixedBackOff(1000L, 2L);
//        var defaultErrorHandler = new DefaultErrorHandler(fixedBackOff);
//
////        var exceptionToIgnoreList = List.of(IllegalArgumentException.class);
////        exceptionToIgnoreList.forEach(defaultErrorHandler::addNotRetryableExceptions);
//        var exceptionToRetryList = List.of(RecoverableDataAccessException.class);
//        exceptionToRetryList.forEach(defaultErrorHandler::addRetryableExceptions);
//
//        // Use ExponentialBackOff
//        var expBackOff = new ExponentialBackOffWithMaxRetries(2);
//        expBackOff.setInitialInterval(1_000L);
//        expBackOff.setMultiplier(2.0);
//        expBackOff.setMaxInterval(2_000L);
//        var errorHandler = new DefaultErrorHandler(
//                publishDbRecover(),
////                publishingRecoverer(),
//                expBackOff);
//
//        errorHandler.setRetryListeners((record, ex, deliveryAttempt) ->
//                log.info("Failed Record in Retry Listener exception : {} , deliveryAttempt : {} ", ex.getMessage(), deliveryAttempt));
//
//        return errorHandler;
//    }
//
//    public DeadLetterPublishingRecoverer publishingRecoverer() {
//        System.out.println(retryTopic + " -123- " + deadLetterTopic);
//        DeadLetterPublishingRecoverer recoverer = new DeadLetterPublishingRecoverer(kafkaTemplate,
//                (r, e) -> {
//                    log.error("Exception in publishingRecoverer : {} ", e.getMessage(), e);
//
//                    if (e.getCause() instanceof RecoverableDataAccessException) {
//                        return new TopicPartition(retryTopic, r.partition());
//                    } else {
//                        return new TopicPartition(deadLetterTopic, r.partition());
//                    }
//                }
//        );
//        return recoverer;
//    }
//
//    public ConsumerRecordRecoverer publishDbRecover() {
//        return (record, exception) -> {
//            log.error("Exception is : {} Failed Record : {} ", exception, record);
//            if (exception.getCause() instanceof RecoverableDataAccessException) {
//                log.info("Inside the recoverable logic");
//                failureService.saveFailedRecord((ConsumerRecord<Integer, String>) record, exception, RETRY);
//            } else {
//                log.info("Inside the non recoverable logic and skipping the record : {}", record);
//                failureService.saveFailedRecord((ConsumerRecord<Integer, String>) record, exception, DEAD);
//            }
//        };
//    }
//
////    ConsumerRecordRecoverer consumerRecordRecoverer = (record, exception) -> {
////        log.error("Exception is : {} Failed Record : {} ", exception, record);
////        if (exception.getCause() instanceof RecoverableDataAccessException) {
////            log.info("Inside the recoverable logic");
////            failureService.saveFailedRecord((ConsumerRecord<Integer, String>) record, exception, RETRY);
////        } else {
////            log.info("Inside the non recoverable logic and skipping the record : {}", record);
////            failureService.saveFailedRecord((ConsumerRecord<Integer, String>) record, exception, DEAD);
////        }
////    };
//
//}