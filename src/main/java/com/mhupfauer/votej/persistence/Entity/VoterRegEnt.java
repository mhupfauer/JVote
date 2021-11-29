package com.mhupfauer.votej.persistence.Entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity(name = "VoterReg")
@Table(name = "VOTERREG")
@NoArgsConstructor
@Getter
@Setter
public class VoterRegEnt {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long ID;
    @OneToOne
    @JoinColumn(name = "user_id")
    private UserEnt user;
    private Boolean activated;
    @JsonBackReference
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ballot_id")
    private BallotEnt ballot;
}

