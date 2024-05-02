package com.fcst.student.RecycleRewards.service.session;

import org.springframework.stereotype.Component;

@Component
public class LoggedUser {
    private Long loggedUserId;
    private String loggedFirstName;
    private String loggedLastName;
    private String loggedEmail;
    private String loggedPassword;
    private String loggedPhone;
    private String loggedAddress;


    public String getLoggedFirstName() {
        return loggedFirstName;
    }

    public void setLoggedFirstName(String loggedFirstName) {
        this.loggedFirstName = loggedFirstName;
    }

    public String getLoggedLastName() {
        return loggedLastName;
    }

    public void setLoggedLastName(String loggedLastName) {
        this.loggedLastName = loggedLastName;
    }

    public String getLoggedEmail() {
        return loggedEmail;
    }

    public void setLoggedEmail(String loggedEmail) {
        this.loggedEmail = loggedEmail;
    }

    public String getLoggedPassword() {
        return loggedPassword;
    }

    public void setLoggedPassword(String loggedPassword) {
        this.loggedPassword = loggedPassword;
    }

    public String getLoggedPhone() {
        return loggedPhone;
    }

    public void setLoggedPhone(String loggedPhone) {
        this.loggedPhone = loggedPhone;
    }

    public String getLoggedAddress() {
        return loggedAddress;
    }

    public void setLoggedAddress(String loggedAddress) {
        this.loggedAddress = loggedAddress;
    }

    public Long getLoggedUserId() {
        return loggedUserId;
    }

    public void setLoggedUserId(Long loggedUserId) {
        this.loggedUserId = loggedUserId;
    }

    public void reset() {
        setLoggedUserId(null);
        setLoggedFirstName(null);
        setLoggedLastName(null);
        setLoggedEmail(null);
        setLoggedPhone(null);
    }
}