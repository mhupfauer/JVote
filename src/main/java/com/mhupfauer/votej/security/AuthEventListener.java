package com.mhupfauer.votej.security;

import com.mhupfauer.votej.persistence.Entity.UserEnt;
import com.mhupfauer.votej.persistence.Repo.UserEntRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.event.AuthenticationSuccessEvent;
import org.springframework.stereotype.Component;

import java.time.Instant;

@Component
public class AuthEventListener implements ApplicationListener<AuthenticationSuccessEvent> {

    @Autowired
    UserEntRepository userEntRepository;

    @Override
    public void onApplicationEvent(AuthenticationSuccessEvent event) {
        UserEnt userEnt = ((AuthenticatedUser) ((UsernamePasswordAuthenticationToken) event.getSource()).getPrincipal()).getUser();
        userEnt.setLastlogintimestamp(Instant.now().getEpochSecond());
        userEntRepository.save(userEnt);
    }
}
