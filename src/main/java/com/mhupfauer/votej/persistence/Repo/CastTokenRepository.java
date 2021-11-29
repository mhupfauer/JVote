package com.mhupfauer.votej.persistence.Repo;

import com.mhupfauer.votej.persistence.Entity.CastToken;
import com.mhupfauer.votej.persistence.Entity.QuestionEnt;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CastTokenRepository extends JpaRepository<CastToken, Long> {
    List<CastToken> getCastTokensByQuestion(QuestionEnt questionEnt);
}