package com.fcst.student.RecycleRewards.service.impl;

import com.fcst.student.RecycleRewards.EmailConfiguration;
import com.fcst.student.RecycleRewards.model.*;
import com.fcst.student.RecycleRewards.model.enums.Role;
import com.fcst.student.RecycleRewards.repository.PrizeRepository;
import com.fcst.student.RecycleRewards.repository.UserRepository;
import com.fcst.student.RecycleRewards.service.AddressService;
import com.fcst.student.RecycleRewards.service.EmailService;
import com.fcst.student.RecycleRewards.service.PurchaseService;
import com.fcst.student.RecycleRewards.service.UserService;
import com.fcst.student.RecycleRewards.service.session.LoggedUser;
import jakarta.mail.internet.MimeMessage;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.support.RequestContextUtils;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.*;

@Service
public class UserServiceImpl implements UserService {
    private final JavaMailSender mailSender;
    private final Environment environment;
    private final ResourceLoader resourceLoader;
    private final PasswordEncoder passwordEncoder;
    private final AddressService addressService;
    private final UserRepository userRepository;
    private final PurchaseService purchaseService;
    private final EmailConfiguration emailConfiguration;
    private final EmailService emailService;
    private final PrizeRepository prizeRepository;
    private final LoggedUser loggedUser;

    @Autowired
    public UserServiceImpl(JavaMailSender mailSender, Environment environment, ResourceLoader resourceLoader, PasswordEncoder passwordEncoder, AddressService addressService, UserRepository userRepository, PurchaseService purchaseService, EmailConfiguration emailConfiguration, EmailService emailService, PrizeRepository prizeRepository, LoggedUser loggedUser) {
        this.mailSender = mailSender;
        this.environment = environment;
        this.resourceLoader = resourceLoader;
        this.passwordEncoder = passwordEncoder;
        this.addressService = addressService;
        this.userRepository = userRepository;
        this.purchaseService = purchaseService;
        this.emailConfiguration = emailConfiguration;
        this.emailService = emailService;
        this.prizeRepository = prizeRepository;
        this.loggedUser = loggedUser;
    }

    @Override
    public void saveUser(User person) {
        userRepository.save(person);
    }

    @Override
    public void updateUser(User person) {
        userRepository.save(person);
    }

    @Override
    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }

    @Override
    public Optional<User> getUserById(Long userId) {
        return userRepository.findById(userId);
    }


    @Override
    public User getUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    @Override
    public User findByResetToken(String resetToken) {
        return userRepository.findByResetToken(resetToken);
    }

    @Override
    public boolean verifyPassword(User user, String password) {
        return passwordEncoder.matches(password, user.getPassword());
    }

    @Override
    public void logout() {
        loggedUser.reset();
    }

    @Override
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public List<User> getParticipantsByPrizeId(Long prizeId) {
        return userRepository.findAllByPrizeId(prizeId);
    }

    @Override
    public Integer getTotalBottlesForAllUsers() {
        return userRepository.getTotalBottlesSum();
    }

    @Override
    public User findByActivationToken(String token) {
        return userRepository.findByActivationToken(token);
    }

    @Override
    public User findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    @Override
    public Long generateUniqueUserCode() {
        Long userCode;
        do {
            userCode = generateRandomCode();
        } while (userRepository.existsByUserCode(userCode));
        return userCode;
    }

    @Override
    public Long generateRandomCode() {
        Random random = new Random();
        // Generate a random 8-digit number
        return 10000000L + Math.abs(random.nextLong() % 90000000L);
    }

    @Override
    public User getUserByUserCode(Long userCode) {
        return userRepository.findByUserCode(userCode);
    }


    @Override
    public String openHomePage(Model model, HttpSession session) {
        Boolean loggedIn = (Boolean) session.getAttribute("loggedIn");
        if (loggedIn == null) {
            loggedIn = false;
        }
        session.setAttribute("loggedIn", loggedIn);
        model.addAttribute("loggedIn", loggedIn);

        Long userId = (Long) session.getAttribute("userId");

        if (loggedIn && userId != null) {
            Optional<User> user = getUserById(userId);
            if (user.isPresent()) {
                Integer bottlesCount = (Integer) session.getAttribute("bottlesCount");

                int userTotalBottles = user.get().getTotalBottles();
                double userKgBottles = userTotalBottles * 0.015;

                model.addAttribute("userTotalBottles", userTotalBottles);
                model.addAttribute("userKgBottles", userKgBottles);
                model.addAttribute("bottlesCount", bottlesCount);
                session.setAttribute("bottlesCount", bottlesCount);
                model.addAttribute("loggedUser", user.get());
                session.setAttribute("loggedUser", user.get());

                Integer totalPoints = user.get().getTotalPoints();
                model.addAttribute("totalPoints", totalPoints);
            }
        }
        Integer totalBottles = getTotalBottlesForAllUsers();
        if (totalBottles == null) {
            totalBottles = 0;
        }
        model.addAttribute("totalBottles", totalBottles);
        Double totalKg = totalBottles * 0.015;
        model.addAttribute("totalKg", totalKg);
        return "home";
    }

    @Override
    public String openRegisterForm(Model model, HttpSession session, HttpServletRequest request) {
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

    @Override
    public String openLoginForm(String error, Model model, HttpSession session) {
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

    @Override
    public String openMyProfile(Model model, HttpSession session) {
        Long userId = (Long) session.getAttribute("userId");
        if (userId == null) {
            return "redirect:/login";
        }

        Optional<User> loggedUserOptional = getUserById(userId);
        if (loggedUserOptional.isEmpty()) {
            return "redirect:/login";
        }

        User loggedUser = loggedUserOptional.get();
        List<PrizeDetailsDto> prizeDetails = purchaseService.getPrizeDetailsForUser(userId);

        model.addAttribute("prizeDetails", prizeDetails != null ? prizeDetails : Collections.emptyList());
        Integer totalPoints = loggedUser.getTotalPoints();
        model.addAttribute("totalPoints", totalPoints);
        model.addAttribute("loggedUser", loggedUser);
        session.setAttribute("loggedUser", loggedUser);
        model.addAttribute("loggedIn", true);

        List<Purchase> purchases = purchaseService.getPurchasesByUserId(userId);
        model.addAttribute("purchases", purchases);

        return "myprofile";
    }


    @Override
    public String openWinnersPage(Model model, HttpSession session) {
        Boolean loggedIn = (Boolean) session.getAttribute("loggedIn");
        if (loggedIn == null) {
            loggedIn = false;
        }
        session.setAttribute("loggedIn", loggedIn);
        model.addAttribute("loggedIn", loggedIn);

        Long userId = (Long) session.getAttribute("userId");

        if (userId != null) {
            Optional<User> user = getUserById(userId);
            if (user.isPresent()) {
                Integer totalPoints = user.get().getTotalPoints();
                model.addAttribute("totalPoints", totalPoints);
                model.addAttribute("loggedUser", user.get());
                session.setAttribute("loggedUser", user.get());

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

    @Override
    public String openAboutPage(Model model, HttpSession session) {
        Boolean loggedIn = (Boolean) session.getAttribute("loggedIn");
        if (loggedIn == null) {
            loggedIn = false;
        }
        session.setAttribute("loggedIn", loggedIn);
        model.addAttribute("loggedIn", loggedIn);

        Long userId = (Long) session.getAttribute("userId");
        if (userId != null) {
            Optional<User> user = getUserById(userId);
            if (user.isPresent()) {
                model.addAttribute("loggedUser", user.get());
                session.setAttribute("loggedUser", user.get());
                Integer totalPoints = user.get().getTotalPoints();
                model.addAttribute("totalPoints", totalPoints);
            }
        }
        return "about";
    }

    @Override
    public String openUsersPage(Model model, HttpSession session) {
        Long userId = (Long) session.getAttribute("userId");
        if (userId != null) {
            Optional<User> user = getUserById(userId);
            if (user.isPresent()) {
                String role = String.valueOf(user.get().getRole());
                if (role != null && role.equals("ADMIN")) {
                    Integer totalPoints = user.get().getTotalPoints();
                    model.addAttribute("totalPoints", totalPoints);
                    model.addAttribute("loggedUser", user.get());
                    session.setAttribute("loggedUser", user.get());

                    model.addAttribute("loggedIn", true);

                    List<User> users = getAllUsers();
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

    @Override
    public ResponseEntity<String> openUserAddress(Long userCode) {
        try {
            User user = getUserByUserCode(userCode);
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
//
//    @Transactional
//    @Override
//    public String deleteUserByCodeProcess(Long userCode) {
//        User user = userRepository.findByUserCode(userCode);
//        user.setDeletedDate(LocalDateTime.now());
//        return "redirect:/users";
//    }

    @Transactional
    @Override
    public String deleteUserByCodeProcess(Long userCode) {
        User user = userRepository.findByUserCode(userCode);

        if (user != null) {

            user.setDeletedDate(LocalDateTime.now());
            userRepository.save(user);
            return "Потребителят е изтрит успешно!";
        }
        return "Потребител с този код не съществува!";
    }


    @Override
    public String openEditUserForm(Long userId, Model model) {
        Optional<User> user = getUserById(userId);
        model.addAttribute("user", user);
        return "edit-user";
    }

    @Override
    public String activateAccountProcess(String token, RedirectAttributes redirectAttributes) {
        User user = findByActivationToken(token);

        if (user == null) {
            redirectAttributes.addFlashAttribute("error", "Вече сте активирали своя профил!");
            return "redirect:/activated_profile";
        }

        if (user.getTokenExpiry().isBefore(LocalDateTime.now())) {
            deleteUser(user.getId());
            redirectAttributes.addFlashAttribute("linkExpired", "Линкът е изтекъл! Изминали са повече от 24 часа от получаването на този email.");
            return "redirect:/activated_profile";
        }

        user.setActivated(true);
        user.setActivationToken(null);
        user.setTokenExpiry(null);
        saveUser(user);

        redirectAttributes.addFlashAttribute("success", "Профилът е активиран успешно!");
        return "redirect:/activated_profile";
    }

    @Override
    public String resetPasswordProcess(String token, RedirectAttributes redirectAttributes) {
        User user = findByResetToken(token);

        if (user == null || user.getTokenExpiry().isBefore(LocalDateTime.now())) {
            redirectAttributes.addFlashAttribute("error", "Линкът за възстановяване на парола е невалиден или е изтекъл.");
            return "redirect:/login";
        }

        redirectAttributes.addFlashAttribute("token", token);
        return "redirect:/show_reset_password_form";
    }

    @Override
    public String registerProcess(String firstName, String lastName, String email, String password, String confirmPassword, String ageConfirmation, RedirectAttributes redirectAttributes, Model model) {
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

        User emailUser = findByEmail(email);
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

        Long userCode = generateUniqueUserCode();
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
            saveUser(user);

            emailService.sendActivationEmail(user, token);

            emailConfiguration.printEmailSettings();

            model.addAttribute("success", "За да активирате своя профил, в следващите 24 часа трябва да натиснете върху линка, изпратен на Вашия email адрес.");
            return "register";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Грешка при регистрацията на потребителя.");
            return "redirect:/register";
        }
    }

    @Override
    public String loginProcess(String email, String password, Model model, HttpSession session) {
        User user = getUserByEmail(email);
        if (user != null && user.getDeletedDate() == null && verifyPassword(user, password)) {
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

    @Override
    public String forgotPasswordProcess(String email, RedirectAttributes redirectAttributes) {
        User user = getUserByEmail(email);
        if (user == null) {
            redirectAttributes.addFlashAttribute("error", "Невалиден email адрес!");
            return "redirect:/login";
        }

        String token = UUID.randomUUID().toString();
        user.setResetToken(token);
        user.setTokenExpiry(LocalDateTime.now().plusHours(24));
        saveUser(user);

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

    @Override
    public String handlePasswordResetProcess(String token, String password, RedirectAttributes redirectAttributes) {
        User user = findByResetToken(token);

        if (user == null || user.getTokenExpiry().isBefore(LocalDateTime.now())) {
            redirectAttributes.addFlashAttribute("error", "Линкът за възстановяване на парола е невалиден или е изтекъл.");
            return "redirect:/login";
        }

        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        String hashedPassword = encoder.encode(password);

        user.setPassword(hashedPassword);
        user.setResetToken(null);
        user.setTokenExpiry(null);
        saveUser(user);

        redirectAttributes.addFlashAttribute("message", "Паролата е възстановена успешно!");
        return "redirect:/login";
    }

    @Override
    public ResponseEntity<String> updateProfileProcess(String firstName, String lastName, String email, Integer telephoneNumber, String city, Integer postCode, String street, Integer streetNumber, Integer floor, Integer apartmentNumber, HttpSession session) {
        try {
            Long userId = (Long) session.getAttribute("userId");
            if (userId == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Потребителят не е оторизиран!");
            }

            // Check if any required fields are empty
            if (firstName == null || firstName.isEmpty() || lastName == null || lastName.isEmpty() || email == null || email.isEmpty() || city == null || city.isEmpty() || postCode == null || street == null || street.isEmpty() || streetNumber == null) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Моля, попълнете всички полета, маркирани със звездичка, за да актуализирате профила си!");
            }

            Optional<User> currentUserOptional = getUserById(userId);
            if (currentUserOptional.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Потребителят не е намерен!");
            }

            User currentUser = currentUserOptional.get();

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
            updateUser(currentUser);

            return ResponseEntity.ok("Данните са актуализирани успешно!");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Грешка при актуализацията на профила: " + e.getMessage());
        }
    }

    @Override
    public String updateUserProcess(Long userCode, User updatedUser) {
        User existingUser = getUserByUserCode(userCode);

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

        updateUser(existingUser);

        return "redirect:/users";
    }

    @Override
    public String logoutProcess(HttpSession session, HttpServletResponse response) {
        session.removeAttribute("userId");
        this.logout();
        session.invalidate();
        response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate"); // Prevent caching
        return "redirect:/home";
    }
}