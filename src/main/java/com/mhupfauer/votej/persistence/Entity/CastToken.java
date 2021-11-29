package com.mhupfauer.votej.persistence.Entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity(name = "CastToken")
@Table(name = "CASTTOKEN")
@NoArgsConstructor
@Getter
@Setter
public class CastToken {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private Long token;
    private boolean isused;
}
