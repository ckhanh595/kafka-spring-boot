package com.email.aws.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.sql.Timestamp;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class WikiMedia {
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
