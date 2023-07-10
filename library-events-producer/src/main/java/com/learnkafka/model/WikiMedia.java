package com.learnkafka.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.Timestamp;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class WikiMedia {
    private Long id;

    private String type;

    @JsonProperty("notify_url")
    private String notifyUrl;

    private Timestamp timestamp;

    @JsonProperty("server_name")
    private String serverName;

    private String comment;

    private String user;
}
