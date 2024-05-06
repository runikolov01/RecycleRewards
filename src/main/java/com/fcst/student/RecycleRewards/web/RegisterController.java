package com.fcst.student.RecycleRewards.web;

import com.fcst.student.RecycleRewards.model.User;
import com.fcst.student.RecycleRewards.model.enums.Role;
import com.fcst.student.RecycleRewards.service.UserService;
import com.fcst.student.RecycleRewards.service.session.LoggedUser;
import jakarta.servlet.http.HttpSession;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDateTime;

@Controller
public class RegisterController {

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UserService userService;

    @Autowired
    private LoggedUser loggedUser;

    @Autowired
    private ModelMapper modelMapper;

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
                                 RedirectAttributes redirectAttributes,
                                 HttpSession session) {
        if (!password.equals(confirmPassword)) {
            // Passwords don't match, redirect back to registration form
            redirectAttributes.addFlashAttribute("error", "Passwords do not match");
            return "redirect:/register";
        }

        if (!"yes".equals(ageConfirmation)) {
            // Age confirmation not accepted, redirect back to registration form
            redirectAttributes.addFlashAttribute("error", "Age confirmation not accepted");
            return "redirect:/register";
        }

        if (!"yes".equals(conditionsConfirmation)) {
            // Conditions confirmation not accepted, redirect back to registration form
            redirectAttributes.addFlashAttribute("error", "Conditions confirmation not accepted");
            return "redirect:/register";
        }

        // All validations passed, create a new User object
        User user = new User();
        user.setFirstName(firstName);
        user.setLastName(lastName);
        user.setEmail(email);
        user.setPassword(passwordEncoder.encode(password)); // Encode the password
        user.setRegistrationDate(LocalDateTime.now());
        user.setRole(Role.CLIENT);

        try {
            // Save the user
            userService.saveUser(user);
            // Store user ID in session
            session.setAttribute("userId", user.getId());
            redirectAttributes.addFlashAttribute("success", true);
            return "redirect:/register";
        } catch (Exception e) {
            // Error saving user, redirect back to registration form
            redirectAttributes.addFlashAttribute("error", "Error registering user");
            return "redirect:/register";
        }
    }

    @PatchMapping("/myProfile")
    public String updateProfile(@RequestParam(required = false) String firstName,
                                @RequestParam(required = false) String lastName,
                                @RequestParam(required = false) String email,
                                @RequestParam(required = false) String telephoneNumber,
                                RedirectAttributes redirectAttributes,
                                HttpSession session) {
        try {
            Long userId = (Long) session.getAttribute("userId");
            if (userId == null) {
                // User ID not found in session, redirect to login
                return "redirect:/login";
            }
            // Retrieve the current user
            User currentUser = userService.getUserById(userId);

            // Update user data only if the parameters are not empty
            if (firstName != null && !firstName.isEmpty()) {
                currentUser.setFirstName(firstName);
            }
            if (lastName != null && !lastName.isEmpty()) {
                currentUser.setLastName(lastName);
            }
            if (email != null && !email.isEmpty()) {
                currentUser.setEmail(email);
            }
            if (telephoneNumber != null && !telephoneNumber.isEmpty()) {
                currentUser.setPhone(telephoneNumber);
            }

            // Update the user in the database
            userService.updateUser(currentUser);

            redirectAttributes.addFlashAttribute("success", true);
            return "redirect:/myProfile";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error updating profile: " + e.getMessage());
            return "redirect:/myProfile";
        }
    }
}