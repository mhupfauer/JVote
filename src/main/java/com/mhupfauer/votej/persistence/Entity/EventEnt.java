package com.mhupfauer.votej.persistence.Entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@Entity(name = "Event")
@Table(name = "EVENTS")
public class EventEnt {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long ID;
    private String title;
    private Long event_timestamp;

    @OneToOne
    @JoinColumn(name = "organizer_id")
    private UserEnt organizer;

    @JsonManagedReference
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "event")
    private List<BallotEnt> ballots;
}
