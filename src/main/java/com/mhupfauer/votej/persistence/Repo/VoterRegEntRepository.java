package com.mhupfauer.votej.persistence.Repo;

import com.mhupfauer.votej.persistence.Entity.VoterRegEnt;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VoterRegEntRepository extends JpaRepository<VoterRegEnt, Long> {
}