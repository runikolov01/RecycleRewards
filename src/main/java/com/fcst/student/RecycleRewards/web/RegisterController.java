package com.fcst.student.RecycleRewards.web;

import com.fcst.student.RecycleRewards.model.Address;
import com.fcst.student.RecycleRewards.model.User;
import com.fcst.student.RecycleRewards.model.enums.Role;
import com.fcst.student.RecycleRewards.service.AddressService;
import com.fcst.student.RecycleRewards.service.UserService;
import com.fcst.student.RecycleRewards.service.session.LoggedUser;
import jakarta.servlet.http.HttpSession;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
    private AddressService addressService;

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

        // Create a new Address object with empty fields
        Address address = new Address();
        // Save the empty address to get the auto-generated ID
        addressService.saveAddress(address);

        // All validations passed, create a new User object
        User user = new User();
        user.setFirstName(firstName);
        user.setLastName(lastName);
        user.setTotalPoints(0);
        user.setEmail(email);
        user.setPassword(passwordEncoder.encode(password)); // Encode the password
        user.setRegistrationDate(LocalDateTime.now());
        user.setRole(Role.CLIENT);

        // Set the address_id in the User entity
        user.setAddressId(address.getId());

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
    public ResponseEntity<String> updateProfile(@RequestParam(required = false) String firstName,
                                                @RequestParam(required = false) String lastName,
                                                @RequestParam(required = false) String email,
                                                @RequestParam(required = false) String telephoneNumber,
                                                @RequestParam(required = false) String city,
                                                @RequestParam(required = false) Integer postCode,
                                                @RequestParam(required = false) String street,
                                                @RequestParam(required = false) Integer streetNumber,
                                                @RequestParam(required = false) Integer floor,
                                                @RequestParam(required = false) Integer apartmentNumber,
                                                HttpSession session) {
        try {
            Long userId = (Long) session.getAttribute("userId");
            if (userId == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body("User not authorized");
            }

            // Check if any required fields are empty
            if (firstName == null || firstName.isEmpty() ||
                    lastName == null || lastName.isEmpty() ||
                    email == null || email.isEmpty() ||
                    city == null || city.isEmpty() ||
                    postCode == null ||
                    street == null || street.isEmpty() ||
                    streetNumber == null) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body("Моля, попълнете всички полета, маркирани със звездичка.");
            }

            // Retrieve the current user
            User currentUser = userService.getUserById(userId);

            // Update user data
            currentUser.setFirstName(firstName);
            currentUser.setLastName(lastName);
            currentUser.setEmail(email);
            currentUser.setPhone(telephoneNumber);

            // Check if the user has an associated address
            Address address = currentUser.getAddress();
            if (address == null) {
                // If the user doesn't have an address, create a new one
                address = new Address();
            }

            // Update address data
            address.setCity(city);
            address.setPostcode(postCode);
            address.setStreet(street);
            address.setStreetNumber(streetNumber);
            address.setFloor(floor != null ? floor : 0);
            address.setApartmentNumber(apartmentNumber != null ? apartmentNumber : 0);

            // Link the address to the user
            currentUser.setAddress(address);

            // Update the user in the database
            userService.updateUser(currentUser);

            return ResponseEntity.ok("Данните са актуализирани успешно!");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error updating profile: " + e.getMessage());
        }
    }
}