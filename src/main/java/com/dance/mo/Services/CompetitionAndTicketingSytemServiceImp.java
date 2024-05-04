package com.dance.mo.Services;

import com.dance.mo.Entities.*;
import com.dance.mo.Repositories.*;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ResourceLoader;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class CompetitionAndTicketingSytemServiceImp implements CompetitionAndTicketingSystemService {

    @Autowired
    CompetitionRepository competitionRepository;
    @Autowired
    DanceVenueRepository danceVenueRepository;
    @Autowired
    MultimediaRepository multimediaRepository;
    @Autowired
    PaymentRepository paymentRepository;
    @Autowired
    TicketRepository ticketRepository;
    @Autowired
    UserRepository userRepository;
    @Autowired
    private ResourceLoader resourceLoader;
    @Autowired
    private UploadRepository uploadRepository;
    @Autowired
    private LocationRepository locationRepository;
    private static final String FILE_PATH="file:///C:/Users/PC/Videos/%s.mp4";


    //Competition functions
    @Override
    public Competition addCompetiton(Competition competition) {
        return competitionRepository.save(competition);
    }


    @Override
    public Competition getCompetitionById(Long competitionId) {
        return competitionRepository.findById(competitionId).orElse(null);
    }

    @Override
    public List<Competition> getAllCompetition() {
        return competitionRepository.findAll();
    }

    @Override
    public boolean deleteCompetition(Long id) {
        Optional<Competition> competitionOptional = competitionRepository.findById(id);

        if (competitionOptional.isPresent()) {
            competitionRepository.delete(competitionOptional.get());
            return true; // Deletion successful
        } else {
            return false; // Competition not found
        }
    }

    @Override
    public ResponseEntity<String> updateCompetition(Long id, Competition updatedCompetition) {
        Optional<Competition> existingCompetition = competitionRepository.findById(id);
        if (existingCompetition.isPresent()) {
            Competition competition = existingCompetition.get();
            competition.setName(updatedCompetition.getName());
            competition.setDate(updatedCompetition.getDate());
            competition.setRules(updatedCompetition.getRules());

            competitionRepository.save(competition);
            return ResponseEntity.ok("competition updated successfully");
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("competition not found");
        }
    }

    @Override
    public void joinCompetition(Competition competition, User participant) {
        User LoggedInParticipant= userRepository.findById(participant.getUserId()).orElse(null);
        if(LoggedInParticipant==null){
            throw new IllegalArgumentException("Participant not found");
        }
        Competition existingCompetition=competitionRepository.findById(competition.getCompetitionId()).orElse(null);
        if(existingCompetition==null){
            throw new IllegalArgumentException("Competition not found");
        }
        //add user to the competition's list of participant
        List<User> participants=existingCompetition.getUserCompetition();
        participants.add(LoggedInParticipant);
        existingCompetition.setUserCompetition(participants);
        competitionRepository.save(existingCompetition);
    }

    @Override
    public void affectCompetitionToADanceVenue(Long IdCompetition, Long IdDanceVenue) {
        Competition c=competitionRepository.findById(IdCompetition).get();
        DanceVenue danceVenue=danceVenueRepository.findById(IdDanceVenue).get();
        c.setDanceVenueAssigned(true);
        c.setDanceVenue(danceVenue);
        competitionRepository.save(c);
        danceVenueRepository.save(danceVenue);
    }

    @Override
    public Competition getByDanceVenue(Long danceVenueId) {
        return competitionRepository.getCompetitionByDanceVenue_DanceVenueId(danceVenueId);
    }

    // DanceVenue functions
    @Override
    public DanceVenue addDanceVenue(DanceVenue danceVenue) {

        Location location = locationRepository.save(danceVenue.getLocation());
        danceVenue.setLocation(location);
        return danceVenueRepository.save(danceVenue);
    }

    @Override
    public DanceVenue getDanceVenueById(Long danceVenueId) {
        return danceVenueRepository.findById(danceVenueId).orElse(null);
    }
    @Override
    public Optional<Object> getDanceVenueByCompetition(Long competitionId) {
        Optional<Competition> competition = competitionRepository.findById(competitionId);
        return danceVenueRepository.findByCompetition(competition.get());
    }

    @Override
    public List<DanceVenue> getAllDanceVenues() {
        return danceVenueRepository.findAll();
    }

    @Override
    public ResponseEntity<String> updateDanceVenue(Long id, DanceVenue updatedDanceVenue) {
        Optional<DanceVenue> existingDanceVenue = danceVenueRepository.findById(id);
        if (existingDanceVenue.isPresent()) {
            DanceVenue danceVenue = existingDanceVenue.get();
            danceVenue.setName(updatedDanceVenue.getName());
            danceVenue.setNumberOfSeat(updatedDanceVenue.getNumberOfSeat());

            danceVenueRepository.save(danceVenue);
            return ResponseEntity.ok("Dance venue updated successfully");
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Dance venue not found");
        }
    }

    @Override
    public boolean deleteDanceVenue(Long id) {
        Optional<DanceVenue> danceVenueOptional = danceVenueRepository.findById(id);
        danceVenueOptional.ifPresent(danceVenueRepository::delete);
        return danceVenueOptional.isPresent();
    }

    //Multimedia functions


    @Override
    public List<Multimedia> gelAllMultimedias() {
        return multimediaRepository.findAll();
    }
    @Override
    public Multimedia getMultimediaById(Long multimediaId) {
        return multimediaRepository.findById(multimediaId).orElse(null);
    }
    @Override
    public Multimedia addMultimedia(Multimedia multimedia){
        multimediaRepository.save(multimedia);
        return multimedia;
    }
    @Override
    public void deleteMultimedia(Long id){
        multimediaRepository.deleteById(id);
    }



    //Ticket functions

    @Override
    public Ticket addTicket(Ticket ticket) {
        return ticketRepository.save(ticket);
    }

    @Override
    public List<Ticket> getAllTicket() {
        return ticketRepository.findAll();
    }

    @Override
    public Ticket getTicketById(Long TicketId) {
        return ticketRepository.findById(TicketId).orElse(null);
    }

    @Override
    public boolean deleteTicket(Long id) {
        Optional<Ticket> ticketOptional=ticketRepository.findById(id);
        if(ticketOptional.isPresent()){
            ticketRepository.delete(ticketOptional.get());
            return true;
        }else{
            return false;
        }
    }

    @Override
    public ResponseEntity<String> updateTiket(Long id, Ticket updatedTicket) {
        Optional<Ticket> ticketOptional=ticketRepository.findById(id);
        if (ticketOptional.isPresent()){
            Ticket ticket=ticketOptional.get();
            ticket.setTicketType(ticket.getTicketType());
            ticket.setPrice(ticket.getPrice());
            ticketRepository.save(ticket);
            return ResponseEntity.ok("updated successfully");
        }else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Ticket not found");
        }
    }

    //Payment functions

    @Override
    public Payment addPayment(Payment payment) {
        return paymentRepository.save(payment);
    }

    @Override
    public Payment getPaymentById(Long PaymentId) {
        return paymentRepository.findById(PaymentId).orElse(null);
    }

    @Override
    public List<Payment> getAllPayment() {
        return paymentRepository.findAll();
    }

    @Override
    public boolean deletPayment(Long id) {
        Optional<Payment> paymentOptional=paymentRepository.findById(id);
        if(paymentOptional.isPresent()){
            paymentRepository.delete(paymentOptional.get());
            return true;
        }else{
            return false;
        }
    }

    @Override
    public ResponseEntity<String> updatePayment(Long id, Payment updatedPayment) {
        Optional<Payment> paymentOptional = paymentRepository.findById(id);
        if (paymentOptional.isPresent()) {
            Payment payment = paymentOptional.get();
            payment.setAmount(payment.getAmount());
            payment.setPaymentWay(payment.getPaymentWay());
            paymentRepository.save(payment);
            return ResponseEntity.ok("payment updated successfully");
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("payment not found");
        }
    }


    //Upload


    @Override
    @Transactional
    public void uploadVideo(MultipartFile file) {
        String filePath="C:/Users/PC/Desktop/uploads/"+file.getOriginalFilename();
        try{
            file.transferTo(new File(filePath));
        }catch (IOException e){
            e.printStackTrace();
            throw new RuntimeException("Failed to upload file");
        }

        Upload video=new Upload();
        video.setPath(filePath);
        video.setUploadDate(LocalDateTime.now());
        uploadRepository.save(video);
    }
}