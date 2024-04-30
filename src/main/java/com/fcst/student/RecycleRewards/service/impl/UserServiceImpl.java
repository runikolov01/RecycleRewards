package com.fcst.student.RecycleRewards.service.impl;

import com.fcst.student.RecycleRewards.model.Person;
import com.fcst.student.RecycleRewards.repository.UserRepository;
import com.fcst.student.RecycleRewards.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public Person saveUser(Person person) {
        person.setPassword(passwordEncoder.encode(person.getPassword()));
        return userRepository.save(person);
    }

    @Override
    public Person updateUser(Person person) {
        return userRepository.save(person);
    }

    @Override
    public void deleteUser(Long userId) {
        userRepository.deleteById(userId);
    }

    @Override
    public Person findUserById(Long userId) {
        return userRepository.findById(userId).orElse(null);
    }
}
