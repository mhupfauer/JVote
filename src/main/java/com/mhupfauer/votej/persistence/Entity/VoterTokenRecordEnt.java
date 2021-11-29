package com.mhupfauer.votej.persistence.Entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity(name = "VotertokenRecord")
@Table(name = "VOTERTOKENRECORD")
@NoArgsConstructor
@Getter
@Setter
public class VoterTokenRecordEnt {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @ManyToOne
    @JoinColumn(name = "question_id")
    QuestionEnt question;
    @OneToOne
    @JoinColumn(name = "user_id")
    UserEnt user;
    boolean is_token_issued;
}
