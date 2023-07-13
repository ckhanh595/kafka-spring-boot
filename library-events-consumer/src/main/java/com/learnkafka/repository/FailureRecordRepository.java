package com.learnkafka.repository;

import com.learnkafka.entity.FailureRecord;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface FailureRecordRepository extends CrudRepository<FailureRecord, Long> {
    List<FailureRecord> findAllByStatus(String status);
}
