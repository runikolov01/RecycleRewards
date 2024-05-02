package com.fcst.student.RecycleRewards.service.session;

import org.springframework.stereotype.Component;

@Component
public class LoggedUser {
    private Long loggedUserId;

    public Long getLoggedUserId() {
        return loggedUserId;
    }

    public void setLoggedUserId(Long loggedUserId) {
        this.loggedUserId = loggedUserId;
    }

    public void reset() {
        setLoggedUserId(null);
    }
}