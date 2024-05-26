package com.fcst.student.RecycleRewards.service;

import com.fcst.student.RecycleRewards.model.User;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface UserService {
    void saveUser(User person);

    User updateUser(User person);

    void deleteUser(Long userId);


    User getUserById(Long userId);

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

    void resetPassword(Long userId, String newPassword);
}