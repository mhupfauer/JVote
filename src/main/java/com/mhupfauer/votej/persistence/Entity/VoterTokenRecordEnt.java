package com.mhupfauer.votej.persistence.Entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity(name = "VotertokenRecord")
@Table(name = "VOTERTOKENRECORD")
@NoArgsConstructor
@Getter
@Setter
@Builder
public class VoterTokenRecordEnt {
    @JsonBackReference
    @ManyToOne
    @JoinColumn(name = "question_id")
    QuestionEnt question;
    @JsonBackReference
    @OneToOne
    @JoinColumn(name = "user_id")
    UserEnt user;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
}
