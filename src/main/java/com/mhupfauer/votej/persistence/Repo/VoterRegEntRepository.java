package com.mhupfauer.votej.persistence.Repo;

import com.mhupfauer.votej.persistence.Entity.BallotEnt;
import com.mhupfauer.votej.persistence.Entity.UserEnt;
import com.mhupfauer.votej.persistence.Entity.VoterRegEnt;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface VoterRegEntRepository extends JpaRepository<VoterRegEnt, Long> {
  List<VoterRegEnt> getVoterRegEntsByBallot(BallotEnt ballotEnt);

  VoterRegEnt getVoterRegEntByUserAndBallot(UserEnt user, BallotEnt ballotEnt);
}
