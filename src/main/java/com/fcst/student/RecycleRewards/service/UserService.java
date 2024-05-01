package com.fcst.student.RecycleRewards.service;

import com.fcst.student.RecycleRewards.model.User;
import org.springframework.stereotype.Service;

@Service
public interface UserService {
    User saveUser(User person);

    User updateUser(User person);

    void deleteUser(Long userId);

    User findUserById(Long userId);

    boolean existsByEmail(String email);
}
