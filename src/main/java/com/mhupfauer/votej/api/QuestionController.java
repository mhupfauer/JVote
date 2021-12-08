package com.mhupfauer.votej.api;

import com.mhupfauer.votej.persistence.Entity.QuestionEnt;
import com.mhupfauer.votej.persistence.Repo.BallotEntRepository;
import com.mhupfauer.votej.persistence.Repo.QuestionEntRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("api/event/{event_id}/ballot/{ballot_id}/question")
public class QuestionController {

  @Autowired private QuestionEntRepository questionEntRepository;

  @Autowired private BallotEntRepository ballotEntRepository;

  @Autowired private Helper helper;

  @GetMapping("/")
  public Object getAllQuestionsOfBallot(@PathVariable(name = "ballot_id") Long bid) {
    if (ballotEntRepository.findById(bid).isEmpty()) {
      return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
    return questionEntRepository.getQuestionEntsByBallot(ballotEntRepository.findById(bid).get());
  }

  @PostMapping("/")
  public ResponseEntity<HttpStatus> newQuestion(
      @PathVariable(name = "ballot_id") Long bid, @RequestBody Map<String, Object> fields) {
    QuestionEnt questionEnt = new QuestionEnt();
    fields = helper.changeField(fields, "ballot", bid);
    if (helper.setFields(fields, questionEnt)) {
      return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }
    questionEntRepository.save(questionEnt);
    return new ResponseEntity<>(HttpStatus.OK);
  }

  @PatchMapping("/{question_id}")
  public ResponseEntity<HttpStatus> patchQuestion(
      @PathVariable(name = "ballot_id") Long bid,
      @PathVariable(name = "question_id") Long qid,
      @RequestBody Map<String, Object> fields) {
    if (questionEntRepository.findById(qid).isEmpty()) {
      return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
    QuestionEnt questionEnt = questionEntRepository.findById(qid).get();
    fields = helper.changeField(fields, "ballot", bid);
    if (helper.setFields(fields, questionEnt)) {
      return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }
    return new ResponseEntity<>(HttpStatus.OK);
  }

  @DeleteMapping("/{question_id}")
  public ResponseEntity<HttpStatus> deleteQuestion(@PathVariable(name = "question_id") Long qid) {
    if (questionEntRepository.findById(qid).isEmpty()) {
      return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
    questionEntRepository.delete(questionEntRepository.findById(qid).get());
    return new ResponseEntity<>(HttpStatus.OK);
  }
}
