package com.mhupfauer.votej.persistence.Repo;

import com.mhupfauer.votej.persistence.Entity.QuestionEnt;
import org.springframework.data.jpa.repository.JpaRepository;

public interface QuestionEntRepository extends JpaRepository<QuestionEnt, Long> {
}