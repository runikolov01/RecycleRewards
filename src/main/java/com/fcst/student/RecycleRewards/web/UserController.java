package com.fcst.student.RecycleRewards.web;

import com.fcst.student.RecycleRewards.model.User;
import com.fcst.student.RecycleRewards.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class UserController {
    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/home")
    public String home(Model model, HttpSession session) {
        return userService.openHomePage(model, session);
    }

    @GetMapping("/register")
    public String registerForm(Model model, HttpSession session, HttpServletRequest request) {
        return userService.openRegisterForm(model, session, request);
    }

    @GetMapping("/login")
    public String loginForm(@RequestParam(required = false) String error, Model model, HttpSession session) {
        return userService.openLoginForm(error, model, session);
    }

    @GetMapping("/myProfile")
    public String showProfile(Model model, HttpSession session) {
        return userService.openMyProfile(model, session);
    }

    @GetMapping("/winners")
    public String showWinners(Model model, HttpSession session) {
        return userService.openWinnersPage(model, session);
    }

    @GetMapping("/about")
    public String about(Model model, HttpSession session) {
        return userService.openAboutPage(model, session);
    }

    @GetMapping("/admin_users")
    public String showUsers(Model model, HttpSession session) {
        return userService.openUsersPage(model, session);
    }

    @GetMapping("/admin_users/address/{userCode}")
    @ResponseBody
    public ResponseEntity<String> getUserAddress(@PathVariable("userCode") Long userCode) {
        return userService.openUserAddress(userCode);
    }

//    @DeleteMapping("/delete/{userCode}")
//    public String deleteUser(@PathVariable("userCode") Long userCode) {
//        return userService.deleteUserByCodeProcess(userCode);
//    }

    @DeleteMapping("/delete/{userCode}")
    public ResponseEntity<String> deleteUser(@PathVariable("userCode") Long userCode) {
        String response = userService.deleteUserByCodeProcess(userCode);
        return ResponseEntity.ok(response); // Return the success message in the response body
    }

    @GetMapping("/edit/{userId}")
    public String showEditUserForm(@PathVariable("userId") Long userId, Model model) {
        return userService.openEditUserForm(userId, model);
    }

    @GetMapping("/activate")
    public String activateAccount(@RequestParam String token, RedirectAttributes redirectAttributes) {
        return userService.activateAccountProcess(token, redirectAttributes);
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
        return userService.resetPasswordProcess(token, redirectAttributes);
    }

    @PostMapping("/register")
    public String registerSubmit(@RequestParam String firstName, @RequestParam String lastName, @RequestParam String email, @RequestParam String password, @RequestParam String confirmPassword, @RequestParam(required = false) String ageConfirmation, RedirectAttributes redirectAttributes, Model model) {
        return userService.registerProcess(firstName, lastName, email, password, confirmPassword, ageConfirmation, redirectAttributes, model);
    }

    @PostMapping("/login")
    public String loginSubmit(@RequestParam String email, @RequestParam String password, Model model, HttpSession session) {
        return userService.loginProcess(email, password, model, session);
    }

    @PostMapping("/forgot_password")
    public String forgotPassword(@RequestParam String email, RedirectAttributes redirectAttributes) {
        return userService.forgotPasswordProcess(email, redirectAttributes);
    }

    @PostMapping("/reset_password")
    public String handlePasswordReset(@RequestParam String token, @RequestParam String password, RedirectAttributes redirectAttributes) {
        return userService.handlePasswordResetProcess(token, password, redirectAttributes);
    }

    @PutMapping("/myProfile")
    public ResponseEntity<String> updateProfile(@RequestParam(required = false) String firstName, @RequestParam(required = false) String lastName, @RequestParam(required = false) String email, @RequestParam(required = false) Integer telephoneNumber, @RequestParam(required = false) String city, @RequestParam(required = false) Integer postCode, @RequestParam(required = false) String street, @RequestParam(required = false) Integer streetNumber, @RequestParam(required = false) Integer floor, @RequestParam(required = false) Integer apartmentNumber, HttpSession session) {
        return userService.updateProfileProcess(firstName, lastName, email, telephoneNumber, city, postCode, street, streetNumber, floor, apartmentNumber, session);
    }

    @PatchMapping("/update/{userCode}")
    public String updateUser(@PathVariable("userCode") Long userCode, @RequestBody User updatedUser) {
        return userService.updateUserProcess(userCode, updatedUser);
    }

    @PostMapping("/logout")
    public String logout(HttpSession session, HttpServletResponse response) {
        return userService.logoutProcess(session, response);
    }
}