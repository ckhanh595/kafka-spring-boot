package com.learnkafka.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.sql.Timestamp;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class WikiMedia {
    @Id
    private Long id;

    private String type;

    private String title;

    @JsonProperty("notify_url")
    private String notifyUrl;

    private Timestamp timestamp;

    @JsonProperty("server_name")
    private String serverName;

    private String comment;

    private String user;
}
