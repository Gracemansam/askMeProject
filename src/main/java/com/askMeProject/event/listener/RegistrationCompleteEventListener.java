package com.askMeProject.event.listener;

import com.askMeProject.event.RegistrationCompleteEvent;
import com.askMeProject.model.User;
import com.askMeProject.service.implementation.EmailService;
import com.askMeProject.service.implementation.UserServiceImplementation;

import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

import java.io.UnsupportedEncodingException;
import java.util.UUID;
@RequiredArgsConstructor
@Component
public class RegistrationCompleteEventListener implements ApplicationListener<RegistrationCompleteEvent> {


    private final UserServiceImplementation authServiceImplementation ;



    private final EmailService emailService;
    private User theUser;
    @Override
    public void onApplicationEvent(RegistrationCompleteEvent event) {
        theUser = event.getUser();

        String verificationToken = UUID.randomUUID().toString();
        authServiceImplementation.saveUserVerificationToken(theUser, verificationToken);
        String url = event.getApplicationUrl()+"/auth/verifyEmail?token="+verificationToken;

        try {
            sendVerificationEmail(url, theUser.getEmail(), theUser.getFirstName());
        } catch (MessagingException | UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
        //log.info("Click the link to verify your registration :  {}", url);
    }

    public void sendVerificationEmail(String url, String email, String firstName) throws MessagingException, UnsupportedEncodingException {
        String subject = "Email Verification";
        String mailContent = "<p> Hi, " + theUser.getFirstName() + ", </p>" +
                "<p>Thank you for registering with us," + "" +
                "Please, follow the link below to complete your registration.</p>" +
                "<a href=\"" + url + "\">Verify your email to activate your account</a>" +
                "<p> Thank you <br> Users Registration Portal Service";
        emailService.sendEmail(url, email, subject, mailContent);
    }
}

