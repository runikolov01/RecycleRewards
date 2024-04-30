package com.fcst.student.RecycleRewards.web;

import com.fcst.student.RecycleRewards.model.Person;
import com.fcst.student.RecycleRewards.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
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

    @Autowired
    private UserService userService;

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
            redirectAttributes.addFlashAttribute("error", "Passwords do not match");
            redirectAttributes.addFlashAttribute("firstName", firstName);
            redirectAttributes.addFlashAttribute("lastName", lastName);
            redirectAttributes.addFlashAttribute("email", email);
            redirectAttributes.addFlashAttribute("ageConfirmation", ageConfirmation);
            redirectAttributes.addFlashAttribute("conditionsConfirmation", conditionsConfirmation);

            return "redirect:/register";
        }

        // Check if age confirmation is "yes"
        if (!"yes".equals(ageConfirmation)) {
            redirectAttributes.addFlashAttribute("error", "Age confirmation not accepted");
            redirectAttributes.addFlashAttribute("firstName", firstName);
            redirectAttributes.addFlashAttribute("lastName", lastName);
            redirectAttributes.addFlashAttribute("email", email);
            redirectAttributes.addFlashAttribute("password", password);
            redirectAttributes.addFlashAttribute("confirmPassword", confirmPassword);
            redirectAttributes.addFlashAttribute("conditionsConfirmation", conditionsConfirmation);

            return "redirect:/register";
        }

        // Check if conditions confirmation is "yes"
        if (!"yes".equals(conditionsConfirmation)) {
            redirectAttributes.addFlashAttribute("error", "Conditions confirmation not accepted");
            redirectAttributes.addFlashAttribute("firstName", firstName);
            redirectAttributes.addFlashAttribute("lastName", lastName);
            redirectAttributes.addFlashAttribute("email", email);
            redirectAttributes.addFlashAttribute("password", password);
            redirectAttributes.addFlashAttribute("confirmPassword", confirmPassword);
            redirectAttributes.addFlashAttribute("ageConfirmation", ageConfirmation);

            return "redirect:/register";
        }

        // All validations passed, create a new Person object
        Person person = new Person();
        person.setFirstName(firstName);
        person.setLastName(lastName);
        person.setEmail(email);
        person.setPassword(passwordEncoder.encode(password)); // Encode the password

        // Save the user
        try {
            userService.saveUser(person);
            redirectAttributes.addFlashAttribute("success", true);
            return "redirect:/register";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error registering user");
            return "redirect:/register";
        }
    }
}
