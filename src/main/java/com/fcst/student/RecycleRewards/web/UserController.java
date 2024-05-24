package com.fcst.student.RecycleRewards.web;

import com.fcst.student.RecycleRewards.EmailConfiguration;
import com.fcst.student.RecycleRewards.model.*;
import com.fcst.student.RecycleRewards.model.enums.Role;
import com.fcst.student.RecycleRewards.repository.PrizeRepository;
import com.fcst.student.RecycleRewards.repository.UserRepository;
import com.fcst.student.RecycleRewards.service.*;
import com.fcst.student.RecycleRewards.service.session.LoggedUser;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.support.RequestContextUtils;

import java.time.LocalDateTime;
import java.util.*;

@Controller
public class UserController {
    private final UserService userService;
    private final LoggedUser loggedUser;
    private final ModelMapper modelMapper;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private AddressService addressService;

    @Autowired
    private PurchaseService purchaseService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PrizeRepository prizeRepository;

    @Autowired
    private PrizeService prizeService;


    @Autowired
    private EmailConfiguration emailConfiguration;

    @Autowired
    private JavaMailSender emailSender;

    @Autowired
    private EmailService emailService;

    @Autowired
    public UserController(UserService userService, LoggedUser loggedUser, ModelMapper modelMapper) {
        this.userService = userService;
        this.loggedUser = loggedUser;
        this.modelMapper = modelMapper;
    }

    @GetMapping("/register")
    public String registerForm(Model model, HttpSession session, HttpServletRequest request) {
        Map<String, ?> inputFlashMap = RequestContextUtils.getInputFlashMap(request);
        if (inputFlashMap != null) {
            model.addAllAttributes(inputFlashMap);
        }

        Boolean loggedIn = (Boolean) session.getAttribute("loggedIn");
        if (loggedIn == null) {
            loggedIn = false;
        }

        model.addAttribute("loggedIn", loggedIn);

        Long userId = (Long) session.getAttribute("userId");

        if (userId != null) {
            return "redirect:/myProfile";
        }

        return "register";
    }


    @GetMapping("/myProfile")
    public String showProfile(Model model, HttpSession session) {
        Long userId = (Long) session.getAttribute("userId");
        if (userId == null) {
            return "redirect:/login";
        }

        User loggedUser = userService.getUserById(userId);
        List<PrizeDetailsDto> prizeDetails = purchaseService.getPrizeDetailsForUser(userId);
        model.addAttribute("prizeDetails", prizeDetails != null ? prizeDetails : Collections.emptyList());

        Integer totalPoints = loggedUser.getTotalPoints();
        model.addAttribute("totalPoints", totalPoints);
        model.addAttribute("loggedUser", loggedUser);
        session.setAttribute("loggedUser", loggedUser);
        model.addAttribute("loggedIn", true);

        List<Purchase> purchases = purchaseService.getPurchasesByUserId(userId);
        model.addAttribute("purchases", purchases);

        model.addAttribute("loggedUser", loggedUser);

        return "myprofile";
    }

    @GetMapping("/login")
    public String loginForm(@RequestParam(required = false) String error, Model model, HttpSession session) {
        Boolean loggedIn = (Boolean) session.getAttribute("loggedIn");
        if (loggedIn == null) {
            loggedIn = false;
        }

        model.addAttribute("loggedIn", loggedIn);

        Long userId = (Long) session.getAttribute("userId");

        if (userId != null) {
            return "redirect:/myProfile";
        }


        if (error != null) {
            model.addAttribute("error", true);
        }
        return "login";
    }

//    @GetMapping("/myProfile")
//    public String openMyProfile(Model model, HttpSession session) {
//        Long userId = (Long) session.getAttribute("userId");
//        if (userId != null) {
//            // Fetch user details from the database using the user ID
//            User user = userService.getUserById(userId);
//            if (user != null) {
//                // Get total points for the logged-in user
//                Integer totalPoints = user.getTotalPoints();
//
//                // Pass total points and loggedIn status to the view
//                model.addAttribute("totalPoints", totalPoints);
//                model.addAttribute("loggedUser", user);
//                session.setAttribute("loggedUser", user);
//                model.addAttribute("loggedIn", true); // Set loggedIn to true
//
//                List<Purchase> purchases = purchaseService.getPurchasesByUserId(userId);
//                model.addAttribute("purchases", purchases);
//
//                List<PrizeDetailsDto> prizeDetails = purchaseService.getPrizeDetailsForUser(userId);
//                model.addAttribute("prizeDetails", prizeDetails);
//
//                return "myProfile";
//            }
//        }
//        // Handle case when user ID is not found or user is not found
//        return "redirect:/login"; // or any other appropriate action
//    }


    @GetMapping("/users")
    public String showUsers(Model model, HttpSession session) {
        Long userId = (Long) session.getAttribute("userId");
        if (userId != null) {
            User user = userService.getUserById(userId);
            if (user != null) {
                String role = String.valueOf(user.getRole());
                if (role != null && role.equals("ADMIN")) {
                    Integer totalPoints = user.getTotalPoints();
                    model.addAttribute("totalPoints", totalPoints);
                    model.addAttribute("loggedUser", user);
                    session.setAttribute("loggedUser", user);

                    model.addAttribute("loggedIn", true);

                    List<User> users = userService.getAllUsers();
                    model.addAttribute("users", users);
                    return "admin_users";
                } else {
                    System.out.println("User role: " + role);
                }
            } else {
                System.out.println("User is null for userId: " + userId);
            }
        }
        return "redirect:/home";
    }

    @GetMapping("/winners")
    public String showWinners(Model model, HttpSession session) {
        Boolean loggedIn = (Boolean) session.getAttribute("loggedIn");
        if (loggedIn == null) {
            loggedIn = false;
        }
        session.setAttribute("loggedIn", loggedIn);
        model.addAttribute("loggedIn", loggedIn);

        Long userId = (Long) session.getAttribute("userId");

        if (userId != null) {
            User user = userService.getUserById(userId);
            if (user != null) {
                String role = String.valueOf(user.getRole());
                Integer totalPoints = user.getTotalPoints();
                model.addAttribute("totalPoints", totalPoints);
                model.addAttribute("loggedUser", user);
                session.setAttribute("loggedUser", user);

                model.addAttribute("loggedIn", true);
            }
        }

        List<Object[]> wonPrizes = prizeRepository.findAllWonPrizes();

        //List of maps to store the User and Prize objects associated with each prize
        List<Map<String, Object>> prizeList = new ArrayList<>();
        for (Object[] objArray : wonPrizes) {
            userId = (Long) objArray[0]; // User ID is at index 0
            Long prizeId = (Long) objArray[1]; // Prize ID is at index 1

            User user = userRepository.findById(userId).orElse(null);
            Prize prize = prizeRepository.findById(prizeId).orElse(null);

            if (user != null && prize != null) {
                Map<String, Object> prizeMap = new HashMap<>();
                prizeMap.put("user", user);
                prizeMap.put("prize", prize);
                prizeList.add(prizeMap);
            }
        }

        model.addAttribute("wonPrizes", prizeList);


        return "winners";
    }


    @GetMapping("/delete/{userId}")
    public String deleteUser(@PathVariable("userId") Long userId) {
        userService.deleteUser(userId);
        return "redirect:/users";
    }

    @GetMapping("/edit/{userId}")
    public String showEditUserForm(@PathVariable("userId") Long userId, Model model) {
        User user = userService.getUserById(userId);
        model.addAttribute("user", user);
        return "edit-user";
    }

    @PostMapping("/login")
    public String loginSubmit(@RequestParam String email, @RequestParam String password, Model model, HttpSession session) {
        User user = userService.getUserByEmail(email);
        if (user != null && userService.verifyPassword(user, password)) {
            if (user.isActivated()) {
                // Authentication successful, store user ID and login status in session
                session.setAttribute("userId", user.getId());
                session.setAttribute("loggedIn", true);
                session.setAttribute("userRole", user.getRole());

                // Fetch total points for the logged-in user
                Integer totalPoints = user.getTotalPoints();

                // Set total points as a session attribute
                session.setAttribute("totalPoints", totalPoints);
                return "redirect:/home";
            } else {
                // User not activated
                model.addAttribute("errorNotActivated", true);
                model.addAttribute("error", false); // Set error to false to avoid null value
                return "login";
            }
        } else {
            // Authentication failed
            model.addAttribute("error", true);
            model.addAttribute("errorNotActivated", false); // Set errorNotActivated to false to avoid null value
            return "login";
        }
    }


    @PostMapping("/logout")
    public String logout(HttpSession session, HttpServletResponse response) {
        session.removeAttribute("userId");
        this.userService.logout();
        session.invalidate();
        response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate"); // Prevent caching
        return "redirect:/home";
    }

    @PostMapping("/register")
    public String registerSubmit(@RequestParam String firstName, @RequestParam String lastName, @RequestParam String email,
                                 @RequestParam String password, @RequestParam String confirmPassword,
                                 @RequestParam(required = false) String ageConfirmation, RedirectAttributes redirectAttributes,
                                 HttpSession session, Model model) {
        if (!password.equals(confirmPassword)) {
            redirectAttributes.addFlashAttribute("error", "Паролите не съвпадат.");
            redirectAttributes.addFlashAttribute("firstName", firstName);
            redirectAttributes.addFlashAttribute("lastName", lastName);
            redirectAttributes.addFlashAttribute("email", email);
            return "redirect:/register";
        }

        if (ageConfirmation == null || !"yes".equals(ageConfirmation)) {
            redirectAttributes.addFlashAttribute("error", "Трябва да потвърдите, че имате навършени 18 години. Ако не сте пълнолетен, не можете да се регистрирате.");
            redirectAttributes.addFlashAttribute("firstName", firstName);
            redirectAttributes.addFlashAttribute("lastName", lastName);
            redirectAttributes.addFlashAttribute("email", email);
            redirectAttributes.addFlashAttribute("password", password);
            redirectAttributes.addFlashAttribute("confirmPassword", confirmPassword);
            return "redirect:/register";
        }

        User emailUser = userService.findByEmail(email);
        if (emailUser != null) {
            redirectAttributes.addFlashAttribute("error", "Вече има регистриран потребител с този email адрес.");
            redirectAttributes.addFlashAttribute("firstName", firstName);
            redirectAttributes.addFlashAttribute("lastName", lastName);
            redirectAttributes.addFlashAttribute("email", email);
            return "redirect:/register";
        }

        Address address = new Address();
        addressService.saveAddress(address);

        User user = new User();

        Long userCode = userService.generateUniqueUserCode();
        user.setUserCode(userCode);

        user.setFirstName(firstName);
        user.setLastName(lastName);
        user.setTotalPoints(0);
        user.setEmail(email);
        user.setPassword(passwordEncoder.encode(password));
        user.setRegistrationDate(LocalDateTime.now());
        user.setRole(Role.CLIENT);
        user.setAddressId(address.getId());

        String token = UUID.randomUUID().toString();
        user.setActivationToken(token);
        user.setTokenExpiry(LocalDateTime.now().plusHours(24));
        user.setActivated(false);

        try {
            userService.saveUser(user);

            emailService.sendActivationEmail(user, token);

            emailConfiguration.printEmailSettings();

            model.addAttribute("success", "За да активирате своя профил, в следващите 24 часа трябва да натиснете върху линка, изпратен на Вашия email адрес.");
            return "register";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Грешка при регистрацията на потребителя.");
            return "redirect:/register";
        }
    }


    @PatchMapping("/myProfile")
    public ResponseEntity<String> updateProfile(@RequestParam(required = false) String firstName, @RequestParam(required = false) String lastName, @RequestParam(required = false) String email, @RequestParam(required = false) Integer telephoneNumber, @RequestParam(required = false) String city, @RequestParam(required = false) Integer postCode, @RequestParam(required = false) String street, @RequestParam(required = false) Integer streetNumber, @RequestParam(required = false) Integer floor, @RequestParam(required = false) Integer apartmentNumber, HttpSession session) {
        try {
            Long userId = (Long) session.getAttribute("userId");
            if (userId == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("User not authorized");
            }

            // Check if any required fields are empty
            if (firstName == null || firstName.isEmpty() || lastName == null || lastName.isEmpty() || email == null || email.isEmpty() || city == null || city.isEmpty() || postCode == null || street == null || street.isEmpty() || streetNumber == null) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Моля, попълнете всички полета, маркирани със звездичка.");
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
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error updating profile: " + e.getMessage());
        }
    }

    @PatchMapping("/update/{userId}")
    public String updateUser(@PathVariable("userId") Long userId, @RequestBody User updatedUser) {
        User existingUser = userService.getUserById(userId);

        if (updatedUser.getFirstName() != null) {
            existingUser.setFirstName(updatedUser.getFirstName());
        }
        if (updatedUser.getLastName() != null) {
            existingUser.setLastName(updatedUser.getLastName());
        }
        if (updatedUser.getEmail() != null) {
            existingUser.setEmail(updatedUser.getEmail());
        }
        if (updatedUser.getPhone() != null) {
            existingUser.setPhone(updatedUser.getPhone());
        }
        if (updatedUser.getRole() != null) {
            existingUser.setRole(updatedUser.getRole());
        }

        userService.updateUser(existingUser);

        return "redirect:/users";
    }
}