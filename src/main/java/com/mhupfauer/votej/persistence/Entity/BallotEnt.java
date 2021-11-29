package com.mhupfauer.votej.persistence.Entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
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
    @JsonManagedReference
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "ballot")
    private List<QuestionEnt> questions;
    @OneToOne
    @JoinColumn(name = "current_question_id")
    private QuestionEnt current_question;
    @Column(length = 1000)
    private String public_key;
    @OneToOne
    @JoinColumn(name = "supervisor_id")
    private UserEnt supervisor;
    @JsonBackReference
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "event_id")
    private EventEnt event;
    @JsonManagedReference
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "ballot")
    private List<VoterRegEnt> voters;
}
