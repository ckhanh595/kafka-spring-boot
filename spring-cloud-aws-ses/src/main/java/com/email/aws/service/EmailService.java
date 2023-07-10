package com.email.aws.service;

import lombok.RequiredArgsConstructor;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmailService {
    private final MailSender mailSender;

    public void sendMessage(SimpleMailMessage simpleMailMessage) {
        this.mailSender.send(simpleMailMessage);
    }
}
