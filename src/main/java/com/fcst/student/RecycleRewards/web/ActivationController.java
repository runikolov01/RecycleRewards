package com.fcst.student.RecycleRewards.web;

import com.fcst.student.RecycleRewards.model.User;
import com.fcst.student.RecycleRewards.service.UserService;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.UUID;

@Controller
public class ActivationController {

    @Autowired
    private UserService userService;

    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    private Environment environment;

    @Autowired
    private ResourceLoader resourceLoader;

    @GetMapping("/activate")
    public String activateAccount(@RequestParam String token, RedirectAttributes redirectAttributes) {
        User user = userService.findByActivationToken(token);

        if (user == null) {
            redirectAttributes.addFlashAttribute("error", "Вече сте активирали своя профил!");
            return "redirect:/activated_profile";
        }

        if (user.getTokenExpiry().isBefore(LocalDateTime.now())) {
            userService.deleteUser(user.getId());
            redirectAttributes.addFlashAttribute("linkExpired", "Линкът е изтекъл! Изминали са повече от 24 часа от получаването на този email.");
            return "redirect:/activated_profile";
        }

        user.setActivated(true);
        user.setActivationToken(null);
        user.setTokenExpiry(null);
        userService.saveUser(user);

        redirectAttributes.addFlashAttribute("success", "Профилът е активиран успешно!");
        return "redirect:/activated_profile";
    }

    @GetMapping("/activated_profile")
    public String showFailedActivation() {
        return "activated_profile";
    }


    @GetMapping("/show_reset_password_form")
    public String showResetPasswordForm() {
        return "show_reset_password_form";
    }

    @GetMapping("/reset_password")
    public String resetPassword(@RequestParam String token, RedirectAttributes redirectAttributes) {
        User user = userService.findByResetToken(token);

        if (user == null || user.getTokenExpiry().isBefore(LocalDateTime.now())) {
            redirectAttributes.addFlashAttribute("error", "Линкът за възстановяване на парола е невалиден или е изтекъл.");
            return "redirect:/login";
        }

        redirectAttributes.addFlashAttribute("token", token);
        return "redirect:/show_reset_password_form";
    }

    @PostMapping("/forgot_password")
    public String forgotPassword(@RequestParam String email, RedirectAttributes redirectAttributes) {
        User user = userService.getUserByEmail(email);
        if (user == null) {
            redirectAttributes.addFlashAttribute("error", "Невалиден email адрес!");
            return "redirect:/login";
        }

        String token = UUID.randomUUID().toString();
        user.setResetToken(token);
        user.setTokenExpiry(LocalDateTime.now().plusHours(24));
        userService.saveUser(user);

        try {
            sendHtmlEmail(user, token);
            redirectAttributes.addFlashAttribute("message", "Линкът за възстановяване на парола е изпратен на вашия email!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Възникна грешка при изпращането на email.");
            e.printStackTrace();
        }

        return "redirect:/login";
    }

    private void sendHtmlEmail(User user, String token) throws Exception {
        MimeMessage mimeMessage = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, "utf-8");

        String activationUrl = "http://localhost:8080/reset_password?token=" + token;

        Resource resource = resourceLoader.getResource("classpath:templates/forgot_password_email.html");
        String htmlContent = new String(Files.readAllBytes(Paths.get(resource.getURI())));

        htmlContent = htmlContent.replace("${firstName}", user.getFirstName());
        htmlContent = htmlContent.replace("${lastName}", user.getLastName());
        htmlContent = htmlContent.replace("${activationUrl}", activationUrl);

        helper.setText(htmlContent, true);
        helper.setTo(user.getEmail());
        helper.setSubject("Заявка за актуализиране на паролата");
        helper.setFrom(Objects.requireNonNull(environment.getProperty("spring.mail.username")));

        mailSender.send(mimeMessage);
    }

    @PostMapping("/reset_password")
    public String handlePasswordReset(@RequestParam String token, @RequestParam String password, RedirectAttributes redirectAttributes) {
        User user = userService.findByResetToken(token);

        if (user == null || user.getTokenExpiry().isBefore(LocalDateTime.now())) {
            redirectAttributes.addFlashAttribute("error", "Линкът за възстановяване на парола е невалиден или е изтекъл.");
            return "redirect:/login";
        }

        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        String hashedPassword = encoder.encode(password);

        user.setPassword(hashedPassword);
        user.setResetToken(null);
        user.setTokenExpiry(null);
        userService.saveUser(user);

        redirectAttributes.addFlashAttribute("message", "Паролата е възстановена успешно!");
        return "redirect:/login";
    }
}