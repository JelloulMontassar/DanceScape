package com.dance.mo.Controller;

import com.dance.mo.Entities.*;
import com.dance.mo.Services.CompetitionAndTicketingSystemService;
import com.dance.mo.Services.UserService;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.springframework.http.MediaType.MULTIPART_FORM_DATA_VALUE;
import static org.springframework.http.ResponseEntity.ok;

@RestController
@RequestMapping("/CRUD")
@NoArgsConstructor
@Slf4j

public class CompetitionAndTicketingSystemController {

    @Autowired
    CompetitionAndTicketingSystemService competitionAndTicketingSystemService;
    @Autowired
    private UserService userService;

    @PostMapping("/addCompetition")
    public ResponseEntity<Competition> addCompetition(@RequestBody Competition competition) {
        competition.setDanceVenueAssigned(false);
        Competition addedCompetition = competitionAndTicketingSystemService.addCompetiton(competition);
        return ok(addedCompetition);
    }

    @GetMapping("/getCompetitionById/{id}")
    public ResponseEntity<Competition> getCompetitionById(@PathVariable Long id) {
        Competition competition = competitionAndTicketingSystemService.getCompetitionById(id);
        return competition != null ? ok(competition) : ResponseEntity.notFound().build();
    }

    @GetMapping("/allCompetitions")
    public List<Competition> getAllCompetitions() {
        return competitionAndTicketingSystemService.getAllCompetition();
    }

    @DeleteMapping("/deleteCompetition/{id}")
    public ResponseEntity<String> deleteCompetition(@PathVariable Long id) {
        boolean isDeleted = competitionAndTicketingSystemService.deleteCompetition(id);
        return isDeleted ? ok("Competition deleted successfully") : ResponseEntity.status(HttpStatus.NOT_FOUND).body("Competition not found");
    }

    @PutMapping("/updateCompetition/{id}")
    public ResponseEntity<String> updateCompetition(@PathVariable Long id, @RequestBody Competition updatedCompetition) {
        return competitionAndTicketingSystemService.updateCompetition(id, updatedCompetition);
    }

    @PostMapping("/join")
    public void joinCompetition(@RequestBody Competition competition,@RequestBody User participant){
         competitionAndTicketingSystemService.joinCompetition(competition, participant);
    }

    @PostMapping("/assignCtoD/{idC}/{idD}")
    public void assignCompetitionToADanceVenue(@PathVariable("idC") Long IdCompetition,@PathVariable("idD") Long IdDanceVenue){
        competitionAndTicketingSystemService.affectCompetitionToADanceVenue(IdCompetition,IdDanceVenue);
    }

    @GetMapping("/CompetitionByDanceVenue/{id}")
    public Competition getByDanceVenue(@PathVariable Long id){
        return competitionAndTicketingSystemService.getByDanceVenue(id);
    }

    //DanceVenue Controller
    @PostMapping("/addDanceVenue")
    public DanceVenue addDanceVenue(@RequestBody DanceVenue danceVenue) {
        return competitionAndTicketingSystemService.addDanceVenue(danceVenue);
    }

    @GetMapping("/getDanceVenueById/{id}")
    public ResponseEntity<DanceVenue> getDanceVenueById(@PathVariable Long id) {
        DanceVenue danceVenue = competitionAndTicketingSystemService.getDanceVenueById(id);
        return danceVenue != null ? ok(danceVenue) : ResponseEntity.notFound().build();
    }
    @GetMapping("/getDanceVenueByCompetitionId/{id}")
    public Optional<Object> getDanceVenueByCompetitionId(@PathVariable Long id) {
        return competitionAndTicketingSystemService.getDanceVenueByCompetition(id);
    }

    @GetMapping("/allDanceVenues")
    public List<DanceVenue> getAllDanceVenues() {

        return competitionAndTicketingSystemService.getAllDanceVenues();
    }

    @PutMapping("/updateDanceVenue/{id}")
    public ResponseEntity<String> updateDanceVenue(@PathVariable Long id, @RequestBody DanceVenue danceVenue) {
        return competitionAndTicketingSystemService.updateDanceVenue(id, danceVenue);
    }

    @DeleteMapping("/deleteDanceVenueById/{id}")
    public ResponseEntity<String> deleteDanceVenueById(@PathVariable Long id) {
        boolean isDeleted = competitionAndTicketingSystemService.deleteDanceVenue(id);
        return isDeleted ? ok("Dance venue deleted successfully") : ResponseEntity.status(HttpStatus.NOT_FOUND).body("Dance venue not found");
    }

    //Multimedia controller

    @GetMapping("/getMultimedia")
    public List<Multimedia> getAllMultimedia(){
        return competitionAndTicketingSystemService.gelAllMultimedias();
    }

    @GetMapping("/MultimediaById/{id}")
    public ResponseEntity<Multimedia> getMultimediaById(@PathVariable Long id){
        Multimedia multimedia=competitionAndTicketingSystemService.getMultimediaById(id);
        if(multimedia!=null){
            return ok(multimedia);
        }else{
            return ResponseEntity.notFound().build();
        }
    }
    @PostMapping("/UploadMultimedia")
    public Multimedia addMultimedia(@RequestBody Multimedia multimedia , Authentication authentication){
        User user = userService.getUserByEmail(authentication.getName());
        Competition competition = competitionAndTicketingSystemService.getCompetitionById(multimedia.getCompetition().getCompetitionId());
        multimedia.setUser(user);
        multimedia.setCompetition(competition);
        return competitionAndTicketingSystemService.addMultimedia(multimedia);
    }






   /* @GetMapping( value = "CRUD/video/{title}", produces = "video/mp4")
    public Mono<Resource> streamContent(@PathVariable String title){
        Competition competition=new Competition();
        return competitionAndTicketingSystemService.retrieveContent(title, competition);
    }*/


    //Ticket controller
    /*@PostMapping("/addTicket")
    public ResponseEntity<Ticket> addTicket(@RequestBody Ticket ticket){
        Ticket tickets=competitionAndTicketingSystemService.addTicket(ticket);
        return ResponseEntity.ok(tickets);
    }
    @GetMapping("/allTickets")
    public List<Ticket> getAllTickets(){
        return competitionAndTicketingSystemService.getAllTicket();
    }
    @GetMapping("/getTicketById/{id}")
    public ResponseEntity<Ticket> getTicketById(@PathVariable Long id){
        Ticket ticket=competitionAndTicketingSystemService.getTicketById(id);
        if(ticket!=null){
            return ResponseEntity.ok(ticket);
        }else{
            return ResponseEntity.notFound().build();
        }
    }*/
    @DeleteMapping("/deleteTicketById/{id}")
    public ResponseEntity<String> deleteTicketById(@PathVariable Long id){
        boolean isDeleted=competitionAndTicketingSystemService.deleteTicket(id);
        if(isDeleted){
            return ok("Ticket deleted successfully");
        }else{
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Ticket not found");
        }
    }
    @PutMapping("/updateTicketById/{id}")
    private ResponseEntity<String> updateTicket(@PathVariable Long id, @RequestBody Ticket updatedTicket){
        return competitionAndTicketingSystemService.updateTiket(id, updatedTicket);
    }

    //Payment controller
    @PostMapping("/addPayment")
    public ResponseEntity<Payment> addPayment(@RequestBody Payment payment){
        Payment payments=competitionAndTicketingSystemService.addPayment(payment);
        return ok(payments);
    }
    @GetMapping("/addPaymentById/{id}")
    public ResponseEntity<Payment> getPaymentById(@PathVariable Long id){
        Payment payment=competitionAndTicketingSystemService.getPaymentById(id);
        if (payment!=null){
            return ok(payment);
        }else {
            return ResponseEntity.notFound().build();
        }
    }
    @GetMapping("/findAllPayments")
    public List<Payment> getAllPayments(){
        return competitionAndTicketingSystemService.getAllPayment();
    }


    @PostMapping("/upload_video")
    public String uploadVideo(@RequestParam("file") MultipartFile file){
        competitionAndTicketingSystemService.uploadVideo(file);
        return "File uploaded successfully";
    }
    @PostMapping(value = "up", consumes = MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Map<String, String>> handleFileUploadForm(@RequestPart("video") MultipartFile file) throws IOException {

        log.info("handling request parts: {}", file);

        try {

            File f = new ClassPathResource("").getFile();
            final Path path = Paths.get(f.getAbsolutePath() + File.separator + "static" + File.separator + "image");

            if (!Files.exists(path)) {
                Files.createDirectories(path);
            }

            Path filePath = path.resolve(file.getOriginalFilename());
            Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

            String fileUri = ServletUriComponentsBuilder.fromCurrentContextPath()
                    .path("/image/")
                    .path(file.getOriginalFilename())
                    .toUriString();

            var result = Map.of(
                    "filename", file.getOriginalFilename(),
                    "fileUri", fileUri
            );
            return ok().body(result);

        } catch (IOException e) {
            log.error(e.getMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


}