package com.fcst.student.RecycleRewards.service;

import com.fcst.student.RecycleRewards.model.User;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

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

//    void resetPassword(Long userId, String newPassword);

    User getUserByCode(Long code);
}