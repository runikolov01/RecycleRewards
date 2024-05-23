package com.fcst.student.RecycleRewards.service.impl;

import com.fcst.student.RecycleRewards.EmailConfiguration;
import com.fcst.student.RecycleRewards.repository.UserRepository;
import com.fcst.student.RecycleRewards.service.EmailService;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
public class EmailServiceImpl implements EmailService {
    private final JavaMailSender emailSender;

    @Autowired
    public EmailServiceImpl(JavaMailSender emailSender) {
        this.emailSender = emailSender;
    }

    @Override
    public void sendActivationEmail(String email, String token) {
        String subject = "Activate Your Account";
        String activationUrl = "http://localhost:8080/activate?token=" + token;
        String message = "Please click the following link to activate your account: " + activationUrl;

        try {
            MimeMessage mimeMessage = emailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage);
            helper.setTo(email);
            helper.setSubject(subject);
            helper.setText(message);
            emailSender.send(mimeMessage);
            System.out.println("Activation email sent to " + email);
        } catch (MessagingException e) {
            System.err.println("Failed to send email: " + e.getMessage());
            e.printStackTrace();        }
    }
}
