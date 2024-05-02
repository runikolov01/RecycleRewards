package com.fcst.student.RecycleRewards.service.session;

import org.springframework.stereotype.Component;

@Component
public class LoggedUser {
    private String loggedFirstName;
    private String loggedLastName;
    private String loggedEmail;
    private String loggedPassword;

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
}