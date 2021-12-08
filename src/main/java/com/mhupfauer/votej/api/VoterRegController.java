package com.mhupfauer.votej.api;

import com.mhupfauer.votej.persistence.Entity.VoterRegEnt;
import com.mhupfauer.votej.persistence.Repo.BallotEntRepository;
import com.mhupfauer.votej.persistence.Repo.EventEntRepository;
import com.mhupfauer.votej.persistence.Repo.UserEntRepository;
import com.mhupfauer.votej.persistence.Repo.VoterRegEntRepository;
import com.mhupfauer.votej.security.ROLES;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.http.HttpResponse;
import java.util.Map;

@RestController
@RequestMapping("api/event/{event_id}/ballot/{ballot_id}/voter")
public class VoterRegController {
  @Autowired private VoterRegEntRepository voterRegEntRepository;

  @Autowired private UserEntRepository userEntRepository;

  @Autowired private EventEntRepository eventEntRepository;

  @Autowired private BallotEntRepository ballotEntRepository;

  @Autowired private Helper helper;

  @GetMapping("/")
  public Object getRegisteredVoters(@PathVariable(name = "ballot_id") Long bid) {
    if (ballotEntRepository.findById(bid).isEmpty()) {
      return new ResponseEntity<HttpResponse>(HttpStatus.NOT_FOUND);
    }
    return voterRegEntRepository.getVoterRegEntsByBallot(ballotEntRepository.findById(bid).get());
  }

  @GetMapping("/{voter_id}")
  public Object getRegisteredVoterById(@PathVariable(name = "voter_id") Long vid) {
    if (voterRegEntRepository.findById(vid).isEmpty()) {
      return new ResponseEntity<HttpResponse>(HttpStatus.NOT_FOUND);
    }
    return voterRegEntRepository.findById(vid).get();
  }

  @PostMapping("/")
  public ResponseEntity<HttpResponse> newRegisteredVoter(
      @PathVariable(name = "event_id") Long eid,
      @PathVariable(name = "ballot_id") Long bid,
      @RequestBody Map<String, Object> fields) {
    if (ballotEntRepository.findById(bid).isEmpty() || eventEntRepository.findById(eid).isEmpty()) {
      return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    // Only admin users or event organizers shall be able to register voters other to them self
    if (fields.containsKey("user")
        && (helper.getCurrentUser().getRole() != ROLES.ADMIN
            || !helper
                .getCurrentUser()
                .equals(eventEntRepository.findById(eid).get().getOrganizer()))) {
      return new ResponseEntity<>(HttpStatus.FORBIDDEN);
    }

    // If user isn't set add current user id to field Map
    if (!fields.containsKey("user")) {
      helper.changeField(fields, "user", helper.getCurrentUser().getId());
    }

    // If user is already registered for this ballot
    if (voterRegEntRepository.getVoterRegEntByUserAndBallot(
            helper.getCurrentUser(), ballotEntRepository.findById(bid).get())
        != null) {
      return new ResponseEntity<>(HttpStatus.FORBIDDEN);
    }

    VoterRegEnt voterRegEnt = new VoterRegEnt();
    helper.changeField(fields, "ballot", bid);
    if (helper.setFields(fields, voterRegEnt)) {
      return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }
    voterRegEntRepository.save(voterRegEnt);
    return new ResponseEntity<>(HttpStatus.OK);
  }

  @PatchMapping("/{voter_reg_id}")
  public ResponseEntity<HttpResponse> editRegisteredVoter(
      @PathVariable(name = "ballot_id") Long bid,
      @PathVariable("voter_reg_id") Long vid,
      @RequestBody Map<String, Object> fields) {
    if (ballotEntRepository.findById(bid).isEmpty()
        || voterRegEntRepository.findById(vid).isEmpty()) {
      return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
    helper.changeField(fields, "ballot", bid);
    VoterRegEnt voterRegEnt = voterRegEntRepository.findById(vid).get();
    if (helper.setFields(fields, voterRegEnt)) {
      return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }
    voterRegEntRepository.save(voterRegEnt);
    return new ResponseEntity<>(HttpStatus.OK);
  }

  @DeleteMapping("/{voter_reg_id}")
  public ResponseEntity<HttpResponse> deleteRegisteredVoter(
      @PathVariable(name = "voter_reg_id") Long vid) {
    if (voterRegEntRepository.findById(vid).isEmpty()) {
      return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
    voterRegEntRepository.delete(voterRegEntRepository.findById(vid).get());
    return new ResponseEntity<>(HttpStatus.OK);
  }
}
