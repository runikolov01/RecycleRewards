package com.fcst.student.RecycleRewards.service;

import com.fcst.student.RecycleRewards.model.User;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import jakarta.transaction.Transactional;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.Optional;

@Service
public interface UserService {
    void saveUser(User person);

    void updateUser(User person);

    void deleteUser(Long id);

    @Transactional
    void deleteUserByUserCode(Long userCode);

    Optional<User> getUserById(Long userId);

    User getUserByEmail(String email);

    User findByResetToken(String resetToken);

    boolean verifyPassword(User user, String password);

    void logout();

    List<User> getAllUsers();

    List<User> getParticipantsByPrizeId(Long prizeId);

    Integer getTotalBottlesForAllUsers();

    User findByActivationToken(String token);

    User findByEmail(String email);

    Long generateUniqueUserCode();

    Long generateRandomCode();

    User getUserByUserCode(Long userCode);

    User getUserByCode(Long code);

    String openHomePage(Model model, HttpSession session);

    String openRegisterForm(Model model, HttpSession session, HttpServletRequest request);

    String openLoginForm(@RequestParam(required = false) String error, Model model, HttpSession session);

    String openMyProfile(Model model, HttpSession session);

    String openWinnersPage(Model model, HttpSession session);

    String openAboutPage(Model model, HttpSession session);

    String openUsersPage(Model model, HttpSession session);

    ResponseEntity<String> openUserAddress(Long userCode);

    String loginProcess(String email, String password, Model model, HttpSession session);

    String registerProcess(String firstName, String lastName, String email, String password, String confirmPassword, String ageConfirmation, RedirectAttributes redirectAttributes, Model model);

    ResponseEntity<String> updateProfileProcess(String firstName, String lastName, String email, Integer telephoneNumber, String city, Integer postCode, String street, Integer streetNumber, Integer floor, Integer apartmentNumber, HttpSession session);

    String updateUserProcess(Long userCode, User updatedUser);

    String logoutProcess(HttpSession session, HttpServletResponse response);

    String activateAccountProcess(String token, RedirectAttributes redirectAttributes);

    String resetPasswordProcess(String token, RedirectAttributes redirectAttributes);

    String forgotPasswordProcess(String email, RedirectAttributes redirectAttributes);

    String handlePasswordResetProcess(String token, String password, RedirectAttributes redirectAttributes);
}