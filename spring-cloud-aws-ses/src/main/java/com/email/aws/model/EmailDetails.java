package com.email.aws.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class EmailDetails {
    private String fromEmail;
    private String toEmail;
    private String subject;
    private String body;
}
