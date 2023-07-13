package com.learnkafka.repository;

import com.learnkafka.entity.WikiMedia;
import org.springframework.data.repository.CrudRepository;

public interface WikiRepository extends CrudRepository<WikiMedia, Long> {
}
