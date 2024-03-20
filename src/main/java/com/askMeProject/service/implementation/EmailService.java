package com.askMeProject.service.implementation;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;

@Service
@RequiredArgsConstructor
public class EmailService {
    private final JavaMailSender javaMailSender;

    public void sendEmail(String url, String email,  String subject, String mailContent) throws MessagingException, UnsupportedEncodingException {
        String senderName = "Graceman";
        MimeMessage message = javaMailSender.createMimeMessage();
        var messageHelper = new MimeMessageHelper(message);
        messageHelper.setFrom("adetoyesamuel63@gmail.com", senderName);
        messageHelper.setTo(email);
        messageHelper.setSubject(subject);
        messageHelper.setText(mailContent, true);
        javaMailSender.send(message);
    }
    public void sendEmail(String email,  String subject, String mailContent) throws MessagingException, UnsupportedEncodingException {
        String senderName = "Graceman";
        MimeMessage message = javaMailSender.createMimeMessage();
        var messageHelper = new MimeMessageHelper(message);
        messageHelper.setFrom("adetoyesamuel63@gmail.com", senderName);
        messageHelper.setTo(email);
        messageHelper.setSubject(subject);
        messageHelper.setText(mailContent, true);
        javaMailSender.send(message);
    }
}
