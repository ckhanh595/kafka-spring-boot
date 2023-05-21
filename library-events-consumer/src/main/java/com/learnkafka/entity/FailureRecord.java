package com.learnkafka.entity;


import lombok.*;

import javax.persistence.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@Entity
public class FailureRecord {
    @Id
    @GeneratedValue
    private Integer bookId;

    private String topic;

    private Integer key;

    private String errorRecord;

    private Integer partition;

    private Long offset_value;

    private String exception;

    private String status;
}
