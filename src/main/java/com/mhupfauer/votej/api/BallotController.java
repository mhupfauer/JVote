package com.mhupfauer.votej.api;

import com.mhupfauer.votej.persistence.Entity.BallotEnt;
import com.mhupfauer.votej.persistence.Repo.BallotEntRepository;
import com.mhupfauer.votej.persistence.Repo.EventEntRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("api/event/{event_id}/ballot")
public class BallotController {

    @Autowired
    private BallotEntRepository ballotEntRepository;

    @Autowired
    private EventEntRepository eventEntRepository;

    @Autowired
    private Helper helper;

    @GetMapping("/")
    public List<BallotEnt> getAllBallots(@PathVariable(name = "event_id") Long eid) {
        return ballotEntRepository.getBallotEntsByEvent(eventEntRepository.findById(eid).get());
    }

    @GetMapping("/{ballot_id}")
    public Object getBallotById(@PathVariable(name = "ballot_id") Long bid) {
        if (ballotEntRepository.findById(bid).isEmpty()) {
            return new ResponseEntity<HttpStatus>(HttpStatus.NOT_FOUND);
        }
        return ballotEntRepository.findById(bid).get();
    }

    @PostMapping("/")
    public ResponseEntity<HttpStatus> newBallot(@PathVariable(name = "event_id") Long eid, @RequestBody Map<String, Object> fields) {
        BallotEnt ballotEnt = new BallotEnt();
        fields = helper.changeField(fields, "event", eid);
        if (helper.setFields(fields, ballotEnt)) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        ballotEntRepository.save(ballotEnt);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PatchMapping("/{ballot_id}")
    public ResponseEntity<HttpStatus> patchBallot(@PathVariable(name = "event_id") Long eid, @PathVariable(name = "ballot_id") Long bid, @RequestBody Map<String, Object> fields) {
        if (ballotEntRepository.findById(bid).isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        fields = helper.changeField(fields, "event", eid);
        BallotEnt ballotEnt = ballotEntRepository.findById(bid).get();
        if (helper.setFields(fields, ballotEnt)) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        ballotEntRepository.save(ballotEnt);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping("/{ballot_id}")
    public ResponseEntity<HttpStatus> deleteBallot(@PathVariable(name = "ballot_id") Long bid, @RequestBody Map<String, Object> fields) {
        if (ballotEntRepository.findById(bid).isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        ballotEntRepository.delete(ballotEntRepository.findById(bid).get());
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
