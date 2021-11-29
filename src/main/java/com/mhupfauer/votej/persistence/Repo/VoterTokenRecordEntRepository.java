package com.mhupfauer.votej.persistence.Repo;

import com.mhupfauer.votej.persistence.Entity.QuestionEnt;
import com.mhupfauer.votej.persistence.Entity.UserEnt;
import com.mhupfauer.votej.persistence.Entity.VoterTokenRecordEnt;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface VoterTokenRecordEntRepository extends JpaRepository<VoterTokenRecordEnt, Long> {
    List<VoterTokenRecordEnt> getVoterTokenRecordEntsByUserAndQuestion(UserEnt userEnt, QuestionEnt questionEnt);
    List<VoterTokenRecordEnt> getVoterTokenRecordEntsByQuestion(QuestionEnt questionEnt);
}