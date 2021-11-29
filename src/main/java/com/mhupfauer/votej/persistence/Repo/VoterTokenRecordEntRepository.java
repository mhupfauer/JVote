package com.mhupfauer.votej.persistence.Repo;

import com.mhupfauer.votej.persistence.Entity.VoterTokenRecordEnt;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VoterTokenRecordEntRepository extends JpaRepository<VoterTokenRecordEnt, Long> {
}