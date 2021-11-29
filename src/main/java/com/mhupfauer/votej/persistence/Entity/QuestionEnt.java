package com.mhupfauer.votej.persistence.Entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;

@Entity(name = "Question")
@Table(name = "QUESTIONS")
@NoArgsConstructor
@Getter
@Setter
public class QuestionEnt {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long ID;
    private String question_text;
    private String question_opt_explanation;
    @ManyToOne
    @JoinColumn(name = "ballot_id")
    private BallotEnt ballot;
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "question")
    private List<AnswerEnt> answers;
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "question")
    private List<VoterTokenRecordEnt> tokens;
}
