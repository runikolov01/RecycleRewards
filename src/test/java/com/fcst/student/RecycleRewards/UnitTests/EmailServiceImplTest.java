package com.fcst.student.RecycleRewards.UnitTests;

import com.fcst.student.RecycleRewards.model.User;
import com.fcst.student.RecycleRewards.service.impl.EmailServiceImpl;
import jakarta.mail.internet.MimeMessage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.mail.javamail.JavaMailSender;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

import static org.mockito.Mockito.*;

public class EmailServiceImplTest {

    @Mock
    private JavaMailSender emailSender;

    @Mock
    private ResourceLoader resourceLoader;

    @InjectMocks
    private EmailServiceImpl emailService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testSendActivationEmail() throws Exception {
        User user = new User();
        user.setFirstName("John");
        user.setLastName("Doe");
        user.setEmail("john.doe@example.com");

        String token = "testToken";
        String htmlTemplate = "<html><body>Hi ${firstName} ${lastName},<br>Click <a href=\"${activationUrl}\">here</a> to activate.</body></html>";

        InputStream inputStream = new ByteArrayInputStream(htmlTemplate.getBytes(StandardCharsets.UTF_8));
        Resource resource = mock(Resource.class);
        when(resource.getInputStream()).thenReturn(inputStream);
        when(resourceLoader.getResource("classpath:/templates/activation-email.html")).thenReturn(resource);

        MimeMessage mimeMessage = mock(MimeMessage.class);
        when(emailSender.createMimeMessage()).thenReturn(mimeMessage);

        emailService.sendActivationEmail(user, token);

        verify(resourceLoader, times(1)).getResource("classpath:/templates/activation-email.html");
        verify(emailSender, times(1)).createMimeMessage();
        verify(emailSender, times(1)).send(mimeMessage);
    }

    @Test
    public void testSendActivationEmailException() throws Exception {
        User user = new User();
        user.setFirstName("John");
        user.setLastName("Doe");
        user.setEmail("john.doe@example.com");

        String token = "testToken";

        when(resourceLoader.getResource("classpath:/templates/activation-email.html"))
                .thenThrow(new RuntimeException("Resource not found"));

        emailService.sendActivationEmail(user, token);

        verify(resourceLoader, times(1)).getResource("classpath:/templates/activation-email.html");
        verify(emailSender, never()).createMimeMessage();
        verify(emailSender, never()).send(any(MimeMessage.class));
    }
}