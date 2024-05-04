package com.dance.mo.Services;

import com.dance.mo.Entities.*;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;

public interface CompetitionAndTicketingSystemService {
    //Competition functions
    public Competition addCompetiton (Competition competition);
    public Competition getCompetitionById(Long competitionId);
    public List<Competition> getAllCompetition();
    public boolean deleteCompetition(Long id);
    public ResponseEntity<String> updateCompetition(Long id, Competition updatedCompetition);
    public void joinCompetition (Competition competition, User participant);
    public void affectCompetitionToADanceVenue(Long IdCompetition, Long IdDanceVenue);



    //DanceVenue functions
    public DanceVenue addDanceVenue(DanceVenue danceVenue);
    public DanceVenue getDanceVenueById(Long DanceVenueId);
    public List<DanceVenue> getAllDanceVenues();
    public boolean deleteDanceVenue(Long id);
    public ResponseEntity<String> updateDanceVenue(Long id, DanceVenue updatedDanceVenue);

    //Multimedia functions

    Multimedia addMultimedia(Multimedia multimedia);
    Multimedia getMultimediaById(Long multimediaId);
    List<Multimedia> gelAllMultimedias();
    void  deleteMultimedia(Long id);

    Competition getByDanceVenue(Long danceVenueId);

    //Ticket functions
    Ticket addTicket(Ticket ticket);
    Ticket getTicketById(Long TicketId);
    List<Ticket> getAllTicket();
    boolean deleteTicket(Long id);
    public ResponseEntity<String> updateTiket(Long id, Ticket updatedTicket);

    //Payment funtions
    Payment addPayment(Payment payment);
    Payment getPaymentById(Long PaymentId);
    List<Payment> getAllPayment();
    boolean deletPayment(Long id);
    public ResponseEntity<String> updatePayment(Long id, Payment updatedPayment);

    //Upload Video
    void uploadVideo(MultipartFile file);

    Optional<Object> getDanceVenueByCompetition(Long id);
}
