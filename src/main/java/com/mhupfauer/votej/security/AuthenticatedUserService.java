package com.mhupfauer.votej.security;

import com.mhupfauer.votej.persistence.Repo.UserEntRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class AuthenticatedUserService implements UserDetailsService {
    @Autowired
    private UserEntRepository userEntRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        username = username.toLowerCase();
        if (userEntRepository.findByEmail(username) == null) {
            throw new UsernameNotFoundException("Username: " + username + " not found");
        }
        return new AuthenticatedUser(userEntRepository.findByEmail(username));
    }
}
