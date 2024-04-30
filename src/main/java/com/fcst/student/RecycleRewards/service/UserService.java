package com.fcst.student.RecycleRewards.service;

import com.fcst.student.RecycleRewards.model.Person;
import org.springframework.stereotype.Service;

@Service
public interface UserService {
    Person saveUser(Person person);
    Person updateUser(Person person);
    void deleteUser(Long userId);
    Person findUserById(Long userId);
}
