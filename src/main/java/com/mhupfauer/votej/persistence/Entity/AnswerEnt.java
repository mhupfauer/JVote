package com.mhupfauer.votej.persistence.Entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity(name = "Answer")
@Table(name = "ANSWERS")
@NoArgsConstructor
@Getter
@Setter
public class AnswerEnt {
  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private Long ID;

  @OneToOne
  @JoinColumn(name = "casttoken_id")
  private CastToken token;

  @ManyToOne
  @JoinColumn(name = "question_id")
  private QuestionEnt question;

  private String encrypted_answer;
}
