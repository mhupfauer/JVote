package com.mhupfauer.votej.persistence.Repo;

import com.mhupfauer.votej.persistence.Entity.BallotEnt;
import com.mhupfauer.votej.persistence.Entity.QuestionEnt;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface QuestionEntRepository extends JpaRepository<QuestionEnt, Long> {
  List<QuestionEnt> getQuestionEntsByBallot(BallotEnt ballotEnt);
}
