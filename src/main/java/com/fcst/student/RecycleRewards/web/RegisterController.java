package com.fcst.student.RecycleRewards.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class RegisterController {

    @Autowired
    private PasswordEncoder passwordEncoder;

    @GetMapping("/register")
    public String registerForm() {
        return "register";
    }

    @PostMapping("/register")
    public String registerSubmit(@RequestParam String firstName,
                                 @RequestParam String lastName,
                                 @RequestParam String email,
                                 @RequestParam String password,
                                 @RequestParam String confirmPassword,
                                 @RequestParam String ageConfirmation,
                                 @RequestParam String conditionsConfirmation,
                                 RedirectAttributes redirectAttributes) {
        // Check if passwords match
        if (!password.equals(confirmPassword)) {
            // Passwords don't match, handle this scenario (e.g., return an error message)
            redirectAttributes.addFlashAttribute("error", "Passwords do not match");
            return "redirect:/register";
        }

        // Check if age confirmation is "yes"
        if (!"yes".equals(ageConfirmation)) {
            // Age confirmation not accepted, handle this scenario
            redirectAttributes.addFlashAttribute("error", "Age confirmation not accepted");
            return "redirect:/register";
        }

        // Check if conditions confirmation is "yes"
        if (!"yes".equals(conditionsConfirmation)) {
            // Conditions confirmation not accepted, handle this scenario
            redirectAttributes.addFlashAttribute("error", "Conditions confirmation not accepted");
            return "redirect:/register";
        }

        // All validations passed, process the registration
        // For simplicity, let's print the form data to the console
        System.out.println("First Name: " + firstName);
        System.out.println("Last Name: " + lastName);
        System.out.println("Email: " + email);
        // You might want to hash the password before storing it in the database
        System.out.println("Password: " + password);
        // Add success message
        redirectAttributes.addFlashAttribute("success", "You have been registered successfully");
        // Redirect to login page
        return "redirect:/login";
    }

}
