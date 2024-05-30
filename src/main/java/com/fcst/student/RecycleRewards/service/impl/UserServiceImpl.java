package com.fcst.student.RecycleRewards.service.impl;

import com.fcst.student.RecycleRewards.model.User;
import com.fcst.student.RecycleRewards.repository.UserRepository;
import com.fcst.student.RecycleRewards.service.UserService;
import com.fcst.student.RecycleRewards.service.session.LoggedUser;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Random;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private LoggedUser loggedUser;

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

    @Transactional
    @Override
    public void deleteUserByUserCode(Long userCode) {
        User user = userRepository.findByUserCode(userCode);
        user.setDeletedDate(LocalDateTime.now());
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

    @Override
    public Optional<User> getUserById(Long userId) {
        return userRepository.findById(userId);
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

//    @Override
//    public void resetPassword(Long userId, String newPassword) {
//        User user = userRepository.findById(userId).orElse(null);
//        if (user != null) {
//            String hashedPassword = passwordEncoder.encode(newPassword);
//            user.setPassword(hashedPassword);
//            userRepository.save(user);
//        }
//    }

    @Override
    public User getUserByCode(Long code) {
        return userRepository.findByUserCode(code);
    }
}