package com.email.aws.controller;

import com.email.aws.model.EmailDetails;
import com.email.aws.service.EmailService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class EmailController {
    private final EmailService emailService;

    @PostMapping("/send-email")
    public ResponseEntity<String> sendMessage(@RequestBody EmailDetails emailDetails) {
        SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
        simpleMailMessage.setFrom(emailDetails.getFromEmail());
        simpleMailMessage.setTo(emailDetails.getToEmail());
        simpleMailMessage.setSubject(emailDetails.getSubject());
        simpleMailMessage.setText(emailDetails.getBody());
        emailService.sendMessage(simpleMailMessage);

        return new ResponseEntity<>("Email sent successfully", HttpStatus.OK);
    }
}
