package com.fcst.student.RecycleRewards.service;

import com.fcst.student.RecycleRewards.model.User;
import org.springframework.stereotype.Service;

@Service
public interface EmailService {
     void sendActivationEmail(String email, String token);
}
