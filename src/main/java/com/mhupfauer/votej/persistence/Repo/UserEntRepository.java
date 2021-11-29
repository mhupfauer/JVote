package com.mhupfauer.votej.persistence.Repo;

import com.mhupfauer.votej.persistence.Entity.UserEnt;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserEntRepository extends JpaRepository<UserEnt, Long> {
    public UserEnt findByEmail(String email);
}