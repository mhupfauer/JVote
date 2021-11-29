package com.mhupfauer.votej.persistence.Entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;

@Entity(name = "Ballot")
@Table(name = "BALLOTS")
@NoArgsConstructor
@Getter
@Setter
public class BallotEnt {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long ID;
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "ballot")
    private List<QuestionEnt> questions;
    @OneToOne
    @JoinColumn(name = "current_question_id")
    private QuestionEnt current_question;
    private String public_key;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "event_id")
    private EventEnt event;
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "ballot")
    private List<VoterRegEnt> voters;
}
