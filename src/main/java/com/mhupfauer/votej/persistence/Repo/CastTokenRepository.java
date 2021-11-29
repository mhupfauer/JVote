package com.mhupfauer.votej.persistence.Repo;

import com.mhupfauer.votej.persistence.Entity.CastToken;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CastTokenRepository extends JpaRepository<CastToken, Long> {
}