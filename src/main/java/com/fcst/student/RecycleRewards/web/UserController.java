package com.fcst.student.RecycleRewards.web;

import com.fcst.student.RecycleRewards.EmailConfiguration;
import com.fcst.student.RecycleRewards.model.*;
import com.fcst.student.RecycleRewards.model.enums.Role;
import com.fcst.student.RecycleRewards.repository.PrizeRepository;
import com.fcst.student.RecycleRewards.repository.UserRepository;
import com.fcst.student.RecycleRewards.service.AddressService;
import com.fcst.student.RecycleRewards.service.EmailService;
import com.fcst.student.RecycleRewards.service.PurchaseService;
import com.fcst.student.RecycleRewards.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
    private EmailConfiguration emailConfiguration;

    @Autowired
    private EmailService emailService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/home")
    public String home(Model model, HttpSession session) {
        Boolean loggedIn = (Boolean) session.getAttribute("loggedIn");
        if (loggedIn == null) {
            loggedIn = false;
        }
        session.setAttribute("loggedIn", loggedIn);
        model.addAttribute("loggedIn", loggedIn);

        Long userId = (Long) session.getAttribute("userId");

        if (loggedIn && userId != null) {
            Optional<User> user = userService.getUserById(userId);
            if (user.isPresent()) {
                Integer bottlesCount = (Integer) session.getAttribute("bottlesCount");

                double userKgBottles = 0.00;
                int userTotalBottles = user.get().getTotalBottles();
                userKgBottles = userTotalBottles * 0.015;


                model.addAttribute("userTotalBottles", userTotalBottles);
                model.addAttribute("userKgBottles", userKgBottles);
                model.addAttribute("bottlesCount", bottlesCount);
                session.setAttribute("bottlesCount", bottlesCount);
                model.addAttribute("loggedUser", user);
                session.setAttribute("loggedUser", user);

                Integer totalPoints = user.get().getTotalPoints();
                model.addAttribute("totalPoints", totalPoints);
            }
        }
        Integer totalBottles = userService.getTotalBottlesForAllUsers();
        if (totalBottles == null) {
            totalBottles = 0;
        }
        model.addAttribute("totalBottles", totalBottles);
        Double totalKg = totalBottles * 0.015;
        model.addAttribute("totalKg", totalKg);
        return "home";
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

        session.setAttribute("loggedIn", loggedIn);
        model.addAttribute("loggedIn", loggedIn);

        Long userId = (Long) session.getAttribute("userId");

        if (userId != null) {
            return "redirect:/myProfile";
        }

        return "register";
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

    @GetMapping("/myProfile")
    public String showProfile(Model model, HttpSession session) {
        Long userId = (Long) session.getAttribute("userId");
        if (userId == null) {
            return "redirect:/login";
        }

        Optional<User> loggedUser = userService.getUserById(userId);
        List<PrizeDetailsDto> prizeDetails = purchaseService.getPrizeDetailsForUser(userId);

        model.addAttribute("prizeDetails", prizeDetails != null ? prizeDetails : Collections.emptyList());
        Integer totalPoints = loggedUser.get().getTotalPoints();
        model.addAttribute("totalPoints", totalPoints);
        model.addAttribute("loggedUser", loggedUser);
        session.setAttribute("loggedUser", loggedUser);
        model.addAttribute("loggedIn", true);

        List<Purchase> purchases = purchaseService.getPurchasesByUserId(userId);
        model.addAttribute("purchases", purchases);

        model.addAttribute("loggedUser", loggedUser);

        return "myprofile";
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
            Optional<User> user = userService.getUserById(userId);
            if (user.isPresent()) {
                String role = String.valueOf(user.get().getRole());
                Integer totalPoints = user.get().getTotalPoints();
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
            userId = (Long) objArray[0];
            Long prizeId = (Long) objArray[1];

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

    @GetMapping("/about")
    public String about(Model model, HttpSession session) {
        Boolean loggedIn = (Boolean) session.getAttribute("loggedIn");
        if (loggedIn == null) {
            loggedIn = false;
        }
        session.setAttribute("loggedIn", loggedIn);
        model.addAttribute("loggedIn", loggedIn);

        Long userId = (Long) session.getAttribute("userId");
        if (userId != null) {
            Optional<User> user = userService.getUserById(userId);
            if (user.isPresent()) {
                model.addAttribute("loggedUser", user);
                session.setAttribute("loggedUser", user);
                Integer totalPoints = user.get().getTotalPoints();
                model.addAttribute("totalPoints", totalPoints);
            }
        }
        return "about";
    }

    @GetMapping("/admin_users")
    public String showUsers(Model model, HttpSession session) {
        Long userId = (Long) session.getAttribute("userId");
        if (userId != null) {
            Optional<User> user = userService.getUserById(userId);
            if (user.isPresent()) {
                String role = String.valueOf(user.get().getRole());
                if (role != null && role.equals("ADMIN")) {
                    Integer totalPoints = user.get().getTotalPoints();
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

    @GetMapping("/admin_users/address/{userCode}")
    @ResponseBody
    public ResponseEntity<String> getUserAddress(@PathVariable("userCode") Long userCode) {
        try {
            User user = userService.getUserByCode(userCode);
            if (user != null) {
                Address address = user.getAddress();
                if (address != null) {
                    String jsonResponse = "{\"city\": \"" + address.getCity() + "\", " + "\"postcode\": \"" + address.getPostcode() + "\", " + "\"street\": \"" + address.getStreet() + "\", " + "\"streetNumber\": \"" + address.getStreetNumber() + "\", " + "\"floor\": \"" + address.getFloor() + "\", " + "\"apartmentNumber\": \"" + address.getApartmentNumber() + "\"}";
                    return ResponseEntity.ok(jsonResponse);
                } else {
                    return ResponseEntity.notFound().build();
                }
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Възникна грешка: " + e.getMessage());
        }
    }

    @GetMapping("/delete/{userCode}")
    public String deleteUser(@PathVariable("userCode") Long userCode) {
        userService.deleteUserByUserCode(userCode);
        return "redirect:/users";
    }

    @GetMapping("/edit/{userId}")
    public String showEditUserForm(@PathVariable("userId") Long userId, Model model) {
        Optional<User> user = userService.getUserById(userId);
        model.addAttribute("user", user);
        return "edit-user";
    }


    @PostMapping("/login")
    public String loginSubmit(@RequestParam String email, @RequestParam String password, Model model, HttpSession session) {
        User user = userService.getUserByEmail(email);
        if (user != null && user.getDeletedDate() == null && userService.verifyPassword(user, password)) {
            if (user.isActivated()) {
                session.setAttribute("userId", user.getId());
                session.setAttribute("loggedIn", true);
                session.setAttribute("userRole", user.getRole());

                Integer totalPoints = user.getTotalPoints();

                session.setAttribute("totalPoints", totalPoints);
                return "redirect:/home";
            } else {
                model.addAttribute("errorNotActivated", true);
                model.addAttribute("error", false);
                return "login";
            }
        } else {
            model.addAttribute("error", true);
            model.addAttribute("errorNotActivated", false);
            return "login";
        }
    }

    @PostMapping("/register")
    public String registerSubmit(@RequestParam String firstName, @RequestParam String lastName, @RequestParam String email, @RequestParam String password, @RequestParam String confirmPassword, @RequestParam(required = false) String ageConfirmation, RedirectAttributes redirectAttributes, Model model) {
        if (!password.equals(confirmPassword)) {
            redirectAttributes.addFlashAttribute("error", "Паролите не съвпадат.");
            redirectAttributes.addFlashAttribute("firstName", firstName);
            redirectAttributes.addFlashAttribute("lastName", lastName);
            redirectAttributes.addFlashAttribute("email", email);
            return "redirect:/register";
        }

        if (!"yes".equals(ageConfirmation)) {
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
        //user.setAddressId(address.getId());
        user.setAddress(address);

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
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Потребителят не е оторизиран!");
            }

            // Check if any required fields are empty
            if (firstName == null || firstName.isEmpty() || lastName == null || lastName.isEmpty() || email == null || email.isEmpty() || city == null || city.isEmpty() || postCode == null || street == null || street.isEmpty() || streetNumber == null) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Моля, попълнете всички полета, маркирани със звездичка, за да актуализирате профила си!");
            }

            Optional<User> currentUser = userService.getUserById(userId);

            // Update user data
            currentUser.get().setFirstName(firstName);
            currentUser.get().setLastName(lastName);
            currentUser.get().setEmail(email);
            currentUser.get().setPhone(telephoneNumber);

            // Check if the user has an associated address
            Address address = currentUser.get().getAddress();
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
            currentUser.get().setAddress(address);

            // Update the user in the database
            userService.updateUser(currentUser.orElse(null));

            return ResponseEntity.ok("Данните са актуализирани успешно!");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Грешка при актуализацията на профила: " + e.getMessage());
        }
    }

    @PatchMapping("/update/{userCode}")
    public String updateUser(@PathVariable("userCode") Long userCode, @RequestBody User updatedUser) {
        User existingUser = userService.getUserByCode(userCode);

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

    @PostMapping("/logout")
    public String logout(HttpSession session, HttpServletResponse response) {
        session.removeAttribute("userId");
        this.userService.logout();
        session.invalidate();
        response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate"); // Prevent caching
        return "redirect:/home";
    }
}