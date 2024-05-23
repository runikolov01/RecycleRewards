package com.fcst.student.RecycleRewards.service.impl;

import com.fcst.student.RecycleRewards.model.User;
import com.fcst.student.RecycleRewards.service.EmailService;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.util.StreamUtils;

import java.io.InputStream;
import java.nio.charset.StandardCharsets;

@Service
public class EmailServiceImpl implements EmailService {
    private final JavaMailSender emailSender;
    private final ResourceLoader resourceLoader;

    @Autowired
    public EmailServiceImpl(JavaMailSender emailSender, ResourceLoader resourceLoader) {
        this.emailSender = emailSender;
        this.resourceLoader = resourceLoader;
    }

    @Override
    public void sendActivationEmail(User user, String token) {
        String subject = "Activate Your Account";
        String activationUrl = "http://localhost:8080/activate?token=" + token;

        try {
            // Load HTML template
            Resource resource = resourceLoader.getResource("classpath:/templates/activation-email.html");
            InputStream inputStream = resource.getInputStream();
            String htmlContent = StreamUtils.copyToString(inputStream, StandardCharsets.UTF_8);

            // Replace placeholders with actual values
            htmlContent = htmlContent
                    .replace("${firstName}", user.getFirstName())
                    .replace("${lastName}", user.getLastName())
                    .replace("${activationUrl}", activationUrl);

            // Create and send email
            MimeMessage mimeMessage = emailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true, "UTF-8");
            helper.setTo(user.getEmail());
            helper.setSubject(subject);
            helper.setText(htmlContent, true);
            emailSender.send(mimeMessage);

            System.out.println("Activation email sent to " + user.getEmail());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
