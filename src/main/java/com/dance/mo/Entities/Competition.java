package com.dance.mo.Entities;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.Set;

@Entity
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class Competition implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long competitionId;
    private String name;
    @Temporal(TemporalType.DATE)
    private Date date;
    private String rules;
    @Temporal(TemporalType.DATE)
    private Date expirationDate;
    private int teamNumber;
    private String teamsName  ;

    @OneToMany (cascade = CascadeType.ALL, fetch = FetchType.EAGER, mappedBy = "competition")
    @JsonIgnore
    private Set<Ticket> tickets;
    @JsonIgnore
    @OneToOne
    private DanceVenue danceVenue;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "competition")
    @JsonIgnore
    private Set<Multimedia> multimedia;

    @ManyToMany
    @JsonIgnore
    private List<User> userCompetition;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "competition")
    @JsonIgnore
    private List<DanceCategory> danceCategories;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "competition")

    private List<Feedback> feedback;
    private boolean danceVenueAssigned;


}
