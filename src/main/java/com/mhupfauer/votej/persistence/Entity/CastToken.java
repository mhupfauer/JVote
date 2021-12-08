package com.mhupfauer.votej.persistence.Entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.*;

import javax.persistence.*;

@Entity(name = "CastToken")
@Table(name = "CASTTOKEN")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class CastToken {
  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private Long id;

  private Long token;

  @JsonBackReference
  @OneToOne
  @JoinColumn(name = "question_id")
  private QuestionEnt question;
}
