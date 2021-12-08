package com.mhupfauer.votej.persistence.Repo;

import com.mhupfauer.votej.persistence.Entity.EventEnt;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EventEntRepository extends JpaRepository<EventEnt, Long> {}
