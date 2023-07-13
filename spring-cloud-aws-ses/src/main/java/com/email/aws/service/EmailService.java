package com.email.aws.service;

import com.email.aws.entity.WikiMedia;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmailService {
    private final MailSender mailSender;
    private final ObjectMapper objectMapper;

    public void sendMessage(SimpleMailMessage simpleMailMessage) {
        this.mailSender.send(simpleMailMessage);
    }

    public void sendWikiMail(ConsumerRecord<String, String> consumerRecord) throws JsonProcessingException {
        var fromEmail = "ckhanh595@gmail.com";
        var toEmail1 = "ckhanh595@hotmail.com";
        var toEmail2 = "ckhanh594@gmail.com";

        var wiki = objectMapper.readValue(consumerRecord.value(), WikiMedia.class);

        var simpleMailMessage = new SimpleMailMessage();
        simpleMailMessage.setFrom(fromEmail);
        simpleMailMessage.setTo("edit".equals(wiki.getType()) ? toEmail1 : toEmail2);
        simpleMailMessage.setSubject(wiki.getTitle());
        simpleMailMessage.setText(wiki.getComment());

        this.mailSender.send(simpleMailMessage);
    }
}
