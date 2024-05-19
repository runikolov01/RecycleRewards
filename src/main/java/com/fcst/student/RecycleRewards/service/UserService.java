package com.fcst.student.RecycleRewards.service;

import com.fcst.student.RecycleRewards.model.User;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface UserService {
    void saveUser(User person);

    User updateUser(User person);

    void deleteUser(Long userId);

    User findUserById(Long userId);

    boolean existsByEmail(String email);

    User getUserById(Long userId);

    User getUserByEmail(String email);

    boolean verifyPassword(User user, String password);

    void logout();

    List<User> getAllUsers();

//    List<User> getUsersByPrize(Long prizeId);
//
//    void awardPrize(User user, Long prizeId);
//
    List<User> getParticipantsByPrizeId(Long prizeId);

}
