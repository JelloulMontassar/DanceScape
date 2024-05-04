package com.dance.mo.Entities;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonView;
import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;
import java.util.List;
import java.util.Set;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class DanceVenue implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)

    private Long danceVenueId;
    private String name;
    private String className;
    private int numberOfSeat;
    @OneToOne
    private Location location;
    @OneToMany(cascade = CascadeType.ALL)
    private Set<Ticket> tickets;

    @OneToOne(mappedBy = "danceVenue")
    private Competition competition;
}