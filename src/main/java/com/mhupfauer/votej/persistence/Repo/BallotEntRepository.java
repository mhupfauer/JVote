package com.mhupfauer.votej.persistence.Repo;

import com.mhupfauer.votej.persistence.Entity.BallotEnt;
import com.mhupfauer.votej.persistence.Entity.EventEnt;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BallotEntRepository extends JpaRepository<BallotEnt, Long> {
  List<BallotEnt> getBallotEntsByEvent(EventEnt eid);
}
