package com.example.microfinancepi.services;

import com.example.microfinancepi.entities.Event;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.util.List;

@Service
public class EmailSenderService {
    @Autowired
    private JavaMailSender mailSender;


    public void sendEmail(String recipientEmail, String subject, String message) throws MessagingException {
        MimeMessage mimeMessage = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true);

        helper.setFrom("sarah.bouallegue@esprit.tn");
        helper.setTo(recipientEmail);
        helper.setSubject(subject);
        helper.setText(message);

        mailSender.send(mimeMessage);
    }


    public void sendMembershipValidationEmail(String email, String subject, String message) throws MessagingException {
        // Utilisez la méthode sendEmail pour envoyer l'e-mail
        sendEmail(email, subject, message);
    }

}